package com.brandon3055.townbuilder.utills;

import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Created by Brandon on 25/02/2015.
 */
public class Utills {

	public static final boolean isSinglePlayerServer()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance() != null;
	}

	public static final boolean isMultiPlayerServer()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance() == null;
	}

	public static String cutStringToLength(String s, int length)
	{
		return s.length() <= length ? s : s.substring(0, length);
	}
}
