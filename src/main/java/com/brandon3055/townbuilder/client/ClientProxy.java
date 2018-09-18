package com.brandon3055.townbuilder.client;

import com.brandon3055.townbuilder.CommonProxy;
import com.brandon3055.townbuilder.client.rendering.RenderTileStructureBuilder;
import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void registerRendering() {
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }


    public boolean isOp(String paramString) {
        return Minecraft.getMinecraft().world.getWorldInfo().getGameType().isCreative();
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void registerServerListeners() {

    }
}
