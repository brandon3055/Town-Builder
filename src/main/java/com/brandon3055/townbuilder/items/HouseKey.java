package com.brandon3055.townbuilder.items;

import com.brandon3055.townbuilder.TownBuilder;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Brandon on 24/01/2015.
 */
public class HouseKey extends Item {
	public HouseKey() {
		this.setUnlocalizedName(TownBuilder.RPREFIX + "houseKey");
		this.setMaxStackSize(1);

		GameRegistry.registerItem(this, "houseKey");
	}

	@Override
	public void registerIcons(IIconRegister iIconRegister) {
		itemIcon = iIconRegister.registerIcon(TownBuilder.RPREFIX + "houseKey");
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return par1ItemStack.hasTagCompound() && (par1ItemStack.getTagCompound().hasKey("Set") || par1ItemStack.getTagCompound().hasKey("Bound"));
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

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		return super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
	}
}
