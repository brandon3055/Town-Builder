package com.brandon3055.townbuilder;

import com.brandon3055.townbuilder.network.PacketClientList;
import com.brandon3055.townbuilder.network.PacketFileTransfer;
import com.brandon3055.townbuilder.network.PacketSchematicClient;
import com.brandon3055.townbuilder.schematics.FileReceiver;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Brandon on 13/01/2015.
 */
public class CommonProxy {

	public void registerRendering(){

	}

	public void registerListeners(){
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileStructureBuilder.class, TownBuilder.RPREFIX + "TileStructureBuilder");
	}

	public void initializeNetwork() {
		TownBuilder.network = NetworkRegistry.INSTANCE.newSimpleChannel(TownBuilder.networkChannelName);
		TownBuilder.network.registerMessage(PacketClientList.Handler.class, PacketClientList.class, 0, Side.CLIENT);
		TownBuilder.network.registerMessage(PacketFileTransfer.Handler.class, PacketFileTransfer.class, 1, Side.CLIENT);
		TownBuilder.network.registerMessage(PacketFileTransfer.Handler.class, PacketFileTransfer.class, 2, Side.SERVER);
		TownBuilder.network.registerMessage(PacketSchematicClient.Handler.class, PacketSchematicClient.class, 3, Side.CLIENT);
	}

	public boolean isOp(String paramString)
	{
		MinecraftServer localMinecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		paramString = paramString.trim();
		for (String str : localMinecraftServer.getConfigurationManager().func_152606_n()) {
			if (paramString.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDedicatedServer()
	{
		return true;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	public void sendFile(String file, int port)
	{

	}

	public void receiveFile(String fileName, NetHandlerPlayServer netHandler)
	{
		FileReceiver.instance.receiveFile(fileName, netHandler);
	}

	public boolean isTransferInProgress() { return FileReceiver.instance.getTransferInProgress(); }
}
