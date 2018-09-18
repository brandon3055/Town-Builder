package com.brandon3055.townbuilder.blocks;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IRenderOverride;
import com.brandon3055.brandonscore.utils.InventoryUtils;
import com.brandon3055.townbuilder.TBFeatures;
import com.brandon3055.townbuilder.client.rendering.RenderTileStructureBuilder;
import com.brandon3055.townbuilder.schematics.SchematicHandler;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import com.brandon3055.townbuilder.utills.LogHelper;
import com.brandon3055.townbuilder.utills.Utills;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Brandon on 21/02/2015.
 */
public class StructureBuilder extends BlockBCore implements ITileEntityProvider, IRenderOverride {
    public StructureBuilder() {
        this.setHardness(10F);
        this.setResistance(100F);

    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileStructureBuilder tile = world.getTileEntity(pos) instanceof TileStructureBuilder ? (TileStructureBuilder) world.getTileEntity(pos) : null;
        if (tile == null) return false;

        ItemStack stack = player.getHeldItem(hand);

        if (player.capabilities.isCreativeMode) {
            if (player.isSneaking()) {
                switch (side) {
                    case DOWN:
                    case UP:
                        tile.yOffset.value += side.getFrontOffsetY();
                        return true;
                    case NORTH:
                    case SOUTH:
                        tile.zOffset.value += side.getFrontOffsetZ();
                        return true;
                    case WEST:
                    case EAST:
                        tile.xOffset.value += side.getFrontOffsetX();
                        return true;
                }
            }
            else if (!stack.isEmpty() && stack.getItem() == TBFeatures.houseKey) {
                handleKeyClick(tile, player, stack);
                return true;
            }
            return true;
        }
        else if (!stack.isEmpty() && stack.getItem() == TBFeatures.houseKey) {
            handleKeyClick(tile, player, stack);
            return true;
        }

        return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }

    private boolean handleKeyClick(TileStructureBuilder tile, EntityPlayer player, ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Bound")) {
            if (tile.keyCode.value == 0) {
                tile.keyCode.value = stack.getTagCompound().getInteger("KeyCode");
                stack.getTagCompound().setString("Bound", "Bound to [multiple builders]");
                if (player.world.isRemote) player.sendMessage(new TextComponentString("Builder bound to key"));
                return false;
            }

            if (stack.getTagCompound().getInteger("KeyCode") == tile.keyCode.value) {
                if (SchematicHandler.getFile(tile.schematic.value) == null) {
                    player.sendMessage(new TextComponentString("[Error - 404] Schematic {" + tile.schematic.value + "} not found!!!"));
                    LogHelper.info("[Error - 404] Schematic {" + tile.schematic + "} not found!!!");
                    return false;
                }

                World world = tile.getWorld();
                BlockPos pos = tile.getPos();
                world.setBlockState(pos, Blocks.STANDING_SIGN.getStateFromMeta(tile.signRotation.value));
                TileEntitySign sign = world.getTileEntity(pos) instanceof TileEntitySign ? (TileEntitySign) world.getTileEntity(pos) : null;

                if (sign != null) {
                    sign.signText[0] = new TextComponentString(Utills.cutStringToLength("§1" + "################", 15));//"§1" + "#############";
                    sign.signText[1] = new TextComponentString(Utills.cutStringToLength("§4" + "Purchased By", 15));//"§4" + "Purchased By";
                    sign.signText[2] = new TextComponentString(Utills.cutStringToLength("§2" + player.getName(), 15));// + player.getCommandSenderName());//"§2" + "test";// + player.getCommandSenderName();
                    sign.signText[3] = new TextComponentString(Utills.cutStringToLength("§1" + "################", 15));//"§1" + "#############";
                }

//				world.setBlockMetadataWithNotify(x, y, z, tile.signRotation, 2);

                try {
                    SchematicHandler.loadAreaFromCompound(SchematicHandler.loadCompoundFromFile(tile.schematic.value), player.world, pos.getX() + tile.xOffset.value, pos.getY() + tile.yOffset.value, pos.getZ() + tile.zOffset.value, tile.copyAir.value);
                    InventoryUtils.consumeStack(new ItemStack(stack.getItem(), 1), player.inventory);
                }
                catch (SchematicHandler.SchematicException e) {
                    e.printStackTrace();
                }


                return true;
            }
        }
        else if (player.capabilities.isCreativeMode) {
            if (tile.keyCode.value == 0)
                tile.keyCode.value = MathHelper.getInt(tile.getWorld().rand, 1, Integer.MAX_VALUE);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("KeyCode", tile.keyCode.value);
            stack.getTagCompound().setString("Bound", "Bound to [x:" + tile.getPos().getX() + " ,y:" + tile.getPos().getY() + " ,z:" + tile.getPos().getZ() + "]");
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileStructureBuilder tile = world.getTileEntity(pos) instanceof TileStructureBuilder ? (TileStructureBuilder) world.getTileEntity(pos) : null;
        if (tile == null) return;
        tile.signRotation.value = MathHelper.floor((double) ((placer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileStructureBuilder();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileStructureBuilder.class, new RenderTileStructureBuilder());
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return true;
    }
}
