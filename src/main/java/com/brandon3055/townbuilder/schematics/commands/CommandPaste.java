package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.schematics.SchematicHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandPaste implements ISubCommand
{
	public static CommandPaste instance = new CommandPaste();

	@Override
	public String getCommandName() {
		return "paste";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args)
	{
		if (args.length < 2 || args.length > 3)
		{
			player.addChatMessage(new TextComponentString("/tt-schematic paste <name> {-i} [Past the specified schematic at your coords. -i will make it ignore air]"));
			return;
		}else
		{
			if (SchematicHandler.getFile(args[1]) == null)
			{
				player.addChatMessage(new TextComponentString(args[1] + " Dose not exist"));
				return;
			}else {
				boolean ia = args.length == 3 && args[2].equals("-i");
				try
				{
					SchematicHandler.loadAreaFromCompound(SchematicHandler.loadCompoundFromFile(args[1]), player.worldObj, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ), !ia);
				}
				catch (SchematicHandler.SchematicException e)
				{
					e.printStackTrace();
				}
				player.addChatMessage(new TextComponentString(args[1] + " Successfully added to world"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] args) {
		if (args.length == 2) {
			return CommandBase.getListOfStringsMatchingLastWord(args, Arrays.asList(SchematicHandler.getSchematics()));
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
			"Usage: /tt-schematic paste <name> [-i]",
			"",
			"Pasts the specified schematic at your location",
			"Add \"-i\" to ignore air"
		};
	}
}
