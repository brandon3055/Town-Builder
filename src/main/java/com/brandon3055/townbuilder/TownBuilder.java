package com.brandon3055.townbuilder;

import com.brandon3055.townbuilder.schematics.SchematicHandler;
import com.brandon3055.townbuilder.schematics.commands.CommandHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.Arrays;

@Mod(modid = TownBuilder.MODID, name = TownBuilder.MODNAME,version = TownBuilder.VERSION, dependencies = "")
public class TownBuilder
{
    public static final String MODID = "TownBuilder";
	public static final String MODNAME = "Town Builder";
    public static final String RPREFIX = MODID.toLowerCase() + ":";
    public static final String VERSION = "1.0.0-Build1";

	public static final String networkChannelName = "TownBuilderC";
	public static SimpleNetworkWrapper network;

	@Mod.Instance(MODID)
    public static TownBuilder instance;

    @SidedProxy(clientSide = "com.brandon3055.townbuilder.client.ClientProxy", serverSide = "com.brandon3055.townbuilder.CommonProxy")
    public static CommonProxy proxy;

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		CommandHandler.init(event);
	}

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.init(event.getSuggestedConfigurationFile());

        event.getModMetadata().autogenerated = false;
        event.getModMetadata().credits = "";
        event.getModMetadata().description = "This is a small mod that adds the ability for map makers and server owners to create a plot system that allows players to bye houses and/or other structures";
        event.getModMetadata().authorList = Arrays.asList("brandon3055");
        event.getModMetadata().logoFile = "";
        event.getModMetadata().url = "http://www.tolkiencraft.com/";
        event.getModMetadata().version = VERSION + "-MC1.7.10";

        ModItems.init();
        ModBlocks.init();
        proxy.registerRendering();
        proxy.registerTileEntities();
		proxy.initializeNetwork();
		SchematicHandler.init(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {proxy.registerListeners();}
}