package com.brandon3055.townbuilder.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Created by Brandon on 21/02/2015.
 */
public class SchematicTool extends Item {
    public SchematicTool() {
//		this.setUnlocalizedName(TownBuilder.RPREFIX + "schematicTool");
        this.setMaxStackSize(1);
        setHasSubtypes(true);
//
//		GameRegistry.registerItem(this, "schematicTool");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this, 1);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("Pos1Y", -1);
            items.add(stack);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() && stack.hasTagCompound()) {
            stack.getTagCompound().setInteger("Pos1Y", -1);
        }
        return super.onItemRightClick(world, player, hand);

    }
}
