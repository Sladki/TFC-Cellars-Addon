package sladki.tfc.TileEntities;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import sladki.tfc.ModManager;
import sladki.tfc.Blocks.BlockCellarDoor;
import sladki.tfc.Blocks.BlockCellarWall;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.TFC_Time;

public class TEIceBunker extends TileEntity implements IInventory {

	private ItemStack[] inventory;
	private int direction;
	
	private boolean isComplete = false;
	private int coolantAmount = 0;
	private int temperature = -1;
	
	private int updateTickCounter = 1200;
	private int lastUpdate = 0;	
	
	public TEIceBunker() {
		inventory = new ItemStack[getSizeInventory()];
	}
	
	public void getCellarInfo(EntityPlayer player) {
		/*if(isComplete) {
			player.addChatMessage(new ChatComponentText("Cellar is complete and have temperature " + temperature + "\u00b0C inside"));
			player.addChatMessage(new ChatComponentText(""+coolantAmount));
		} else {
			player.addChatMessage(new ChatComponentText("Cellar is not complete or not cooled enough"));
		}*/
		if(isComplete) {
			if(temperature <  4) {
				player.addChatMessage(new ChatComponentText("It is freezing here"));
			} else {
				player.addChatMessage(new ChatComponentText("The cellar is chilly"));
			}
		} else {
			player.addChatMessage(new ChatComponentText("The cellar is not complete"));
		}
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
			return;
		}

		if(updateTickCounter >= 1200) {
			updateCellar();
			updateTickCounter = 0;
		}
		updateTickCounter++;
	}
	
	private void updateCellar() {
		temperature = 5;
		isComplete = isStructureComplete();

		if(isComplete) {			
			if(coolantAmount == 0) {
				for(int slot = 3; slot >= 0; slot--) {
					if(inventory[slot] != null) {
						if(Block.getBlockFromItem(inventory[slot].getItem()) == Blocks.snow) {
							decrStackSize(slot, 1);
							coolantAmount = 3;
							break;
						}
						if(Block.getBlockFromItem(inventory[slot].getItem()) == ModManager.IceBlock) {
							decrStackSize(slot, 1);
							coolantAmount = 6;
							break;
						}
					}
				}
			}
			if(coolantAmount > 0) {
				temperature = 1;
				if(lastUpdate < TFC_Time.getTotalDays()) {
					coolantAmount--;
					lastUpdate = TFC_Time.getTotalDays();
				}
			}
			temperature = temperature + doorsLoss();
		}
	}
	
	public int getCellarTemperature() {
		if(temperature == -1) {
			//Force update
			updateCellar();
		}
		return temperature;
	}
	
	public boolean isCellarComplete() {
		return isComplete;
	}
	
	private int doorsLoss() {
		int loss = 0;
		
		if(direction == 0) {
			BlockCellarDoor door = (BlockCellarDoor) this.worldObj.getBlock(xCoord, yCoord + 1, zCoord + 3);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord, yCoord + 1, zCoord + 3)) {
				loss = 1;
			}
			
			door = (BlockCellarDoor) this.worldObj.getBlock(xCoord, yCoord + 1, zCoord + 4);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord, yCoord + 1, zCoord + 4)) {
				if(loss > 0) {
					loss = 4;
				} else {
					loss = 1;
				}
			}
			return loss;
		}
		
		if(direction == 2) {
			BlockCellarDoor door = (BlockCellarDoor) this.worldObj.getBlock(xCoord, yCoord + 1, zCoord - 3);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord, yCoord + 1, zCoord - 3)) {
				loss = 1;
			}
			
			door = (BlockCellarDoor) this.worldObj.getBlock(xCoord, yCoord + 1, zCoord - 4);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord, yCoord + 1, zCoord - 4)) {
				if(loss > 0) {
					loss = 4;
				} else {
					loss = 1;
				}
			}
			return loss;
		}
		
		if(direction == 1) {
			BlockCellarDoor door = (BlockCellarDoor) this.worldObj.getBlock(xCoord - 3, yCoord + 1, zCoord);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord - 3, yCoord + 1, zCoord)) {
				loss = 1;
			}
			
			door = (BlockCellarDoor) this.worldObj.getBlock(xCoord - 4, yCoord + 1, zCoord);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord - 4, yCoord + 1, zCoord)) {
				if(loss > 0) {
					loss = 4;
				} else {
					loss = 1;
				}
			}
			return loss;
		}
		
		if(direction == 3) {
			BlockCellarDoor door = (BlockCellarDoor) this.worldObj.getBlock(xCoord + 3, yCoord + 1, zCoord);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord + 3, yCoord + 1, zCoord)) {
				loss = 1;
			}
			
			door = (BlockCellarDoor) this.worldObj.getBlock(xCoord + 4, yCoord + 1, zCoord);
			if(door == ModManager.CellarDoorBlock && door.isDoorOpen(this.worldObj, xCoord + 4, yCoord + 1, zCoord)) {
				if(loss > 0) {
					loss = 4;
				} else {
					loss = 1;
				}
			}
			return loss;
		}
		
		return loss;
	}
	
	private boolean isStructureComplete() {
		
		//North
		//  1     2-3     4
		//#####  #####  #####
		//## ##  #   #  #####
		//#####  #   #  #####
		//#####  #   #  #####
		//#####  ## ##  #####
		// ###    # #    ###
		
		if(this.getWorldObj().getBlock(xCoord, yCoord + 1, zCoord - 1) instanceof BlockCellarWall) {
			//north
			direction = 0;
		} else if(this.getWorldObj().getBlock(xCoord, yCoord + 1, zCoord + 1) instanceof BlockCellarWall) {
			//south
			direction = 2;
		} else if(this.getWorldObj().getBlock(xCoord + 1, yCoord + 1, zCoord) instanceof BlockCellarWall) {
			//east
			direction = 1;
		} else if(this.getWorldObj().getBlock(xCoord - 1, yCoord + 1, zCoord) instanceof BlockCellarWall) {
			//west
			direction = 3;
		} else { 
			return false; 
		}
		
		if(direction == 0) {
			for(int level = 0; level <=3; level++) {
				for(int z = -1; z <= 4; z++) {
					for(int x = -2; x <= 2; x++) {
						
						if(level == 0) {
							if(x == 0 && z == 0) {
								continue;
							}
						} else if(level == 1 || level == 2) {
							if((x >= -1 && x <= 1) && (z >= 0 && z <= 2)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 0)) {
									continue;
								}
								return false;
							} else if(x == 0 && (z == 3 || z == 4)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 1)) {
									continue;
								}
								return false;
							}
						}	
						if((x == -2 || x == 2) && (z == 4)) {
							continue;
						}
						
						if(!(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z) instanceof BlockCellarWall)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		
		if(direction == 2) {
			for(int level = 0; level <=3; level++) {
				for(int z = -4; z <= 1; z++) {
					for(int x = -2; x <= 2; x++) {
						
						if(level == 0) {
							if(x == 0 && z == 0) {
								continue;
							}
						} else if(level == 1 || level == 2) {
							if((x >= -1 && x <= 1) && (z >= -2 && z <= 0)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 0)) {
									continue;
								}
								return false;
							} else if(x == 0 && (z == -3 || z == -4)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 1)) {
									continue;
								}
								return false;
							}
						}	
						if((x == -2 || x == 2) && (z == -4)) {
							continue;
						}
						
						if(!(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z) instanceof BlockCellarWall)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		
		if(direction == 1) {
			for(int level = 0; level <=3; level++) {
				for(int x = -4; x <= 1; x++) {
					for(int z = -2; z <= 2; z++) {
						
						if(level == 0) {
							if(x == 0 && z == 0) {
								continue;
							}
						} else if(level == 1 || level == 2) {
							if((z >= -1 && z <= 1) && (x >= -2 && x <= 0)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 0)) {
									continue;
								}
								return false;
							} else if(z == 0 && (x == -3 || x == -4)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 1)) {
									continue;
								}
								return false;
							}
						}	
						if((z == -2 || z == 2) && (x == -4)) {
							continue;
						}
						
						if(!(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z) instanceof BlockCellarWall)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		
		if(direction == 3) {
			for(int level = 0; level <=3; level++) {
				for(int x = -1; x <= 4; x++) {
					for(int z = -2; z <= 2; z++) {
						
						if(level == 0) {
							if(x == 0 && z == 0) {
								continue;
							}
						} else if(level == 1 || level == 2) {
							if((z >= -1 && z <= 1) && (x >= 0 && x <= 2)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 0)) {
									continue;
								}
								return false;
							} else if(z == 0 && (x == 3 || x == 4)) {
								if(isBlockSuitable(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z), 1)) {
									continue;
								}
								return false;
							}
						}	
						if((z == -2 || z == 2) && (x == 4)) {
							continue;
						}
						
						if(!(this.getWorldObj().getBlock(xCoord + x, yCoord + level, zCoord + z) instanceof BlockCellarWall)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		
		return false;
	}
	
	private boolean isBlockSuitable(Block block, int mode) {
		if(mode == 0) {
			if(block == Blocks.air || block == ModManager.CellarShelfBlock) {
				return true;
			}
		} else if(mode == 1) {
			if(block == ModManager.CellarDoorBlock) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return 4;
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
		return "Ice Bunker";
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
		//if(Block.getBlockFromItem(stack.getItem()) == TFCBlocks.Ice || Block.getBlockFromItem(stack.getItem()) == Blocks.snow) {
			return true;
		//}
		//return false;
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
        lastUpdate = tagCompound.getInteger("LastUpdate");
        coolantAmount = tagCompound.getInteger("CoolantAmount");
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
		tagCompound.setInteger("LastUpdate", lastUpdate);
		tagCompound.setInteger("CoolantAmount", coolantAmount);
		
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
