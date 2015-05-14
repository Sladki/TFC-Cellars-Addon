package sladki.tfc.Containers;

import com.bioxx.tfc.api.TFCBlocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sladki.tfc.ModManager;

public class SlotIceBunker extends Slot {

	public SlotIceBunker(IInventory inventory, int i, int j, int k) {
		super(inventory, i, j, k);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if(Block.getBlockFromItem(itemStack.getItem()) == ModManager.IceBlock
		|| Block.getBlockFromItem(itemStack.getItem()) == Blocks.snow) {
			
			return true;
		}
		return false;
	}
}
