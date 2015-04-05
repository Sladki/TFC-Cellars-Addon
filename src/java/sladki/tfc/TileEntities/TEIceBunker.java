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
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;
import sladki.tfc.ModManager;
import sladki.tfc.Blocks.BlockCellarDoor;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Time;

public class TEIceBunker extends TileEntity implements IInventory {

	//NBT
	private ItemStack[] inventory = null;
	private int coolantAmount = 0;
	private int lastUpdate = 0;	
	
	private int[] entrance = new int[4];	//x, z of the first door + offsetX, offsetZ of the second door
	private int[] oldSize = new int[4];		//need in order to update containers if a cellar has become not complete
	private int[] size = new int[4];		//internal size, +z -x -z + x
	private boolean isComplete = false;
	private float temperature = -1;
	private int updateTickCounter = 1200;
	private int currentRow;
	private boolean updatingContainers;
	
	
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
			player.addChatMessage(new ChatComponentText("The cellar is not complete or is not chilled yet"));
		}
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
			return;
		}
		
		//If cellar change its size we have to update containers first, before we check its compliance and change the size
		//Updating containers row by row to decrease freezes
		if(updatingContainers) {
			if((isComplete && currentRow == size[0] + 1) || (!isComplete && currentRow == oldSize[0] + 1)) {
				updatingContainers = false;
			} else {
				for(int y = 1; y <=2; y++) {
					if(isComplete) {
						for(int x = -size[1]; x <= size[3]; x++) {
							updateContainer(x, y, currentRow);
						}
					} else {
						for(int x = -oldSize[1]; x <= oldSize[3]; x++) {
							updateContainer(x, y, currentRow);
						}
					}
				}
				currentRow++;
			}
			return;
		} else {
			//Check cellar compliance once per 1200 ticks, check coolant once per 100 ticks
			if(updateTickCounter % 100 == 0) {
				boolean changed = false;
				
				if(updateTickCounter >= 1200) {
					changed = updateCellar(true);
					updateTickCounter = 0;
				} else {
					changed = updateCellar(false);
				}
				
				if(changed) {
					updatingContainers = true;
					if(isComplete) {
						currentRow = -size[2];
					} else {
						currentRow = -oldSize[2];
					}
				}
			}
		}
		updateTickCounter++;
	}
	
	private boolean updateCellar(boolean checkCompliance) {
		
		boolean wasComplete = isComplete;
		float oldTemperature = temperature;
		temperature = 5;
		
		if(checkCompliance) {
			isComplete = isStructureComplete();
		}

		if(isComplete) {	
			float outsideTemp = TFC_Climate.getHeightAdjustedTemp(this.worldObj, xCoord, yCoord + 1, zCoord);
			if(coolantAmount <= 0) {
				for(int slot = 3; slot >= 0; slot--) {
					if(inventory[slot] != null) {
						if(Block.getBlockFromItem(inventory[slot].getItem()) == ModManager.IceBlock) {
							lastUpdate = TFC_Time.getTotalDays();
							coolantAmount = coolantAmount + 120;
							decrStackSize(slot, 1);
							break;
						}
					}
				}
			}
			if(coolantAmount > 0) {
				temperature = 1;
				if(lastUpdate < TFC_Time.getTotalDays()) {
					if(outsideTemp > -10) {	//magic
						int volume = (size[1] + size[3] + 1) * (size[0] + size[2] + 1);
						coolantAmount = coolantAmount - (int)(0.1 * volume * outsideTemp + volume + 2);
					}
					lastUpdate = lastUpdate++;
				}
			}
			temperature = temperature + doorsLoss();
			if(temperature > outsideTemp) {
				temperature = outsideTemp;
			}
		}
		
		if(wasComplete == isComplete && oldTemperature == temperature) {
			return false;
		}
		return true;
	}
	
	private int doorsLoss() {
		int loss = 0;
		
		//1st door
		Block door = this.worldObj.getBlock(
				xCoord + entrance[0], yCoord + 1, zCoord + entrance[1]);
		if(door == ModManager.CellarDoorBlock && !((BlockCellarDoor)door).isDoorOpen(this.worldObj,
				xCoord + entrance[0], yCoord + 1, zCoord + entrance[1])) {	
		
		} else {
			loss = 1;
		}
		
		//2nd door
		door = this.worldObj.getBlock(xCoord + entrance[0] + entrance[2], yCoord + 1, zCoord + entrance[1] + entrance[3]);
		if(door == ModManager.CellarDoorBlock && !((BlockCellarDoor)door).isDoorOpen(this.worldObj,
				xCoord + entrance[0] + entrance[2], yCoord + 1, zCoord + entrance[1] + entrance[3])) {
		
		} else {
			if(loss > 0) {
				loss = 4;
			} else {
				loss = 1;
			}
		}
		return loss;
	}
	
	private boolean isStructureComplete() {	
		oldSize[1] = size[1]; oldSize[3] = size[3];
		oldSize[2] = size[2]; oldSize[0] = size[0];
		
		entrance[0] = 0; entrance[1] = 0;
		entrance[2] = 0; entrance[3] = 0;
		int blockType = -1;
		
		//get size
		for(int direction = 0; direction < 4; direction++) {
			for(int distance = 1; distance < 6; distance++) {
				//max distance between an ice bunker and a wall is 3
				if(distance == 5) { return false;	} 
				
				if(direction == 1) {			blockType = getBlockType(-distance, 1, 0); }
				else if(direction == 3) {	blockType = getBlockType(distance, 1, 0); }
				else if(direction == 2) {	blockType = getBlockType(0, 1, -distance); }
				else if(direction == 0) {	blockType = getBlockType(0, 1, distance); }
				
				if(blockType == 0 || blockType == 1) {
					size[direction] = distance - 1;
					break;
				}
				
				if(blockType == -1) {
					return false;
				}
			}	
		}
		
		//check blocks and set entrance
		for(int y = 0; y < 4; y++) {
			for(int x = -size[1] - 1; x <= size[3] + 1; x++) {
				for(int z = -size[2] - 1; z <= size[0] + 1; z++) {
					
					blockType = getBlockType(x, y, z);
					
					//Ice bunker
					if(y == 0 && x == 0 && z == 0) {
						continue;
					}
					
					//Blocks inside the cellar
					if(y == 1 || y == 2) {
						if(x >= -size[1] && x <= size[3]) {
							if(z >= -size[2] && z <= size[0]) {
								if(blockType == 2) {
									continue;
								}
								return false;
							}
						}
					}
					
					//Corners
					if((x == -size[1] - 1 || x == size[3] + 1) && (z == -size[2] - 1 || z == size[0] + 1)) {
						if(blockType == 0) {
							continue;
						}
						return false;
					}
					
					//Doors
					if(blockType == 1) {
						//upper part of the door
						if(entrance[0] == x && entrance[1] == z) {
							continue;
						}
						
						//1 entrance only!
						if(entrance[0] == 0 && entrance[1] == 0) {
							entrance[0] = x; entrance[1] = z;
							if(x == -size[1] - 1) {
								entrance[2] = -1;
							} else if(x == size[3] + 1) {
								entrance[2] = 1;
							} else if(z == -size[2] - 1) {
								entrance[3] = -1;
							} else if(z == size[0] + 1) {
								entrance[3] = 1;
							}
							
							continue;
						}
						return false;
					}
					
					//Walls
					if(blockType == 0) {
						continue;
					}
					return false;
				}
			}
		}
		
		if(entrance[0] == 0 && entrance[1] == 0) {
			System.out.println("No doors");
			return false;
		}
		
		//check the entrance
		for(int y = 0; y < 4; y++) {
			for(int x = -MathHelper.abs_int(entrance[3]); x <= MathHelper.abs_int(entrance[3]); x++) {
				for(int z = -MathHelper.abs_int(entrance[2]); z <= MathHelper.abs_int(entrance[2]); z++ ) {
					
					blockType = getBlockType(x + entrance[0] + entrance[2], y, z + entrance[1] + entrance[3]);
				
					if(y == 1 || y == 2) {
						if(x == 0 && z == 0) {
							if(blockType == 1) {
								continue;
							}
							return false;
						}
					}
					if(blockType == 0) {
						continue;
					}
					
					return false;
				}
			}
		}		
		return true;
	}
	
	private int getBlockType(int x, int y, int z) {
		Block block = this.getWorldObj().getBlock(xCoord + x, yCoord + y, zCoord + z);
		if(block == ModManager.CellarWallBlock) {
			return 0;
		} else if(block == ModManager.CellarDoorBlock) {
			return 1;
		} else if(block == ModManager.CellarShelfBlock || block == TFCBlocks.Barrel || block == Blocks.wall_sign || block == Blocks.standing_sign ||
				block.isAir(this.worldObj, x, y, z)) {
			return 2;
		}
		//System.out.println(x + " " + y + " " + z + " " + block.getUnlocalizedName());
		return -1;
	}
	
	private void updateContainer(int x, int y, int z) {
		Block block = this.getWorldObj().getBlock(xCoord + x, yCoord + y, zCoord + z);
		if(block == ModManager.CellarShelfBlock) {
			TileEntity tileEntity = this.worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
			if(tileEntity != null) {
				((TECellarShelf) tileEntity).updateShelf(isComplete, temperature);
			}
		}
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
