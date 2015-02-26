package com.brandon3055.townbuilder.schematics;

import com.brandon3055.townbuilder.utills.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Brandon on 25/02/2015.
 */
@SideOnly(Side.CLIENT)
public class FileSender
{
	public static final FileSender instance = new FileSender();
	private SenderThread thread;
	private String file = null;
	private int port;

	public FileSender()
	{
	}


	public void sendFile(String file, int port)
	{
		if (this.thread != null && this.thread.getRunning()) {
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "File transfer already in progress"));
			return;
		}
		this.thread = new SenderThread(this);
		this.file = file;
		this.thread.setRunning(true);
		this.thread.start();
		this.port = port;
	}


	public class SenderThread extends Thread
	{
		private FileSender sender;
		private boolean running = false;

		public SenderThread(FileSender sender)
		{
			super("TT File Sender Thread");
			this.sender = sender;
		}

		@Override
		public void run()
		{
			LogHelper.info("Thread run");
			if (sender.file == null) return;
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream os = null;
			ServerSocket servsock = null;
			Socket sock = null;

			LogHelper.info("Waiting for connection...");
			try
			{
				servsock = new ServerSocket(port);
				sock = servsock.accept();
				LogHelper.info("Accepted connection : " + sock);

				File myFile = SchematicHandler.getFile(sender.file);
				byte [] mybytearray  = new byte [(int)myFile.length()];
				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);
				os = sock.getOutputStream();

				LogHelper.info("Sending " + myFile.getName() + "(" + mybytearray.length + " bytes)");

				int count;
				while ((count = bis.read(mybytearray)) > 0)
				{
					os.write(mybytearray, 0, count);
				}

				os.flush();
				LogHelper.info("Done.");

				running = false;
				sender.file = null;
			}
			catch (IOException e)
			{
				running = false;
				sender.file = null;
				e.printStackTrace();
			}

		}

		public boolean getRunning() { return running; }

		public void setRunning(boolean b) { this.running = b; }
	}
}
