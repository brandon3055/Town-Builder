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
}
