package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.network.PacketClientList;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandList implements ISubCommand
{
	public static CommandList instance = new CommandList();

	@Override
	public String getCommandName() {
		return "list";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args)
	{
		if (args != null && args.length == 2 && (args[1].equals("client") || args[1].equals("c")) && TownBuilder.proxy.isDedicatedServer() && player instanceof EntityPlayerMP)
		{
			TownBuilder.network.sendTo(new PacketClientList(), (EntityPlayerMP)player);
			return;
		}
		String names = "";
		for (String s : SchematicHandler.getSchematics())
		{
			names = names + TextFormatting.DARK_PURPLE + s + ", ";
		}
		if (StringUtils.isNullOrEmpty(names)) names = "[404] - No schematics found";

		player.addChatMessage(new TextComponentString(names));
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
		List<String> l = new ArrayList<String>();
		l.add("client");
		return l;
	}

	@Override
	public boolean canSenderUseCommand(ICommandSender sender) {
		return CommandHandler.checkOpAndNotify(sender);
	}

	@Override
	public String[] helpInfo(EntityPlayer sender) {
		return new String[]
		{
			"Usage: /tt-schematic list [client]",
			"",
			"Lists all schematics",
			"Add \"client\" to display schematics",
			"on your local machine if you are on a server"
		};
	}
}
