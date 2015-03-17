package com.brandon3055.townbuilder.schematics.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandHelp implements ISubCommand
{
	public static CommandHelp instance = new CommandHelp();

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args)
	{
		if (args.length == 2 && CommandHandler.commands.containsKey(args[1]))
		{
			for (int i = 0; i < CommandHandler.commands.get(args[1]).helpInfo(player).length; i++)
			{
				if (i > 0) player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + CommandHandler.commands.get(args[1]).helpInfo(player)[i]));
				else  player.addChatComponentMessage(new ChatComponentText(CommandHandler.commands.get(args[1]).helpInfo(player)[i]));
			}
			return;
		}
		player.addChatComponentMessage(new ChatComponentText("Usage: /tt-schematic help <command>"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Commands:"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "-create"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "-delete"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "-list"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "-paste"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "-block"));
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "-uploadtoserver"));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
		return CommandBase.getListOfStringsFromIterableMatchingLastWord(paramArrayOfString, CommandHandler.commands.keySet());
	}

	@Override
	public boolean canSenderUseCommand(ICommandSender sender) {
		return CommandHandler.checkOpAndNotify(sender);
	}

	@Override
	public String[] helpInfo(EntityPlayer sender) {
		return new String[]
		{
			"Usage: /tt-schematic help <command>",
			"",
			"Gives information about the usage of the given command"
		};
	}
	/*

	if (args.length == 1)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.BOLD + "Note this will open the following web page"));
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "http://youtu.be/3vpUF5-qJK8"));
				player.addChatMessage(new ChatComponentText("To open the page run: /tt-schematic tutorial confirm"));
			}
			else if (args.length == 2 && args[1].equals("confirm"))
			{
				try
				{
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null);
					oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI("http://youtu.be/3vpUF5-qJK8"));
				}
				catch (Throwable throwable)
				{
					LogHelper.error("Couldn\'t open link " + throwable);
				}
			}

	 */
}
