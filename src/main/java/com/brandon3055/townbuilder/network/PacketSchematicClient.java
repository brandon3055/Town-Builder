package com.brandon3055.townbuilder.network;

import com.brandon3055.brandonscore.network.MessageHandlerWrapper;
import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.commands.CommandCreate;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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

	public static class Handler extends MessageHandlerWrapper<PacketSchematicClient, IMessage> {
		@Override
		public IMessage handleMessage(PacketSchematicClient message, MessageContext ctx) {
			CommandCreate.instance.handleCommand(TownBuilder.proxy.getClientPlayer(), new String[] {"create", message.fileName});
			return null;
		}
	}
}
