package com.brandon3055.townbuilder.items;

import com.brandon3055.brandonscore.items.ItemBCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Brandon on 24/01/2015.
 */
public class HouseKey extends ItemBCore {
	public HouseKey() {
//		this.setUnlocalizedName(TownBuilder.RPREFIX + "houseKey");
		this.setMaxStackSize(1);

//		GameRegistry.registerItem(this, "houseKey");
	}

//	@Override
//	public void registerIcons(IIconRegister iIconRegister) {
//		itemIcon = iIconRegister.registerIcon(TownBuilder.RPREFIX + "houseKey");
//	}


	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.hasTagCompound() && (stack.getTagCompound().hasKey("Set") || stack.getTagCompound().hasKey("Bound"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Set"))
		{
			list.add("Bound To: {X: "+stack.getTagCompound().getInteger("X")+", "+"Y: "+stack.getTagCompound().getInteger("Y")+", "+"Z: "+stack.getTagCompound().getInteger("Z")+"}");
		}
		else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Bound"))
		{
			list.add(stack.getTagCompound().getString("Bound"));
		}
		else
		{
			list.add("Unbound");
		}
	}
}
