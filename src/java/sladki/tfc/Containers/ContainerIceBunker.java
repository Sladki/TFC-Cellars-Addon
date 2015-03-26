package sladki.tfc.Containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sladki.tfc.TileEntities.TEIceBunker;

public class ContainerIceBunker extends Container {

	private TEIceBunker tileEntity;
	
	public ContainerIceBunker(InventoryPlayer player, TEIceBunker tileEntity) {
		addSlotToContainer(new SlotIceBunker(tileEntity, 0, 71, 25));
		addSlotToContainer(new SlotIceBunker(tileEntity, 1, 89, 25));
		addSlotToContainer(new SlotIceBunker(tileEntity, 2, 71, 43));
		addSlotToContainer(new SlotIceBunker(tileEntity, 3, 89, 43));
		
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

                    if (slot < 4) {
                            if (!this.mergeItemStack(stackInSlot, 4, this.inventorySlots.size(), true)) {
                                    return null;
                            }
                    }
                    else if (!this.mergeItemStack(stackInSlot, 0, 4, false)) {
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
