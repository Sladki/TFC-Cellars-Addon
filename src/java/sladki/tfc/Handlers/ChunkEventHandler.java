package sladki.tfc.Handlers;

import net.minecraftforge.event.world.WorldEvent;
import sladki.tfc.ModManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChunkEventHandler {

	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load event) {
		if(!event.world.isRemote && event.world.provider.dimensionId == 0) {
			ModManager.registerAnvilRecipes();
		}
	}
	
}