package com.brandon3055.townbuilder.schematics;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.townbuilder.ConfigHandler;
import com.brandon3055.townbuilder.utills.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			netHandler.playerEntity.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "A file transfer is already in progress from another client"));
			return;
		}
		this.client = netHandler.playerEntity;
		this.clientAddress = netHandler.netManager.channel().remoteAddress();
		LogHelper.info("Receiving file from: " + this.clientAddress);
		client.addChatComponentMessage(new TextComponentString("Receiving file from: " + this.clientAddress));
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

				client.addChatComponentMessage(new TextComponentString("Attempting to connect"));
				LogHelper.info("Attempting to connect");

				SocketAddress addr = new InetSocketAddress(BrandonsCore.proxy.getMCServer().getServerHostname(), ConfigHandler.filePort);
				Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);

				socket = new Socket(proxy);

				LogHelper.info("socket created");
				socket.connect(receiver.clientAddress);
				//		new Socket(receiver.clientAddress, ConfigHandler.filePort); // ######## This is where it throws the exception ########

				LogHelper.info("Connection established");
				client.addChatComponentMessage(new TextComponentString("Connection established"));

				is = socket.getInputStream();
				fos = new FileOutputStream(new File(SchematicHandler.getSaveFolder(), receiver.fileName + ".schematic"));
				bos = new BufferedOutputStream(fos);

				int size = socket.getReceiveBufferSize();

				byte [] bytes  = new byte [size];

				receiver.client.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Upload in progress..."));

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
				receiver.client.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Upload Complete"));
			}
			catch (IOException e)
			{
				client.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "Upload Failed [IOException]"));
				e.printStackTrace();
			}

			transferInProgress = false;
		}


		public boolean getTransferInProgress() { return transferInProgress; }
	}

}
