package sladki.tfc.Handlers;

import com.bioxx.tfc.Handlers.Network.AbstractPacket;
import com.bioxx.tfc.TerraFirmaCraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.item.ItemTossEvent;

/**
 * Created by raymondbh on 11.05.2015.
 * This code is heavily inspired by Aleksey-Terzi. All credits to him.
 * https://github.com/Aleksey-Terzi/MerchantsTFC
 */
public class PlayerTracker
{
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        AbstractPacket pkt = new InitWorldPacket();
        TerraFirmaCraft.packetPipeline.sendTo(pkt, (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ServerDisconnectionFromClientEvent event)
    {
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
    }

    @SubscribeEvent
    public void notifyPickup(PlayerEvent.ItemPickupEvent event)
    {
    }

    // Register the Player Toss Event Handler, workaround for a crash fix
    @SubscribeEvent
    public void onPlayerTossEvent(ItemTossEvent event)
    {
    }
}
