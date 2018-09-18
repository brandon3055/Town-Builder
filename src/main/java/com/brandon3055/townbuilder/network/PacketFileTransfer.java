package com.brandon3055.townbuilder.network;

import com.brandon3055.brandonscore.network.MessageHandlerWrapper;
import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.FileHandler;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Brandon on 25/02/2015.
 */
public class PacketFileTransfer implements IMessage {
    public String fileName;
    public boolean transferValid;
    public int port;

    public PacketFileTransfer() {
    }

    public PacketFileTransfer(String fileName, boolean transferValid, int port) {
        this.fileName = fileName;
        this.transferValid = transferValid;
        this.port = port;
    }

    public PacketFileTransfer(String fileName, boolean transferValid) {
        this(fileName, transferValid, 0);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        fileName = ByteBufUtils.readUTF8String(buf);
        transferValid = buf.readBoolean();
        port = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, fileName);
        buf.writeBoolean(transferValid);
        buf.writeInt(port);
    }

    public static class Handler extends MessageHandlerWrapper<PacketFileTransfer, IMessage> {
        @Override
        public IMessage handleMessage(PacketFileTransfer message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                if (SchematicHandler.getFile(message.fileName) != null) {
                    TownBuilder.proxy.getClientPlayer().sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "[CLIENT] Sending File"));
                    FileHandler.instance.sendFileToServer(message.fileName);
                    return new PacketFileTransfer(message.fileName, true);
                }
                else return new PacketFileTransfer(message.fileName, false);
            }
            else {
                if (!message.transferValid)
                    ctx.getServerHandler().player.sendMessage(new TextComponentString("That file dose not exist on your client"));
            }
            return null;
        }
    }
}
