package sladki.tfc.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import sladki.tfc.Cellars;
import sladki.tfc.TileEntities.TEIceBunker;

import com.bioxx.tfc.Core.TFCTabs;

public class BlockIceBunker extends BlockContainer {
	
	private static IIcon textureTop;
	private static IIcon textureSide;
	
	public BlockIceBunker(Material material) {
		super(material);
		this.setCreativeTab(TFCTabs.TFCDevices);
		this.setStepSound(Block.soundTypeWood);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		}
		
		boolean getInfo = false;
		if(player.isSneaking()) {
			if(player.getCurrentEquippedItem() == null) {
				getInfo = true;
			} else {
				return false;
			}
		}
		
		TEIceBunker tileEntity = (TEIceBunker) world.getTileEntity(x, y, z);
		if(tileEntity != null) {
			if(getInfo) {
				tileEntity.getCellarInfo(player);
				return true;
			}
			player.openGui(Cellars.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		if(side == 1) {
			return textureTop;
		}
		return textureSide;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister registerer) {
		textureSide = registerer.registerIcon("minecraft" + ":" + "planks_oak");
		textureTop = registerer.registerIcon(Cellars.MODID + ":" + "iceBunkerTop");
	}


	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEIceBunker();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		 dropItems(world, x, y, z);
		 super.breakBlock(world, x, y, z, block, metadata);
	}

	private void dropItems(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;
		
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			
			if (item != null && item.stackSize > 0) {
				EntityItem entityItem = new EntityItem(world,
						x + 0.5, y + 0.5, z + 0.5, item);
				world.spawnEntityInWorld(entityItem);
			}
		}
	}
}
