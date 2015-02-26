package com.brandon3055.townbuilder;

import com.brandon3055.townbuilder.items.HouseKey;
import com.brandon3055.townbuilder.items.SchematicTool;
import net.minecraft.item.Item;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ModItems {

	public static Item houseKey;
	public static Item schematicTool;

	public static void init()
	{
		houseKey = new HouseKey();
		schematicTool = new SchematicTool();
	}
}
