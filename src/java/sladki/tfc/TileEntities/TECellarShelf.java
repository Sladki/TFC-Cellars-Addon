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
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.Food;
import com.bioxx.tfc.api.TFCOptions;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Interfaces.IFood;

public class TECellarShelf extends TileEntity implements IInventory {

	private ItemStack[] inventory;
	
	private int cellarTick = -240;	//Because a bunker may be not in the same chunk
	private float temperature = -1;
	
	private int updateTickCounter = 120;
	
	
	
	public TECellarShelf() {
		inventory = new ItemStack[getSizeInventory()];
	}	
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
			return;
		}
			

		if(updateTickCounter % 5 == 0) {
			handleItemTicking();
		}
		
		updateTickCounter++;
	}
	
	private void handleItemTicking() {
		float envDecay = 1f;
		
		if(cellarTick >= 0) {
			if(cellarTick > 0) {
				envDecay = TFC_Core.getEnvironmentalDecay(temperature);
				cellarTick--;
				
				//Syncing
				if(cellarTick == 0) {
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}

			TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, envDecay);
		} else {
			cellarTick++;
			
			//Syncing syncing
			if(cellarTick == 0) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			
			return;			
		}
	}
	
	public void updateShelf(float temp) {
		cellarTick = 100;
		temperature	= temp;
		
		//Syncing syncing diving diving
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public float getTemperature() {
		return temperature;
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
	
	private void writeSyncData(NBTTagCompound tagCompound) {
		float temp = (cellarTick <= 0) ? -1000 : temperature;
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
		writeSyncData(tagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
		readSyncData(packet.func_148857_g());
	}
	
}