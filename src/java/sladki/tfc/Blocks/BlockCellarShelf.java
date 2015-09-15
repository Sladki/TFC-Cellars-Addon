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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sladki.tfc.Cellars;
import sladki.tfc.TileEntities.TECellarShelf;

import com.bioxx.tfc.Core.TFCTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCellarShelf extends BlockContainer {
	
	public static IIcon textureSide;
	public static int renderId = 0;

	public BlockCellarShelf(Material material) {
		super(material);
		this.setCreativeTab(TFCTabs.TFC_DEVICES);
		this.setStepSound(Block.soundTypeWood);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return textureSide;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister registerer) {
		textureSide = registerer.registerIcon("minecraft:planks_oak");
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return renderId;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		}
		
		if(player.isSneaking()) {
			return false;
		}
		
		TECellarShelf tileEntity = (TECellarShelf) world.getTileEntity(x, y, z);
		if(tileEntity != null) {
			
			//TODO:Delete these
			
			//tileEntity.getShelfInfo(player);
			
			//
			
			player.openGui(Cellars.instance, 1, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TECellarShelf();
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