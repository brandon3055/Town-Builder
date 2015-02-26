package com.brandon3055.townbuilder.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by Brandon on 24/01/2015.
 */
public class HouseBuilderItemBlock extends ItemBlock {
	public HouseBuilderItemBlock(Block p_i45328_1_) {
		super(p_i45328_1_);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}
}
