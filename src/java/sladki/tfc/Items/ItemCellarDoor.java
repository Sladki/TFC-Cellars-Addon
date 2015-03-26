package sladki.tfc.Items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import sladki.tfc.Cellars;
import sladki.tfc.ModManager;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;

public class ItemCellarDoor extends ItemTerra {

	public ItemCellarDoor() {
		super();
		this.maxStackSize = 1;
		this.setUnlocalizedName("CellarDoor");
		this.setCreativeTab(TFCTabs.TFCBuilding);
		this.setTextureName(Cellars.MODID + ":" + "cellarDoor");
	}
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (side != 1) {
			return false;
		}
		
		++y;
		Block block = ModManager.CellarDoorBlock;
		
		if(player.canPlayerEdit(x, y, z, side, is) && player.canPlayerEdit(x, y + 1, z, side, is)) {
			if(!block.canPlaceBlockAt(world, x, y, z)) {
				return false;
			}
			
			side = MathHelper.floor_double((player.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
			placeDoorBlock(world, x, y, z, side, block);
			--is.stackSize;
			return true;
		}
		
		return false;
	}
	
	public static void placeDoorBlock(World world, int x, int y, int z, int side, Block block) {
		byte var6 = 0;
		byte var7 = 0;
		
		if (side == 0) {
			var7 = 1;
		} else if (side == 1) {
			var6 = -1;
		} else if (side == 2) {
		var7 = -1;
		} else if (side == 3) {
			var6 = 1;
		}
		
		int var8 = (world.getBlock(x - var6, y, z - var7).isNormalCube() ? 1 : 0) + (world.getBlock(x - var6, y + 1, z - var7).isNormalCube() ? 1 : 0);
		int var9 = (world.getBlock(x + var6, y, z + var7).isNormalCube() ? 1 : 0) + (world.getBlock(x + var6, y + 1, z + var7).isNormalCube() ? 1 : 0);
		
		boolean var10 = world.getBlock(x - var6, y, z - var7) == block || world.getBlock(x - var6, y + 1, z - var7) == block;
		boolean var11 = world.getBlock(x + var6, y, z + var7) == block || world.getBlock(x + var6, y + 1, z + var7) == block;
		boolean var12 = false;
		
		if (var10 && !var11) {
			var12 = true;
		} else if (var9 > var8)	{
			var12 = true;
		}
		
		world.setBlock(x, y, z, block, side, 0x2);
		world.setBlock(x, y + 1, z, block, 8 | (var12 ? 1 : 0), 0x2);
		world.notifyBlocksOfNeighborChange(x, y, z, block);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
	}
	
	@Override
	public void registerIcons(IIconRegister register) {
		this.itemIcon = register.registerIcon(Cellars.MODID + ":" + "cellarDoor");
	}
	
	@Override
	public EnumSize getSize(ItemStack is) {
		return EnumSize.HUGE;
	}
	
	@Override
	public EnumWeight getWeight(ItemStack is) {
		return EnumWeight.HEAVY;
	}

}
