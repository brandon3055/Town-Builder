package com.brandon3055.townbuilder.network;

import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.commands.CommandCreate;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by Brandon on 26/02/2015.
 */
public class PacketSchematicClient  implements IMessage {

	public String fileName;

	public PacketSchematicClient() {}

	public PacketSchematicClient(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		fileName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, fileName);
	}

	public static class Handler implements IMessageHandler<PacketSchematicClient, IMessage> {
		@Override
		public IMessage onMessage(PacketSchematicClient message, MessageContext ctx) {
			CommandCreate.instance.handleCommand(TownBuilder.proxy.getClientPlayer(), new String[] {"create", message.fileName});
			return null;
		}
	}
}
