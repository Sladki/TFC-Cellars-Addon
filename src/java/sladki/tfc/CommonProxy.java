package sladki.tfc;

import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void registerGuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Cellars.instance, new sladki.tfc.Gui.GuiHandler());
	}
	
	public void registerRenderInformation()	{
	}

	public boolean isRemote() {
		return false;
	}
	
	public void hideItemsNEI() {	
	}
	
}