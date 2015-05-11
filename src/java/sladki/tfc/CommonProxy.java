package sladki.tfc;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import sladki.tfc.Handlers.ServerTickHandler;


public class CommonProxy {

	public void registerGuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Cellars.instance, new sladki.tfc.Gui.GuiHandler());
	}
	
	public void registerRenderInformation()	{
	}

	public void registerTickHandler()
	{
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
	}

	public boolean isRemote() {
		return false;
	}
	
	public void hideItemsNEI() {	
	}
	
}