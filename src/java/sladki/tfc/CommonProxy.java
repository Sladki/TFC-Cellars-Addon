package sladki.tfc;

import net.minecraftforge.common.MinecraftForge;
import sladki.tfc.Handlers.ChunkEventHandler;

import com.bioxx.tfc.Handlers.Client.ClientTickHandler;

import cpw.mods.fml.common.FMLCommonHandler;
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