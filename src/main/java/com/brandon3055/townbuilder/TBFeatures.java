package com.brandon3055.townbuilder;

import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.config.Feature;
import com.brandon3055.townbuilder.blocks.StructureBuilder;
import com.brandon3055.townbuilder.items.HouseKey;
import com.brandon3055.townbuilder.items.SchematicTool;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Created by Brandon on 13/01/2015.
 */
public class TBFeatures {

	@Feature(registryName = "house_key", stateOverride = "items#type=house_key")
	public static Item houseKey = new HouseKey();

	@Feature(registryName = "schematic_tool", stateOverride = "items#type=schematic_tool")
	public static Item schematicTool = new SchematicTool();

	@Feature(registryName = "structure_builder", tileEntity = TileStructureBuilder.class, itemBlock = ItemBlockBCore.class)
	public static Block structureBuilder = new StructureBuilder();
	
}
