package sladki.tfc.Containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sladki.tfc.TileEntities.TECellarShelf;

import com.bioxx.tfc.Containers.Slots.SlotCookableFoodOnly;
import com.bioxx.tfc.Containers.Slots.SlotFoodOnly;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Enums.EnumSize;

public class ContainerCellarShelf extends Container {

	private TECellarShelf tileEntity;
	
	public ContainerCellarShelf(InventoryPlayer player, TECellarShelf tileEntity) {	
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 7; x++) {
				addSlotToContainer(new SlotCookableFoodOnly(tileEntity,
						x + (y * 7),		//Slot id
						26 + (x * 18),		//Slot x
						25 + (y * 18)		//Slot y
				).setSize(EnumSize.HUGE));
			}
		}

		bindPlayerInventory(player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();

                    if (slot < 14) {
                            if (!this.mergeItemStack(stackInSlot, 14, this.inventorySlots.size(), true)) {
                                    return null;
                            }
                    }
                    else if (!this.mergeItemStack(stackInSlot, 0, 14, false)) {
                            return null;
                    }

                    if (stackInSlot.stackSize == 0) {
                            slotObject.putStack(null);
                    } else {
                            slotObject.onSlotChanged();
                    }

                    if (stackInSlot.stackSize == stack.stackSize) {
                            return null;
                    }
                    
                    slotObject.onPickupFromSlot(player, stackInSlot);
            }
            
            return stack;
    }
	
	@Override
	public boolean mergeItemStack(ItemStack is, int slotStart, int slotFinish, boolean backward) {
		if(!backward) {
			if(this.getSlot(0).isItemValid(is)) {
				return super.mergeItemStack(is, slotStart, slotFinish, backward);
			}
			return false;
		}
		return super.mergeItemStack(is, slotStart, slotFinish, backward);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                        8 + j * 18, 84 + i * 18));
                }
        }

        for (int i = 0; i < 9; i++) {
                addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
	}

}