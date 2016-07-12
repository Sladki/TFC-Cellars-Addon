package sladki.tfc.TileEntities;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.api.Food;
import com.bioxx.tfc.api.TFCBlocks;

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
import sladki.tfc.ModConfig;
import sladki.tfc.ModManager;
import sladki.tfc.Blocks.BlockCellarDoor;

public class TEIceBunker extends TileEntity implements IInventory {

	//NBT
	private ItemStack[] inventory = null;
	private int coolantAmount = 0;
	private int lastUpdate = 0;	
	
	private int[] entrance = new int[4];	//x, z of the first door + offsetX, offsetZ of the second door
	private int[] size = new int[4];		//internal size, +z -x -z + x
	private boolean isComplete = false;
	private boolean hasAirlock = false;
	
	private float avgYearTemp = Float.MIN_VALUE;
	private float lossMult = -1f;
	
	private float temperature = -1;	//-1000 cellar is not complete
	private byte error = 0;
	
	private int updateTickCounter = 1200;
	
	
	
	public TEIceBunker() {
		inventory = new ItemStack[getSizeInventory()];
	}
	
	public void getCellarInfo(EntityPlayer player) {
		if(ModConfig.isDebugging) {
			player.addChatMessage(new ChatComponentText("Temperature: " + temperature + " Coolant: " + coolantAmount));
			player.addChatMessage(new ChatComponentText("Check console for more information"));
			updateCellar(true);
			return;
		}
		
		if(isComplete) {
			if(temperature < 0) {
				player.addChatMessage(new ChatComponentText("It is icy here"));
			} else if(temperature < 5) {
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
			
		//Check cellar compliance once per 1200 ticks, check coolant and update containers once per 100 ticks
		if(updateTickCounter % 100 == 0) {
			
			if(updateTickCounter >= 1200) {
				updateCellar(true);
				updateTickCounter = 0;
			} else {
				updateCellar(false);
			}
			
			updateContainers();
		}
		updateTickCounter++;
	}
	
	private void updateCellar(boolean checkCompliance) {
		if(avgYearTemp == Float.MIN_VALUE) {
			for(int month = 0; month < 12; month++) {
				avgYearTemp += TFC_Climate.getHeightAdjustedTempSpecificDay(worldObj, month * TFC_Time.daysInMonth, xCoord, yCoord, zCoord);
			}
			avgYearTemp = avgYearTemp * 0.015f;	//Magic! (divide by 12 (average) * 0.18)
		}
		temperature = avgYearTemp;
		
		if(checkCompliance) {
			isComplete = isStructureComplete();
		}

		if(isComplete) {	
			float outsideTemp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord + 1, zCoord);
			if(coolantAmount <= 0) {
				for(int slot = 3; slot >= 0; slot--) {
					if(inventory[slot] != null) {
						if(Block.getBlockFromItem(inventory[slot].getItem()) == ModManager.IceBlock) {
							coolantAmount = coolantAmount + 120;
						} else if(Block.getBlockFromItem(inventory[slot].getItem()) == Blocks.snow) {
							coolantAmount = coolantAmount + 40;
						}
						lastUpdate = TFC_Time.getTotalDays();
						decrStackSize(slot, 1);
						temperature = ModConfig.iceHouseTemperature;
						break;
					}
				}
			}
			
			if(coolantAmount > 0) {
				temperature = ModConfig.iceHouseTemperature;
				if(lastUpdate < TFC_Time.getTotalDays()) {
					if(outsideTemp > -10) {	//magic
						int volume = (size[1] + size[3] + 1) * (size[0] + size[2] + 1);
						coolantAmount = coolantAmount - (int)(ModConfig.coolantConsumptionMultiplier * (0.05 * volume * (1 + lossMult) * (outsideTemp + volume + 2)));
					}
					lastUpdate++;
				}
			}
			
			float doorsLossMult = doorsLossMult();
			if(lossMult == -1) {
				lossMult = doorsLossMult;
			}
			
			if(lossMult != doorsLossMult) {
				if(lossMult > doorsLossMult) {
					lossMult = (lossMult - 0.01f) * 0.75f;
					lossMult = Math.max(doorsLossMult, lossMult);
				} else {
					lossMult = (lossMult + 0.01f) * 1.15f;	//0.05f because lossMult can to be 0
					lossMult = Math.min(doorsLossMult, lossMult);
				}
			}
			
			temperature = temperature + lossMult * outsideTemp;
			if(temperature > outsideTemp) {
				temperature = outsideTemp;
			}
		}
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	private float doorsLossMult() {
		float loss = 0;
		
		//1st door
		Block door = worldObj.getBlock(
				xCoord + entrance[0], yCoord + 1, zCoord + entrance[1]);
		if(door == ModManager.CellarDoorBlock && ((BlockCellarDoor)door).isDoorOpen(worldObj,
				xCoord + entrance[0], yCoord + 1, zCoord + entrance[1])) {	
		
			loss = 0.05f;
		}
		
		//2nd door
		//Does it even exist?
		if(!hasAirlock) {
			return loss * 8 + 0.3f;
		}
		
		door = worldObj.getBlock(xCoord + entrance[0] + entrance[2], yCoord + 1, zCoord + entrance[1] + entrance[3]);
		if(door == ModManager.CellarDoorBlock && ((BlockCellarDoor)door).isDoorOpen(worldObj,
				xCoord + entrance[0] + entrance[2], yCoord + 1, zCoord + entrance[1] + entrance[3])) {
		
			return loss * 13 + 0.05f;
		} 
		
		return loss;
	}
	
	private boolean isStructureComplete() {	
		entrance[0] = 0; entrance[1] = 0;
		entrance[2] = 0; entrance[3] = 0;
		
		hasAirlock = false;
		error = 0;
		
		int blockType = -1;
		
		//get size
		for(int direction = 0; direction < 4; direction++) {
			for(int distance = 1; distance < 6; distance++) {
				//max distance between an ice bunker and a wall is 3
				if(distance == 5) { 
					if(ModConfig.isDebugging) {
						System.out.println("Cellar at " + xCoord + " " + yCoord + " " + zCoord
								+ " can't find a wall on " + direction + " side");
					}
					error = 1;
					return false;
				} 
				
				if(direction == 1) {			blockType = getBlockType(-distance, 1, 0); }
				else if(direction == 3) {	blockType = getBlockType(distance, 1, 0); }
				else if(direction == 2) {	blockType = getBlockType(0, 1, -distance); }
				else if(direction == 0) {	blockType = getBlockType(0, 1, distance); }
				
				if(blockType == 0 || blockType == 1) {
					size[direction] = distance - 1;
					break;
				}
				
				if(blockType == -1) {
					error = 2;
					return false;
				}
			}	
		}
		
		//check blocks and set entrance
		for(int y = 0; y < 4; y++) {
			for(int x = -size[1] - 1; x <= size[3] + 1; x++) {
				for(int z = -size[2] - 1; z <= size[0] + 1; z++) {
					
					//Ice bunker
					if(y == 0 && x == 0 && z == 0) {
						continue;
					}
					
					blockType = getBlockType(x, y, z);
					
					//Blocks inside the cellar
					if(y == 1 || y == 2) {
						if(x >= -size[1] && x <= size[3]) {
							if(z >= -size[2] && z <= size[0]) {
								if(blockType == 2) {
									continue;
								}
								error = 2;
								return false;
							}
						}
					}
					
					//Corners
					if((x == -size[1] - 1 || x == size[3] + 1) && (z == -size[2] - 1 || z == size[0] + 1)) {
						if(blockType == 0) {
							continue;
						}
						error = 1;
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
						
						if(ModConfig.isDebugging) {
							System.out.println("Cellar at " + xCoord + " " + yCoord + " " + zCoord
									+ " has too many doors");
						}
						error = 3;
						return false;
					}
					
					//Walls
					if(blockType == 0) {
						continue;
					}
					error = 1;
					return false;
				}
			}
		}
		
		if(entrance[0] == 0 && entrance[1] == 0) {
			if(ModConfig.isDebugging) {
				System.out.println("Cellar at " + xCoord + " " + yCoord + " " + zCoord
						+ " has no doors");
			}
			error = 3;
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
								hasAirlock = true;
								continue;
							}
							
							hasAirlock = false;
							if(ModConfig.isDebugging) {
								System.out.println("Cellar at " + xCoord + " " + yCoord + " " + zCoord
										+ " doesn't has the second door, block there is " + blockType);
							}
						}
					}
					if(blockType == 0) {
						continue;
					}
					
					hasAirlock = false;
					if(ModConfig.isDebugging) {
						System.out.println("Doors at the cellar at " + xCoord + " " + yCoord + " " + zCoord
								+ " doesn't surrounded by wall, block there is " + blockType);
					}
				}
			}
		}	
		
		if(ModConfig.isDebugging) {
			System.out.println("Cellar at " + xCoord + " " + yCoord + " " + zCoord + " is complete");
		}
		
		return true;
	}
	
	private int getBlockType(int x, int y, int z) {
		Block block = getWorldObj().getBlock(xCoord + x, yCoord + y, zCoord + z);
		if(block == ModManager.CellarWallBlock) {
			return 0;
		} else if(block == ModManager.CellarDoorBlock) {
			return 1;
		} else if(block == ModManager.CellarShelfBlock || block == TFCBlocks.barrel || block == Blocks.wall_sign || block == Blocks.standing_sign ||
				block.isAir(worldObj, x, y, z)) {
			return 2;
		}
		
		if(ModConfig.isDebugging) {
			System.out.println("Incorrect cellar block at " + x + " " + y + " " + z + " " + block.getUnlocalizedName());
		}
		
		return -1;
	}
	
	public void updateContainers() {
		if(!isComplete) {
			return;
		}
		
		float envDecay = TFC_Core.getEnvironmentalDecay(temperature);
		for(int y = 1; y <=2; y++) {
			for(int z = -size[2]; z <= size[0]; z++) {
				for(int x = -size[1]; x <= size[3]; x++) {
					updateContainer(x, y, z, envDecay);
				}
			}
		}
	}
	
	private void updateContainer(int x, int y, int z, float envDecay) {
		Block block = getWorldObj().getBlock(xCoord + x, yCoord + y, zCoord + z);
		if(block == ModManager.CellarShelfBlock) {
			TileEntity tileEntity = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
			if(tileEntity != null) {
				((TECellarShelf) tileEntity).updateShelf(temperature);
			}
			
			return;
		}
	}
	
	public float getTemperature() {
		return temperature;
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
	
	private void writeSyncData(NBTTagCompound tagCompound) {
		float temp = (error == 0) ? temperature : -error * 1000;
		tagCompound.setFloat("Temperature", temp);
	}
	
	private void readSyncData(NBTTagCompound tagCompound) {
		temperature = tagCompound.getFloat("Temperature");
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
		writeSyncData(tagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
		readSyncData(packet.func_148857_g());
	}

}
