package com.brandon3055.townbuilder;

import com.brandon3055.townbuilder.blocks.StructureBuilder;
import net.minecraft.block.Block;

/**
 * Created by Brandon on 24/01/2015.
 */
public class ModBlocks {

	public static Block structureBuilder;

	public static void init()
	{
		structureBuilder = new StructureBuilder();
	}
}
