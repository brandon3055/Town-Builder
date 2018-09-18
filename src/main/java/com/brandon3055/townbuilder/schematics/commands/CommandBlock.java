package com.brandon3055.townbuilder.schematics.commands;

import codechicken.lib.raytracer.RayTracer;
import com.brandon3055.townbuilder.TBFeatures;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandBlock implements ISubCommand {
    public static CommandBlock instance = new CommandBlock();

    @Override
    public String getCommandName() {
        return "block";
    }

    @Override
    public void handleCommand(EntityPlayer player, String[] args) {
        if (args.length < 2 || args.length > 4) {
            player.sendMessage(new TextComponentString("/tbuilder block <function> [Used to interact with the structure builder block you are looking at]"));
            return;
        }
        else {
            RayTraceResult mop = RayTracer.retrace(player, 50);
            if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK || player.world.getBlockState(mop.getBlockPos()).getBlock() != TBFeatures.structureBuilder) {
                player.sendMessage(new TextComponentString("Did not find Structure Builder! [you must be looking at a structure builder block to use this command]"));
                return;
            }
            TileStructureBuilder tile = player.world.getTileEntity(mop.getBlockPos()) instanceof TileStructureBuilder ? (TileStructureBuilder) player.world.getTileEntity(mop.getBlockPos()) : null;
            if (tile == null) {
                player.sendMessage(new TextComponentString("[ERROR 404] Block tile entity not found"));
                return;
            }

            if (args[1].equals("set")) {
                if (args.length != 3) {
                    player.sendMessage(new TextComponentString("/tt-schematic block set <schematic name>"));
                    return;
                }

                if (SchematicHandler.getFile(args[2]) == null) {
                    player.sendMessage(new TextComponentString(args[2] + " Dose not exist"));
                    return;
                }
                NBTTagCompound compound = null;
                try {
                    compound = SchematicHandler.loadCompoundFromFile(args[2]);
                }
                catch (SchematicHandler.SchematicException e) {
                    e.printStackTrace();
                }
                tile.schematic.value = args[2];
                tile.xSize.value = compound.getShort("Width");
                tile.ySize.value = compound.getShort("Height");
                tile.zSize.value = compound.getShort("Length");
                player.sendMessage(new TextComponentString(args[2] + " Bound to block"));
//				player.worldObj.markBlockForUpdate(mop.blockX, mop.blockY, mop.blockZ);
            }
            else if (args[1].equals("toggleview")) {
                tile.showPosition.value = !tile.showPosition.value;
                tile.getDataManager().detectAndSendChanges();
                player.sendMessage(new TextComponentString("Paste Area display " + (tile.showPosition.value ? "Activated" : "Deactivated")));
//				player.worldObj.markBlockForUpdate(mop.blockX, mop.blockY, mop.blockZ);
            }
            tile.updateBlock();
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, "set", "toggleview");
    }

    @Override
    public boolean canSenderUseCommand(ICommandSender sender) {
        return CommandHandler.checkOpAndNotify(sender);
    }

    @Override
    public String[] helpInfo(EntityPlayer sender) {
        return new String[]{"Usage: /tt-schematic block <function>", "", "Functions: (apply to the structure builder your looking at)", "-set <name> (Sets the schematic the block will place)", "-toggleview (Toggles schematic placement view)",};
    }
}
