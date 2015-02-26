package com.brandon3055.townbuilder.items;

import com.brandon3055.townbuilder.TownBuilder;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Brandon on 21/02/2015.
 */
public class SchematicTool extends Item
{
	public SchematicTool() {
		this.setUnlocalizedName(TownBuilder.RPREFIX + "schematicTool");
		this.setMaxStackSize(1);
		setHasSubtypes(true);

		GameRegistry.registerItem(this, "schematicTool");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs p_150895_2_, List list) {
		ItemStack stack = new ItemStack(item, 1);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Pos1Y", -1);
		list.add(stack);
	}

	@Override
	public void registerIcons(IIconRegister iIconRegister) {
		itemIcon = iIconRegister.registerIcon(TownBuilder.RPREFIX + "schematicTool");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {//throws IOException
		//if (!player.worldObj.isRemote) TolkienTweaks.proxy.receiveFile("test", ((EntityPlayerMP)player).playerNetServerHandler);
		//LogHelper.info(TolkienTweaks.proxy.isDedicatedServer());

		if (player.isSneaking() && stack.hasTagCompound()) stack.getTagCompound().setInteger("Pos1Y", -1);
		return stack;
	}
}
