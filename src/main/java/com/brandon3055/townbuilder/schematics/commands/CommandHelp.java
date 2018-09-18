package com.brandon3055.townbuilder.schematics.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandHelp implements ISubCommand {
    public static CommandHelp instance = new CommandHelp();

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void handleCommand(EntityPlayer player, String[] args) {
        if (args.length == 2 && CommandHandler.commands.containsKey(args[1])) {
            for (int i = 0; i < CommandHandler.commands.get(args[1]).helpInfo(player).length; i++) {
                if (i > 0)
                    player.sendMessage(new TextComponentString(TextFormatting.GRAY + CommandHandler.commands.get(args[1]).helpInfo(player)[i]));
                else
                    player.sendMessage(new TextComponentString(CommandHandler.commands.get(args[1]).helpInfo(player)[i]));
            }
            return;
        }
        player.sendMessage(new TextComponentString("Usage: /tbuilder help <command>"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Commands:"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "-create"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "-delete"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "-list"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "-paste"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "-block"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "-uploadtoserver"));

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
        return CommandBase.getListOfStringsMatchingLastWord(paramArrayOfString, CommandHandler.commands.keySet());
    }

    @Override
    public boolean canSenderUseCommand(ICommandSender sender) {
        return CommandHandler.checkOpAndNotify(sender);
    }

    @Override
    public String[] helpInfo(EntityPlayer sender) {
        return new String[]{"Usage: /tt-schematic help <command>", "", "Gives information about the usage of the given command"};
    }
	/*

	if (args.length == 1)
			{
				player.addChatMessage(new TextComponentString(TextFormatting.BOLD + "Note this will open the following web page"));
				player.addChatMessage(new TextComponentString(TextFormatting.BLUE + "http://youtu.be/3vpUF5-qJK8"));
				player.addChatMessage(new TextComponentString("To open the page run: /tt-schematic tutorial confirm"));
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
