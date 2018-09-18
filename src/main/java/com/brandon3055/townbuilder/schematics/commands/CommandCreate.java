package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.TBFeatures;
import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.network.PacketSchematicClient;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandCreate implements ISubCommand {
    public static CommandCreate instance = new CommandCreate();

    @Override
    public String getCommandName() {
        return "create";
    }

    @Override
    public void handleCommand(EntityPlayer player, String[] args) {
        if (args.length < 2 || args.length > 3) {
            player.sendMessage(new TextComponentString("/tbuilder create <name> {-o} [Creates a schematic with given name. -o will make it overwrite an existing schematic with that name if one exists]"));
            return;
        }
        if (args.length == 3 && args[2].equals("client") && TownBuilder.proxy.isDedicatedServer()) {
            TownBuilder.network.sendTo(new PacketSchematicClient(args[1]), (EntityPlayerMP) player);
            return;
        }
        if (SchematicHandler.getFile(args[1]) != null && (args.length != 3 || !args[2].equals("-o"))) {
            player.sendMessage(new TextComponentString("That name is already used! Ether delete it or pick a new name"));
            return;
        }
        ItemStack tool = player.getHeldItemMainhand();
        if (tool.isEmpty() || tool.getItem() != TBFeatures.schematicTool) {
            player.sendMessage(new TextComponentString("You are not holding a Schematic Tool"));
            return;
        }
        if (!tool.hasTagCompound() || tool.getTagCompound().getInteger("Pos1Y") == -1) {
            player.sendMessage(new TextComponentString("You must first set the region to copy by right clicking one corner of the region and left clicking the other corner"));
            return;
        }

        int x1 = tool.getTagCompound().getInteger("Pos1X");
        int y1 = tool.getTagCompound().getInteger("Pos1Y");
        int z1 = tool.getTagCompound().getInteger("Pos1Z");
        int x2 = tool.getTagCompound().getInteger("Pos2X");
        int y2 = tool.getTagCompound().getInteger("Pos2Y");
        int z2 = tool.getTagCompound().getInteger("Pos2Z");

        int ph;
        if (x2 < x1) {
            ph = x1;
            x1 = x2;
            x2 = ph;
        }
        if (y2 < y1) {
            ph = y1;
            y1 = y2;
            y2 = ph;
        }
        if (z2 < z1) {
            ph = z1;
            z1 = z2;
            z2 = ph;
        }

        int xSize = x2 - x1 + 1;
        int ySize = y2 - y1 + 1;
        int zSize = z2 - z1 + 1;

        SchematicHandler.saveCompoundToFile(SchematicHandler.getCompoundForArea(player.world, x1, y1, z1, xSize, ySize, zSize), args[1]);

        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Region successfully saved to schematic"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
        if (paramArrayOfString.length != 3) return null;
        List<String> l = new ArrayList<String>();
        l.add("-o");
        l.add("client");
        return l;
    }

    @Override
    public boolean canSenderUseCommand(ICommandSender sender) {
        return CommandHandler.checkOpAndNotify(sender);
    }

    @Override
    public String[] helpInfo(EntityPlayer sender) {
        return new String[]{"Usage: /tt-schematic create <name> [-o] or [client]", "", "Creates a schematic of the selected area with the given name", "add \"-o\" to overwrite an existing schematic with the given name", "add \"client\" to save the schematic locally ", "(if you are on a server)", "", "Use the schematic tool to select an area using left & right", "click to select 2 points", "You must be holding the tool when you run the command",};
    }
}
