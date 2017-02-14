package com.brandon3055.townbuilder.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Brandon on 21/02/2015.
 */
public class SchematicTool extends Item
{
	public SchematicTool() {
//		this.setUnlocalizedName(TownBuilder.RPREFIX + "schematicTool");
		this.setMaxStackSize(1);
		setHasSubtypes(true);
//
//		GameRegistry.registerItem(this, "schematicTool");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs p_150895_2_, List list) {
		ItemStack stack = new ItemStack(item, 1);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Pos1Y", -1);
		list.add(stack);
	}

//	@Override
//	public void registerIcons(IIconRegister iIconRegister) {
//		itemIcon = iIconRegister.registerIcon(TownBuilder.RPREFIX + "schematicTool");
//	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		//if (!player.worldObj.isRemote) TolkienTweaks.proxy.receiveFile("test", ((EntityPlayerMP)player).playerNetServerHandler);
		//LogHelper.info(TolkienTweaks.proxy.isDedicatedServer());

        //addBlocks[index >> 1] = (byte) (((index & 1) != 0) ? addBlocks[index >> 1] & 0xF0 | (Block.getIdFromBlock(block) >> 8) & 0xF : addBlocks[index >> 1] & 0xF | ((Block.getIdFromBlock(block) >> 8) & 0xF) << 4);

//        if (world.isRemote) {
//
//
//            byte id = (byte)world.rand.nextInt(16);
//            byte meta = (byte)world.rand.nextInt(16);
//
//            byte b = (byte)((id & 0xF) << 4 | meta);
//
//
//            LogHelper.info("Combined: " + Integer.toBinaryString(b)+" value: "+b);
//
//            int decodedID = (b & 0xF0) >> 4;
//            int decodedMeta = b & 0xF;
//
//
//            LogHelper.info("Input:  " + id + " " + meta);
//            LogHelper.info("Output: " + decodedID + " " + decodedMeta);
//
//
//        }
		if (player.isSneaking() && stack.hasTagCompound()) {
			stack.getTagCompound().setInteger("Pos1Y", -1);
		}
		return super.onItemRightClick(stack, world, player, hand);

	}
}
