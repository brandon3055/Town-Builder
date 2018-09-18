package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.TownBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandHandler extends CommandBase {

    private static List<String> aliases;

    public static CommandHandler instance = new CommandHandler();
    public static Map<String, ISubCommand> commands = new HashMap();

    static {
        registerSubCommand(CommandDelete.instance);
        registerSubCommand(CommandHelp.instance);
        registerSubCommand(CommandCreate.instance);
        registerSubCommand(CommandList.instance);
        registerSubCommand(CommandBlock.instance);
        registerSubCommand(CommandPaste.instance);
        registerSubCommand(CommandSend.instance);
    }

    public static void init(FMLServerStartingEvent event) {
        aliases = new ArrayList<>();
        aliases.add("tbuilder");
        event.registerServerCommand(instance);
    }

    public static void registerSubCommand(ISubCommand command) {
        if (!commands.containsKey(command.getCommandName())) {
            commands.put(command.getCommandName(), command);
        }
    }

    public static Set<String> getCommandList() {
        return commands.keySet();
    }

    @Override
    public String getName() {
        return "town-builder";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName() + " help";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayer)) return;
        if (args.length > 0 && commands.containsKey(args[0])) {
            if (commands.get(args[0]).canSenderUseCommand(sender))
                commands.get(args[0]).handleCommand((EntityPlayer) sender, args);
            return;
        }
        throw new WrongUsageException("Type '" + getUsage(sender) + "' for help.");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, commands.keySet());
        }
        if (commands.containsKey(args[0])) {
            return (commands.get(args[0])).addTabCompletionOptions(sender, args);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    public static boolean checkOpAndNotify(ICommandSender sender) {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer() && sender instanceof EntityPlayer && ((EntityPlayer) sender).isCreative()) {
            return true;
        }
        if (TownBuilder.proxy.isOp(sender.getName())) {
            return true;
        }
        else {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "You need to be an op to use this command"));
            return false;
        }
    }
}
