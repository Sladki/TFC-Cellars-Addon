package sladki.tfc.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.world.World;
import sladki.tfc.ModManager;

/**
 * Created by raymondbh on 11.05.2015.
 * This code is heavily inspired by Aleksey-Terzi. All credits to him.
 * https://github.com/Aleksey-Terzi/MerchantsTFC
 */

public class ServerTickHandler {

    @SubscribeEvent
    public void onServerWorldTick(WorldTickEvent event)
        {
        World world = event.world;

        if(event.phase == Phase.START)
        {
            if(world.provider.dimensionId == 0)
            {
                ModManager.registerAnvilRecipes();
            }
        }
    }
}
