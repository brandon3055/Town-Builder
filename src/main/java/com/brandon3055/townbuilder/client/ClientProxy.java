package com.brandon3055.townbuilder.client;

import com.brandon3055.townbuilder.CommonProxy;
import com.brandon3055.townbuilder.client.rendering.RenderTileStructureBuilder;
import com.brandon3055.townbuilder.schematics.FileSender;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRendering() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileStructureBuilder.class, new RenderTileStructureBuilder());
	}

	@Override
	public void registerListeners() {
		super.registerListeners();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}


	public boolean isOp(String paramString)
	{
		return Minecraft.getMinecraft().theWorld.getWorldInfo().getGameType().isCreative();
	}

	@Override
	public boolean isDedicatedServer() {
		return false;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void sendFile(String file, int port) {
		FileSender.instance.sendFile(file, port);
	}

	@Override
	public void receiveFile(String fileName, NetHandlerPlayServer netHandler) {

	}

	@Override
	public boolean isTransferInProgress() {
		return false;
	}
}
