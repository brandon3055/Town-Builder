package com.brandon3055.townbuilder.network;

import com.brandon3055.townbuilder.schematics.FileHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by Brandon on 2/03/2015.
 */
public class PacketByteStream  implements IMessage {

	public short packetindex;
	public short packetCount;
	public byte[] bytes;

	public PacketByteStream(){}

	public PacketByteStream(byte[] bytes, int count, int index)
	{
		this.bytes = bytes;
		this.packetCount = (short)count;
		this.packetindex = (short)index;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		packetCount = buf.readShort();
		packetindex = buf.readShort();
		bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(packetCount);
		buf.writeShort(packetindex);
		buf.writeBytes(bytes);
	}

	public static class Handler implements IMessageHandler<PacketByteStream, IMessage>
	{
		@Override
		public IMessage onMessage(PacketByteStream message, MessageContext ctx)
		{
			FileHandler.instance.receiveFile(message, ctx);
			return null;
		}
	}
}

