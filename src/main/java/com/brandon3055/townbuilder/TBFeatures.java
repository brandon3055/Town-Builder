package com.brandon3055.townbuilder;

import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IModFeatures;
import com.brandon3055.brandonscore.registry.ModFeature;
import com.brandon3055.brandonscore.registry.ModFeatures;
import com.brandon3055.townbuilder.blocks.StructureBuilder;
import com.brandon3055.townbuilder.items.HouseKey;
import com.brandon3055.townbuilder.items.SchematicTool;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import javax.annotation.Nullable;

/**
 * Created by Brandon on 13/01/2015.
 */
@ModFeatures(modid = TownBuilder.MODID)
public class TBFeatures implements IModFeatures {

    @ModFeature(name = "house_key", stateOverride = "items#type=house_key")
    public static Item houseKey = new HouseKey();

    @ModFeature(name = "schematic_tool", stateOverride = "items#type=schematic_tool")
    public static Item schematicTool = new SchematicTool();

    @ModFeature(name = "structure_builder", tileEntity = TileStructureBuilder.class, itemBlock = ItemBlockBCore.class)
    public static Block structureBuilder = new StructureBuilder();

    @Nullable
    @Override
    public CreativeTabs getCreativeTab(Feature feature) {
        return CreativeTabs.MISC;
    }
}
