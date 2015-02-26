package com.brandon3055.townbuilder.schematics.commands;

import com.brandon3055.townbuilder.ModBlocks;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import com.brandon3055.townbuilder.utills.Utills;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandBlock implements ISubCommand
{
	public static CommandBlock instance = new CommandBlock();

	@Override
	public String getCommandName() {
		return "block";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args)
	{
		if (args.length < 2 || args.length > 4)
		{
			player.addChatMessage(new ChatComponentText("/tt-schematic block <function> [Used to interact with the structure builder block you are looking at]"));
			return;
		}else
		{
			MovingObjectPosition mop = Utills.raytraceFromEntity(player.worldObj, player, 50);
			if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || player.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) != ModBlocks.structureBuilder)
			{
				player.addChatMessage(new ChatComponentText("Did not find Structure Builder! [you must be looking at a structure builder block to use this command]"));
				return;
			}
			TileStructureBuilder tile = player.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ) instanceof TileStructureBuilder ? (TileStructureBuilder) player.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ) : null;
			if (tile == null)
			{
				player.addChatMessage(new ChatComponentText("[ERROR 404] Block tile entity not found"));
				return;
			}

			if (args[1].equals("set"))
			{
				if (args.length != 3)
				{
					player.addChatMessage(new ChatComponentText("/tt-schematic block set <schematic name>"));
					return;
				}
				NBTTagCompound compound = SchematicHandler.loadCompoundFromFile(args[2]);
				if (compound == null)
				{
					player.addChatMessage(new ChatComponentText(args[2] + " Dose not exist"));
					return;
				}
				tile.schematic = args[2];
				tile.xSize = compound.getShort("Width");
				tile.ySize = compound.getShort("Height");
				tile.zSize = compound.getShort("Length");
				player.addChatMessage(new ChatComponentText(args[2] + " Bound to block"));
				player.worldObj.markBlockForUpdate(mop.blockX, mop.blockY, mop.blockZ);
			}
			else if (args[1].equals("toggleview"))
			{
				tile.showPosition = !tile.showPosition;
				player.worldObj.markBlockForUpdate(mop.blockX, mop.blockY, mop.blockZ);
			}

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
		return new String[]
		{
			"Usage: /tt-schematic block <function>",
			"",
			"Functions: (apply to the structure builder your looking at)",
			"-set <name> (Sets the schematic the block will place)",
			"-toggleview (Toggles schematic placement view)",
		};
	}
}
