package com.brandon3055.townbuilder.schematics;

import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.network.PacketByteStream;
import com.brandon3055.townbuilder.utills.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 2/03/2015.
 */
public class FileHandler {
    public static FileHandler instance = new FileHandler();

    public boolean transferInProgress = false;
    private byte[] bytes;
    private List<PacketByteStream> receivedPackets;
    public String fileName;
    private int transferTimeOut = 0;
    private EntityPlayer player;

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (transferInProgress && event.phase == TickEvent.Phase.END && transferTimeOut++ >= 200) {
            transferInProgress = false;
            transferTimeOut = 0;
            receivedPackets = null;
            if (player != null)
                player.sendMessage(new TextComponentString(TextFormatting.RED + "[SERVER] Upload Failed (missing packet(s))"));
            LogHelper.error("File upload failed (did not receive all packets)");
        }
    }

    public void sendFileToServer(String filename) {
        if (TownBuilder.proxy.isDedicatedServer()) {
            LogHelper.error("Attempt to send file from wrong side!!!");
            return;
        }

        this.fileName = filename;

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        File file = SchematicHandler.getFile(filename);

        bytes = new byte[(int) file.length()];

        try {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bis = new BufferedInputStream(fis);

        int length = 0;

        try {
            length = bis.read(bytes);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        List<byte[]> splitBytes = new ArrayList<byte[]>();

        for (int i = 0; i < bytes.length; i++) {
            int packet = i / 32000;
            if (splitBytes.size() <= packet) {
                int size = bytes.length - i;
                splitBytes.add(new byte[size >= 32000 ? 32000 : size]);
            }

            splitBytes.get(packet)[i - packet * 32000] = bytes[i];
        }

        LogHelper.info("sending " + bytes.length + " bytes");

        for (int i = 0; i < splitBytes.size(); i++) {
            TownBuilder.network.sendToServer(new PacketByteStream(splitBytes.get(i), splitBytes.size(), i));
        }

        TownBuilder.proxy.getClientPlayer().sendMessage(new TextComponentString(TextFormatting.GREEN + "[CLIENT] File Sent"));
    }

    public void receiveFile(PacketByteStream message, MessageContext ctx) {
        if (player == null) player = ctx.getServerHandler().player;
        if (transferInProgress && ctx.getServerHandler().player != player || !TownBuilder.proxy.isOp(player.getName())) {
            ctx.getServerHandler().player.sendMessage(new TextComponentString(TextFormatting.RED + "[SERVER] Error Transfer already in progress... "));
            LogHelper.error("#####################################################");
            LogHelper.error("Client attempted to send unauthorised file to server");
            LogHelper.error("This is ether a result of a bug or client hacking");
            LogHelper.error("Please report to brandon3055");
            LogHelper.error("Sender: " + ctx.getServerHandler().player.getName());
            LogHelper.error("#####################################################");
            return;
        }

        //receive
        transferInProgress = true;
        transferTimeOut = 0;
        if (receivedPackets == null) {
            player = ctx.getServerHandler().player;
            LogHelper.info("Receiving File from " + ctx.getServerHandler().player.getName() + " [" + message.packetCount + " packets]");
            ctx.getServerHandler().player.sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "[SERVER] Receiving File"));
            receivedPackets = new ArrayList<PacketByteStream>(message.packetCount);
        }
        receivedPackets.add(message.packetindex, message);
        if (receivedPackets.size() < message.packetCount) return;

        //process
        int totalBytes = 0;
        for (PacketByteStream p : receivedPackets) totalBytes += p.bytes.length;

        byte[] receivedBytes = new byte[totalBytes];

        for (int i = 0; i < receivedPackets.size(); i++) {
            byte[] b = receivedPackets.get(i).bytes;
            for (int j = 0; j < b.length; j++) {
                receivedBytes[j + (i * 32000)] = b[j];
            }
        }

        LogHelper.info("received " + receivedBytes.length + " bytes");

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            fos = new FileOutputStream(new File(SchematicHandler.getSaveFolder(), fileName + ".schematic"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bos = new BufferedOutputStream(fos);

        try {
            bos.write(receivedBytes, 0, receivedBytes.length);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bos.flush();
            fos.close();
            bos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        transferInProgress = false;
        receivedPackets = null;
        ctx.getServerHandler().player.sendMessage(new TextComponentString(TextFormatting.GREEN + "[SERVER] File Received"));
    }


}
