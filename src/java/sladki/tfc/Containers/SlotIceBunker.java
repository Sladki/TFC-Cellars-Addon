package sladki.tfc.Containers;

import sladki.tfc.ModManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.TFCBlocks;

public class SlotIceBunker extends Slot {

	public SlotIceBunker(IInventory inventory, int i, int j, int k) {
		super(inventory, i, j, k);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if(Block.getBlockFromItem(itemStack.getItem()) == ModManager.IceBlock) {
			return true;
		}
		return false;
	}
}
