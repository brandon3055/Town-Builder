package com.brandon3055.townbuilder.schematics.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public abstract interface ISubCommand
{
	public abstract String getCommandName();

	public abstract void handleCommand(EntityPlayer player, String[] args);

	public abstract List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString);

	public abstract boolean canSenderUseCommand(ICommandSender sender);

	public abstract String[] helpInfo(EntityPlayer sender);
}

