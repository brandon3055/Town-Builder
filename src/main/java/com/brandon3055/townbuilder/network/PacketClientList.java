package com.brandon3055.townbuilder.network;

import com.brandon3055.brandonscore.network.MessageHandlerWrapper;
import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.commands.CommandList;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Brandon on 25/02/2015.
 */
public class PacketClientList implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler extends MessageHandlerWrapper<PacketClientList, IMessage> {
        @Override
        public IMessage handleMessage(PacketClientList message, MessageContext ctx) {
            CommandList.instance.handleCommand(TownBuilder.proxy.getClientPlayer(), null);
            return null;
        }
    }
}
