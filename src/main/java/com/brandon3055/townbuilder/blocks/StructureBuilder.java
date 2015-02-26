package com.brandon3055.townbuilder.blocks;

import com.brandon3055.townbuilder.ModItems;
import com.brandon3055.townbuilder.TownBuilder;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Brandon on 21/02/2015.
 */
public class StructureBuilder extends Block {
	public StructureBuilder() {
		super(Material.rock);
		this.setBlockTextureName(TownBuilder.RPREFIX + "structureBuilder");
		this.setBlockName(TownBuilder.RPREFIX + "structureBuilder");
		this.setHardness(10F);
		this.setResistance(100F);

		GameRegistry.registerBlock(this, "structureBuilder");
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileStructureBuilder();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileStructureBuilder tile = world.getTileEntity(x, y, z) instanceof TileStructureBuilder ? (TileStructureBuilder) world.getTileEntity(x, y, z) : null;
		if (tile == null) return false;
		ItemStack stack = player.getHeldItem();

		if (player.capabilities.isCreativeMode)
		{
			if (player.isSneaking())
			{
				switch (side)
				{
					case 0:
					case 1:
						tile.yOffset += ForgeDirection.getOrientation(side).offsetY;
						return true;
					case 2:
					case 3:
						tile.zOffset += ForgeDirection.getOrientation(side).offsetZ;
						return true;
					case 4:
					case 5:
						tile.xOffset += ForgeDirection.getOrientation(side).offsetX;
						return true;
				}
			}
			else if (stack != null && stack.getItem() == ModItems.houseKey)
			{
				handleKeyClick(tile, player, stack);
				return true;
			}
			return true;
		}
		else if (stack != null && stack.getItem() == ModItems.houseKey)
		{
			handleKeyClick(tile, player, stack);
			return true;
		}

		return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
	}

	private boolean handleKeyClick(TileStructureBuilder tile, EntityPlayer player, ItemStack stack)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Bound"))
		{
			if (stack.getTagCompound().getInteger("KeyCode") == tile.keyCode)
			{
				if (SchematicHandler.loadCompoundFromFile(tile.schematic) == null)
				{
					player.addChatComponentMessage(new ChatComponentText("[Error - 404] Schematic {" + tile.schematic + "} not found!!!"));
					return false;
				}

				World world = tile.getWorldObj();
				int x = tile.xCoord;
				int y = tile.yCoord;
				int z = tile.zCoord;

				world.setBlock(x, y, z, Blocks.standing_sign);
				TileEntitySign sign = world.getTileEntity(x, y, z) instanceof TileEntitySign ? (TileEntitySign) world.getTileEntity(x, y, z) : null;
				if (sign != null)
				{
					sign.signText[0] = EnumChatFormatting.DARK_BLUE + "###############";
					sign.signText[1] = EnumChatFormatting.DARK_RED + "Purchased By";
					sign.signText[2] = EnumChatFormatting.DARK_GREEN + player.getCommandSenderName();
					sign.signText[3] = EnumChatFormatting.DARK_BLUE + "###############";
				}

				world.setBlockMetadataWithNotify(x, y, z, tile.signRotation, 2);

				SchematicHandler.loadAreaFromCompound(SchematicHandler.loadCompoundFromFile(tile.schematic), player.worldObj, tile.xCoord + tile.xOffset, tile.yCoord + tile.yOffset, tile.zCoord + tile.zOffset, tile.copyAir);


				return true;
			}
		}
		else if (player.capabilities.isCreativeMode)
		{
			if (tile.keyCode == 0) tile.keyCode = MathHelper.getRandomIntegerInRange(tile.getWorldObj().rand, 1, Integer.MAX_VALUE);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("KeyCode", tile.keyCode);
			stack.getTagCompound().setString("Bound", "Bound to [x:" + tile.xCoord + " ,y:" + tile.yCoord + " ,z:" + tile.zCoord + "]");
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack p_149689_6_) {
		TileStructureBuilder tile = world.getTileEntity(x, y, z) instanceof TileStructureBuilder ? (TileStructureBuilder) world.getTileEntity(x, y, z) : null;
		if (tile == null) return;
		tile.signRotation =  MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
	}
}
