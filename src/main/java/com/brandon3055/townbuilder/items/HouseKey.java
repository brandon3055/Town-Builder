package com.brandon3055.townbuilder.items;

import com.brandon3055.brandonscore.items.ItemBCore;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Brandon on 24/01/2015.
 */
public class HouseKey extends ItemBCore {
    public HouseKey() {
        this.setMaxStackSize(1);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTagCompound() && (stack.getTagCompound().hasKey("Set") || stack.getTagCompound().hasKey("Bound"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Set")) {
            tooltip.add("Bound To: {X: " + stack.getTagCompound().getInteger("X") + ", " + "Y: " + stack.getTagCompound().getInteger("Y") + ", " + "Z: " + stack.getTagCompound().getInteger("Z") + "}");
        }
        else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Bound")) {
            tooltip.add(stack.getTagCompound().getString("Bound"));
        }
        else {
            tooltip.add("Unbound");
        }
    }
}
