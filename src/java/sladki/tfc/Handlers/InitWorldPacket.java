package sladki.tfc.Handlers;

import com.bioxx.tfc.Handlers.Network.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import sladki.tfc.ModManager;

/**
 * Created by raymondbh on 11.05.2015.
  * This code is heavily inspired by Aleksey-Terzi. All credits to him.
 * https://github.com/Aleksey-Terzi/MerchantsTFC
 */
public class InitWorldPacket extends AbstractPacket {

    @Override
    public void handleClientSide(EntityPlayer player) {
        ModManager.registerAnvilRecipes();
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){

    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){

    }
}
