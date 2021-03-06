package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.ConfigHandler;
import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.network.PacketFileTransfer;
import com.brandon3055.townbuilder.schematics.FileHandler;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandSend implements ISubCommand {

    public static CommandSend instance = new CommandSend();

    @Override
    public String getCommandName() {
        return "uploadtoserver";
    }

    @Override
    public void handleCommand(EntityPlayer player, String[] args) {
        if (args.length == 2 && TownBuilder.proxy.isDedicatedServer() && player instanceof EntityPlayerMP) {
            if (SchematicHandler.getFile(args[1]) != null) {
                player.sendMessage(new TextComponentString(args[1] + " Already exists on the server"));
                return;
            }
            if (!FileHandler.instance.transferInProgress) {
                FileHandler.instance.fileName = args[1];
                TownBuilder.network.sendTo(new PacketFileTransfer(args[1], true, ConfigHandler.filePort), (EntityPlayerMP) player);
            }
            else
                player.sendMessage(new TextComponentString(TextFormatting.RED + "The server is already receiving a file"));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
        return null;
    }

    @Override
    public boolean canSenderUseCommand(ICommandSender sender) {
        return CommandHandler.checkOpAndNotify(sender);
    }

    @Override
    public String[] helpInfo(EntityPlayer sender) {
        return new String[]{"Usage: /tt-schematic uploadtoserver <name>", "", "Sends the specified schematic from your client", "to the server"};
    }
}
