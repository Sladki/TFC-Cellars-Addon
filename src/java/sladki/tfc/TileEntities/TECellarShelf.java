package sladki.tfc.TileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.Constants;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.api.TFCOptions;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Interfaces.IFood;

public class TECellarShelf extends TileEntity implements IInventory {

	private ItemStack[] inventory;
	
	private boolean inCellar = false;
	private float temperature = 0;
	
	private int updateTickCounter = 0;
	
	public TECellarShelf() {
		inventory = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
			return;
		}
		
		if(updateTickCounter >= 4) {
			if(inCellar) {
				decayTick();
			} else {
				TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord);
			}
			updateTickCounter = 0;
		}
		
		updateTickCounter++;
	}
	
	public void updateShelf(boolean inCellar, float temp) {
		this.inCellar		= inCellar;
		this.temperature	= temp;
	}
	
	private void decayTick() {
		for(int i = 0; i < 14; i++) {
			ItemStack itemStack = inventory[i];
			if( itemStack != null) {
				if(itemStack.getItem() instanceof IFood) {
					
					NBTTagCompound tag = itemStack.getTagCompound();
					if(tag != null &&(tag.hasKey("foodWeight") && tag.hasKey("foodDecay"))) {
						
						if(tag.getInteger("decayTimer") < TFC_Time.getTotalHours()) {
							
							int timeDelta = (int) (TFC_Time.getTotalHours() - tag.getInteger("decayTimer"));
							float protMult = 1;
							
							if(TFCOptions.useDecayProtection) {
								if(timeDelta > TFCOptions.decayProtectionDays * 24) {
									tag.setInteger("decayTimer", (int) TFC_Time.getTotalHours() - 24);
								} else if(timeDelta > 24) {
									protMult = 1 - (timeDelta / (TFCOptions.decayProtectionDays * 24));
								}
							}
							
							float currentDecay = tag.getFloat("foodDecay");
							float enviromentalDecay = 0;
							float decayRate = 1.0f;
							
							float weight = tag.getFloat("foodWeight");
							
							if(temperature > 0) {
								enviromentalDecay = 2 * (1.0f - (15.0f / (15.0f + temperature)));
							}
							
							if(tag.hasKey("decayRate")) {
								decayRate = tag.getFloat("decayRate");
							} else {
								decayRate = ((IFood) itemStack.getItem()).getDecayRate(itemStack);
							}
							
							if(currentDecay < 0) {
								float decayIncrement = decayRate * enviromentalDecay;
								if(currentDecay + decayIncrement < 0) {
									currentDecay = currentDecay + decayIncrement;
								} else {
									currentDecay = 0;
								}
							} else if(currentDecay == 0) {
								currentDecay = (tag.getFloat("foodWeight") * 0.0025f) * TFCOptions.decayMultiplier;
							} else {
								double foodDecayRate = Global.FOOD_DECAY_RATE - 1;
								foodDecayRate = foodDecayRate * (decayRate * enviromentalDecay * protMult * TFCOptions.decayMultiplier);
								currentDecay = (float) (currentDecay * (foodDecayRate + 1));
							}
							tag.setInteger("decayTimer", (tag.getInteger("decayTimer") + 1));
							tag.setFloat("foodDecay", currentDecay);
						}
						
						if(tag.getFloat("foodDecay") / tag.getFloat("foodWeight") > 0.9f) {
							inventory[i] = null;
						} else {
							inventory[i].setTagCompound(tag);
						}
					}		
				}
			}
		}
	}
	
	@Override
	public int getSizeInventory() {
		return 14;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = inventory[slot];
		if(stack != null) {
			if(stack.stackSize <= amount) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amount);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if(stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "Cellar Shelf";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		NBTTagList tagList = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tag = tagList.getCompoundTagAt(i);
                byte slot = tag.getByte("Slot");
                if (slot >= 0 && slot < getSizeInventory()) {
                        inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
                }
        }
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		NBTTagList tagList = new NBTTagList();
		for(int i = 0; i < getSizeInventory(); i++) {
			if(inventory[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				inventory[i].writeToNBT(tag);
				tagList.appendTag(tag);
			}
		}
		tagCompound.setTag("Items", tagList);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
	
}