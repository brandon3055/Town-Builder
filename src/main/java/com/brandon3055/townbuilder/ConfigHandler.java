package com.brandon3055.townbuilder;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ConfigHandler {
	public static Configuration config;

	public static int filePort;

	public static void init(File confFile) {
		if (config == null) {
			config = new Configuration(confFile);
			syncConfig();
		}
	}

	public static void syncConfig() {

		try {
			if (TownBuilder.proxy.isDedicatedServer()) filePort = config.get(Configuration.CATEGORY_GENERAL, "Schematic upload port", 25570, "This is the ports that will be used to upload schematic files to the server").getInt();
		}
		catch (Exception e) {
			FMLLog.log(Level.ERROR, "Unable to load Config");
			e.printStackTrace();
		}
		finally {
			if (config.hasChanged()) config.save();
		}
	}
}

