package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.schematics.SchematicHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandDelete implements ISubCommand
{
	public static CommandDelete instance = new CommandDelete();

	@Override
	public String getCommandName() {
		return "delete";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args)
	{
		if (args.length < 2 || args.length > 2)
		{
			player.addChatMessage(new ChatComponentText("Usage: /tt-schematic delete <name> [Deletes the specified schematic]"));
			return;
		}else
		{
			if (SchematicHandler.getFile(args[1]) != null)
			{
				SchematicHandler.deleteCompoundFile(args[1]);
				player.addChatMessage(new ChatComponentText(args[1] + " Deleted"));
			}else player.addChatMessage(new ChatComponentText(args[1] + " Dose not exist"));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 2) {
			return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, Arrays.asList(SchematicHandler.getSchematics()));
		}
		return null;
	}

	@Override
	public boolean canSenderUseCommand(ICommandSender sender) {
		return CommandHandler.checkOpAndNotify(sender);
	}

	@Override
	public String[] helpInfo(EntityPlayer sender) {
		return new String[]
		{
			"Usage: /tt-schematic delete <name>",
			"",
			"Deletes the specified schematic"
		};
	}
}
