package com.brandon3055.townbuilder.network;

import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.commands.CommandList;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by Brandon on 25/02/2015.
 */
public class PacketClientList implements IMessage {
	@Override
	public void fromBytes(ByteBuf buf)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{

	}

	public static class Handler implements IMessageHandler<PacketClientList, IMessage>
	{
		@Override
		public IMessage onMessage(PacketClientList message, MessageContext ctx) {
			CommandList.instance.handleCommand(TownBuilder.proxy.getClientPlayer(), null);
			return null;
		}
	}
}
