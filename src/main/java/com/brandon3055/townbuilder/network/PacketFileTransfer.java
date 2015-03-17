package com.brandon3055.townbuilder.network;

import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.FileHandler;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Brandon on 25/02/2015.
 */
public class PacketFileTransfer implements IMessage {
	public String fileName;
	public boolean transferValid;
	public int port;

	public PacketFileTransfer(){}

	public PacketFileTransfer(String fileName, boolean transferValid, int port){
		this.fileName = fileName;
		this.transferValid = transferValid;
		this.port = port;
	}

	public PacketFileTransfer(String fileName, boolean transferValid){
		this(fileName, transferValid, 0);
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		fileName = ByteBufUtils.readUTF8String(buf);
		transferValid = buf.readBoolean();
		port = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, fileName);
		buf.writeBoolean(transferValid);
		buf.writeInt(port);
	}

	public static class Handler implements IMessageHandler<PacketFileTransfer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketFileTransfer message, MessageContext ctx)
		{
			if (ctx.side == Side.CLIENT)
			{
				if (SchematicHandler.getFile(message.fileName) != null) {
					TownBuilder.proxy.getClientPlayer().addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "[CLIENT] Sending File"));
					FileHandler.instance.sendFileToServer(message.fileName);
					return new PacketFileTransfer(message.fileName, true);
				}
				else return new PacketFileTransfer(message.fileName, false);
			}
			else
			{
				if (!message.transferValid) ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("That file dose not exist on your client"));
			}
			return null;
		}
	}
}
