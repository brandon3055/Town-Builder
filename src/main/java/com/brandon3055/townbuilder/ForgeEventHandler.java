package com.brandon3055.townbuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Brandon on 22/01/2015.
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event)
	{
		if ((event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) || event.entityPlayer.getHeldItem() == null || event.entityPlayer.getHeldItem().getItem() != ModItems.schematicTool) return;

		ItemStack tool = event.entityPlayer.getHeldItem();

		if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
		{
			if (!tool.hasTagCompound()) tool.setTagCompound(new NBTTagCompound());
			if (tool.getTagCompound().getInteger("Pos1Y") == -1)
			{
				tool.getTagCompound().setInteger("Pos2X", event.x);
				tool.getTagCompound().setInteger("Pos2Y", event.y);
				tool.getTagCompound().setInteger("Pos2Z", event.z);
			}
			tool.getTagCompound().setInteger("Pos1X", event.x);
			tool.getTagCompound().setInteger("Pos1Y", event.y);
			tool.getTagCompound().setInteger("Pos1Z", event.z);
			event.entityPlayer.addChatComponentMessage(new ChatComponentText("Pos1 Set"));
			event.setCanceled(true);
		}
		else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (!tool.hasTagCompound()) tool.setTagCompound(new NBTTagCompound());
			if (tool.getTagCompound().getInteger("Pos1Y") == -1)
			{
				tool.getTagCompound().setInteger("Pos1X", event.x);
				tool.getTagCompound().setInteger("Pos1Y", event.y);
				tool.getTagCompound().setInteger("Pos1Z", event.z);
			}
			tool.getTagCompound().setInteger("Pos2X", event.x);
			tool.getTagCompound().setInteger("Pos2Y", event.y);
			tool.getTagCompound().setInteger("Pos2Z", event.z);
			if (event.entityPlayer.worldObj.isRemote)event.entityPlayer.addChatComponentMessage(new ChatComponentText("Pos2 Set"));
			else event.setCanceled(true);
		}
	}
}
