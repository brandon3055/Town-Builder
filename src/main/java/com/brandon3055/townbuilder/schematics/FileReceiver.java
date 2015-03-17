package com.brandon3055.townbuilder.schematics;

import com.brandon3055.townbuilder.ConfigHandler;
import com.brandon3055.townbuilder.utills.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Brandon on 25/02/2015.
 */
@SideOnly(Side.SERVER)
public class FileReceiver
{
	public static FileReceiver instance = new FileReceiver();
	private ReceiverThread thread;
	private EntityPlayer client;
	private SocketAddress clientAddress;
	private String fileName;


	public void receiveFile(String fileName, NetHandlerPlayServer netHandler)
	{
		if (this.thread != null && this.thread.getTransferInProgress()){
			netHandler.playerEntity.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "A file transfer is already in progress from another client"));
			return;
		}
		this.client = netHandler.playerEntity;
		this.clientAddress = netHandler.func_147362_b().channel().remoteAddress();
		LogHelper.info("Receiving file from: " + this.clientAddress);
		client.addChatComponentMessage(new ChatComponentText("Receiving file from: " + this.clientAddress));
		this.fileName = fileName;
		this.thread = new ReceiverThread(this);
		this.thread.transferInProgress = true;
		this.thread.start();
	}

	public boolean getTransferInProgress() { return thread != null && thread.getTransferInProgress(); }


	public class ReceiverThread extends Thread
	{
		private FileReceiver receiver;
		private boolean transferInProgress;

		public ReceiverThread(FileReceiver receiver)
		{
			super("TT File Receiver Thread");
			this.receiver = receiver;
		}

		@Override
		public void run()
		{
			try
			{
				Socket socket;
				InputStream is;
				FileOutputStream fos;
				BufferedOutputStream bos;

				client.addChatComponentMessage(new ChatComponentText("Attempting to connect"));
				LogHelper.info("Attempting to connect");

				SocketAddress addr = new InetSocketAddress(MinecraftServer.getServer().getServerHostname(), ConfigHandler.filePort);
				Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);

				socket = new Socket(proxy);

				LogHelper.info("socket created");
				socket.connect(receiver.clientAddress);
				//		new Socket(receiver.clientAddress, ConfigHandler.filePort); // ######## This is where it throws the exception ########

				LogHelper.info("Connection established");
				client.addChatComponentMessage(new ChatComponentText("Connection established"));

				is = socket.getInputStream();
				fos = new FileOutputStream(new File(SchematicHandler.getSaveFolder(), receiver.fileName + ".schematic"));
				bos = new BufferedOutputStream(fos);

				int size = socket.getReceiveBufferSize();

				byte [] bytes  = new byte [size];

				receiver.client.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Upload in progress..."));

				int count;
				while ((count = is.read(bytes)) > 0) {
					bos.write(bytes, 0, count);
				}

				bos.flush();

				is.close();
				fos.close();
				bos.close();
				socket.close();

				LogHelper.info("File " + receiver.fileName + " downloaded [" + size + "]");
				receiver.client.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Upload Complete"));
			}
			catch (IOException e)
			{
				client.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Upload Failed [IOException]"));
				e.printStackTrace();
			}

			transferInProgress = false;
		}


		public boolean getTransferInProgress() { return transferInProgress; }
	}

}
