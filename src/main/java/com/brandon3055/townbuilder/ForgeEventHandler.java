package com.brandon3055.townbuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Brandon on 22/01/2015.
 */
public class ForgeEventHandler {

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event) {
        if (!(event instanceof PlayerInteractEvent.LeftClickBlock || event instanceof PlayerInteractEvent.RightClickBlock) || event.getEntityPlayer().getHeldItemMainhand().isEmpty() || event.getEntityPlayer().getHeldItemMainhand().getItem() != TBFeatures.schematicTool) {
            return;
        }

        ItemStack tool = event.getEntityPlayer().getHeldItemMainhand();

        if (event instanceof PlayerInteractEvent.LeftClickBlock) {
            if (!tool.hasTagCompound()) tool.setTagCompound(new NBTTagCompound());
            if (tool.getTagCompound().getInteger("Pos1Y") == -1) {
                tool.getTagCompound().setInteger("Pos2X", event.getPos().getX());
                tool.getTagCompound().setInteger("Pos2Y", event.getPos().getY());
                tool.getTagCompound().setInteger("Pos2Z", event.getPos().getZ());
            }
            tool.getTagCompound().setInteger("Pos1X", event.getPos().getX());
            tool.getTagCompound().setInteger("Pos1Y", event.getPos().getY());
            tool.getTagCompound().setInteger("Pos1Z", event.getPos().getZ());
            event.getEntityPlayer().sendMessage(new TextComponentString("Pos1 Set"));
            event.setCanceled(true);
        }
        else if (event instanceof PlayerInteractEvent.RightClickBlock) {
            if (!tool.hasTagCompound()) tool.setTagCompound(new NBTTagCompound());
            if (tool.getTagCompound().getInteger("Pos1Y") == -1) {
                tool.getTagCompound().setInteger("Pos1X", event.getPos().getX());
                tool.getTagCompound().setInteger("Pos1Y", event.getPos().getY());
                tool.getTagCompound().setInteger("Pos1Z", event.getPos().getZ());
            }

            tool.getTagCompound().setInteger("Pos2X", event.getPos().getX());
            tool.getTagCompound().setInteger("Pos2Y", event.getPos().getY());
            tool.getTagCompound().setInteger("Pos2Z", event.getPos().getZ());
            if (event.getEntityPlayer().world.isRemote)
                event.getEntityPlayer().sendMessage(new TextComponentString("Pos2 Set"));
            else event.setCanceled(true);
        }
    }
}
