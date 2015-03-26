package sladki.tfc;

import sladki.tfc.TileEntities.TECellarShelf;
import sladki.tfc.TileEntities.TEIceBunker;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void init() {
		GameRegistry.registerTileEntity(TEIceBunker.class, "TEIceBunker");
		GameRegistry.registerTileEntity(TECellarShelf.class, "TECellarShelf");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Cellars.instance, new sladki.tfc.Gui.GuiHandler());
	}
	
	public void registerRenderInformation()	{
	}

	public boolean isRemote() {
		return false;
	}
	
	public void tweakNEI() {
		
	}
	
}