package com.brandon3055.townbuilder;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ConfigHandler {
	public static Configuration config;

	public static int filePort;

	public static void init(Configuration configuration) {
		if (config == null) {
			config = configuration;
			syncConfig();
		}
	}

	public static void syncConfig() {

		try {
//			if (TownBuilder.proxy.isDedicatedServer()) filePort = config.get(Configuration.CATEGORY_GENERAL, "Schematic upload port", 25570, "This is the ports that will be used to upload schematic files to the server").getInt();
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

