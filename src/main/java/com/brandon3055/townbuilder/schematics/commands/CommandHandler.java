package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.TownBuilder;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandHandler extends CommandBase
{

	private static List aliases;

	public static CommandHandler instance = new CommandHandler();
	public static Map<String, ISubCommand> commands = new HashMap();

	static
	{
		registerSubCommand(CommandDelete.instance);
		registerSubCommand(CommandHelp.instance);
		registerSubCommand(CommandCreate.instance);
		registerSubCommand(CommandList.instance);
		registerSubCommand(CommandBlock.instance);
		registerSubCommand(CommandPaste.instance);
		registerSubCommand(CommandSend.instance);
	}

	public static void init(FMLServerStartingEvent event)
	{
		aliases = new ArrayList();
		aliases.add("tbuilder");
		event.registerServerCommand(instance);
	}

	public static void registerSubCommand(ISubCommand command)
	{
		if (!commands.containsKey(command.getCommandName()))
		{
			commands.put(command.getCommandName(), command);
		}
	}

	public static Set<String> getCommandList()
	{
		return commands.keySet();
	}

	@Override
	public String getCommandName() {
		return "town-builder";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName() + " help";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (!(sender instanceof EntityPlayer)) return;
		if (args.length > 0 && commands.containsKey(args[0])) {
			if (commands.get(args[0]).canSenderUseCommand(sender)) commands.get(args[0]).handleCommand((EntityPlayer) sender, args);
			return;
		}
		throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsFromIterableMatchingLastWord(args, commands.keySet());
		}
		if (commands.containsKey(args[0])) {
			return (commands.get(args[0])).addTabCompletionOptions(sender, args);
		}
		return null;
	}


	public static boolean checkOpAndNotify(ICommandSender sender)
	{
		if (TownBuilder.proxy.isOp(sender.getCommandSenderName())) return true;
		else {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You need to be an op to use this command"));
			return false;
		}
	}
}
