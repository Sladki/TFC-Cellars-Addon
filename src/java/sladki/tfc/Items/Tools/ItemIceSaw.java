package sladki.tfc.Items.Tools;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sladki.tfc.Cellars;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Items.Tools.ItemTerraTool;
import com.google.common.collect.Sets;

public class ItemIceSaw extends ItemTerraTool {

	private static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet(new Block[]{TFCBlocks.Ice});

	
	public ItemIceSaw(ToolMaterial material) {
		super(1.0f, material, blocksEffectiveAgainst);
	}
	
	@Override
	public float func_150893_a(ItemStack par1ItemStack, Block par2Block) {
		return par2Block != null && par2Block.getMaterial() == Material.ice ? this.efficiencyOnProperMaterial*0.1F : super.func_150893_a(par1ItemStack, par2Block);
	}
	
	@Override
	public void registerIcons(IIconRegister register) {
		String name = this.getUnlocalizedName().replace("item.", "");
		this.itemIcon = register.registerIcon(Cellars.MODID + ":" + "tools/" + name);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase player) {
		super.onBlockDestroyed(itemStack, world, block, x, y, z, player);
		
		if(world.isRemote) {
			return false;
		}
		
		if(block == TFCBlocks.Ice) {
			EntityItem entityItem = new EntityItem(world,
					x + 0.5, y + 0.5, z + 0.5, new ItemStack(TFCBlocks.Ice, 1));
			world.spawnEntityInWorld(entityItem);
			world.setBlockToAir(x, y, z);
		}
        return true;
    }
	
}
