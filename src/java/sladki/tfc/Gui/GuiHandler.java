package sladki.tfc.Gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sladki.tfc.Containers.ContainerCellarShelf;
import sladki.tfc.Containers.ContainerIceBunker;
import sladki.tfc.TileEntities.TECellarShelf;
import sladki.tfc.TileEntities.TEIceBunker;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(ID == 0) {
			return new ContainerIceBunker(player.inventory, (TEIceBunker) tileEntity);
		} else if(ID == 1) {
			return new ContainerCellarShelf(player.inventory, (TECellarShelf) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(ID == 0) {
			return new GuiIceBunker(player.inventory, (TEIceBunker) tileEntity);
		} else if(ID == 1) {
			return new GuiCellarShelf(player.inventory, (TECellarShelf) tileEntity);
		}
		return null;
	}	
	
}
