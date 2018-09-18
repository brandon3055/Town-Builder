package com.brandon3055.townbuilder.client;

import com.brandon3055.townbuilder.TBFeatures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 14/01/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {

        if (Minecraft.getMinecraft().player.capabilities.isCreativeMode && !Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty() && Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == TBFeatures.schematicTool) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ItemStack tool = player.getHeldItemMainhand();

            if (!tool.hasTagCompound()) return;

            int x1 = tool.getTagCompound().getInteger("Pos1X");
            int y1 = tool.getTagCompound().getInteger("Pos1Y");
            if (y1 == -1) return;
            int z1 = tool.getTagCompound().getInteger("Pos1Z");
            int x2 = tool.getTagCompound().getInteger("Pos2X");
            int y2 = tool.getTagCompound().getInteger("Pos2Y");
            int z2 = tool.getTagCompound().getInteger("Pos2Z");

            int ph;
            if (x2 < x1) {
                ph = x1;
                x1 = x2;
                x2 = ph;
            }
            if (y2 < y1) {
                ph = y1;
                y1 = y2;
                y2 = ph;
            }
            if (z2 < z1) {
                ph = z1;
                z1 = z2;
                z2 = ph;
            }

            int xSize = x2 - x1 + 1;
            int ySize = y2 - y1 + 1;
            int zSize = z2 - z1 + 1;

            double trX = x1 - player.prevPosX - (player.posX - player.prevPosX) * (double) event.getPartialTicks();
            double trY = y1 - player.prevPosY - (player.posY - player.prevPosY) * (double) event.getPartialTicks();
            double trZ = z1 - player.prevPosZ - (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();

//			int xSize = x2 - x1 + 1;
//			int ySize = y2 - y1 + 1;
//			int zSize = z2 - z1 + 1;


            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GlStateManager.disableAlpha();
            GlStateManager.disableCull();
            GlStateManager.depthMask(false);

            GlStateManager.pushMatrix();

            GlStateManager.translate(trX, trY, trZ);

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

//			tess.startDrawingQuads();
//			tess.setColorRGBA_F(1f, 0f, 0f, 0.5f);

            {//main Cube
                buffer.pos(0, 0, 0).color(1f, 0f, 0f, 0.5f).endVertex();
                buffer.pos(0, ySize, 0).color(1f, 0f, 0f, 0.5f).endVertex();
                buffer.pos(xSize, ySize, 0).color(1f, 0f, 0f, 0.5f).endVertex();
                buffer.pos(xSize, 0, 0).color(1f, 0f, 0f, 0.5f).endVertex();

//				buffer.setColorRGBA_F(1f, 1f, 0f, 0.5f);

                buffer.pos(0, 0, 0).color(1f, 1f, 0f, 0.5f).endVertex();
                buffer.pos(0, 0, zSize).color(1f, 1f, 0f, 0.5f).endVertex();
                buffer.pos(0, ySize, zSize).color(1f, 1f, 0f, 0.5f).endVertex();
                buffer.pos(0, ySize, 0).color(1f, 1f, 0f, 0.5f).endVertex();

//				buffer.setColorRGBA_F(1f, 0f, 1f, 0.5f);

                buffer.pos(xSize, 0, 0).color(1f, 0f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, ySize, 0).color(1f, 0f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, ySize, zSize).color(1f, 0f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, 0, zSize).color(1f, 0f, 1f, 0.5f).endVertex();

//				buffer.setColorRGBA_F(0f, 1f, 1f, 0.5f);

                buffer.pos(0, 0, zSize).color(0f, 1f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, 0, zSize).color(0f, 1f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, ySize, zSize).color(0f, 1f, 1f, 0.5f).endVertex();
                buffer.pos(0, ySize, zSize).color(0f, 1f, 1f, 0.5f).endVertex();

//				buffer.setColorRGBA_F(0f, 1f, 0f, 0.5f);

                buffer.pos(0, 0, 0).color(0f, 1f, 0f, 0.5f).endVertex();
                buffer.pos(xSize, 0, 0).color(0f, 1f, 0f, 0.5f).endVertex();
                buffer.pos(xSize, 0, zSize).color(0f, 1f, 0f, 0.5f).endVertex();
                buffer.pos(0, 0, zSize).color(0f, 1f, 0f, 0.5f).endVertex();

//				buffer.setColorRGBA_F(0f, 0f, 1f, 0.5f);

                buffer.pos(0, ySize, 0).color(0f, 0f, 1f, 0.5f).endVertex();
                buffer.pos(0, ySize, zSize).color(0f, 0f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, ySize, zSize).color(0f, 0f, 1f, 0.5f).endVertex();
                buffer.pos(xSize, ySize, 0).color(0f, 0f, 1f, 0.5f).endVertex();
            }
            tess.draw();

            x1 = tool.getTagCompound().getInteger("Pos1X");
            y1 = tool.getTagCompound().getInteger("Pos1Y");
            z1 = tool.getTagCompound().getInteger("Pos1Z");
            x2 = tool.getTagCompound().getInteger("Pos2X");
            y2 = tool.getTagCompound().getInteger("Pos2Y");
            z2 = tool.getTagCompound().getInteger("Pos2Z");

            xSize = x2 - x1 + 1;
            ySize = y2 - y1 + 1;
            zSize = z2 - z1 + 1;

            trX = x1 - player.prevPosX - (player.posX - player.prevPosX) * (double) event.getPartialTicks();
            trY = y1 - player.prevPosY - (player.posY - player.prevPosY) * (double) event.getPartialTicks();
            trZ = z1 - player.prevPosZ - (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();


            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();

            GlStateManager.translate(trX, trY, trZ);

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            {//Pos1
//				tess.setColorRGBA_F(0f, 0.5f, 0f, 0.8f);

                buffer.pos(0, 0, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 1, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 1, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 0, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();

                buffer.pos(0, 0, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 0, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 1, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 1, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();

                buffer.pos(1, 0, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 1, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 1, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 0, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();

                buffer.pos(0, 0, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 0, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 1, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 1, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();

                buffer.pos(0, 0, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 0, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 0, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 0, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();

                buffer.pos(0, 1, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(0, 1, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 1, 1).color(0f, 0.5f, 0f, 0.8f).endVertex();
                buffer.pos(1, 1, 0).color(0f, 0.5f, 0f, 0.8f).endVertex();
            }
            {//Pos2
//				tess.setColorRGBA_F(0f, 0f, 0.5f, 0.8f);

                buffer.pos(xSize - 1, ySize - 1, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize - 1, zSize - 1).color(0f, 0f, 0.5f, 0.8f);

                buffer.pos(xSize - 1, ySize - 1, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize - 1, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize, zSize - 1).color(0f, 0f, 0.5f, 0.8f);

                buffer.pos(xSize, ySize - 1, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize - 1, zSize).color(0f, 0f, 0.5f, 0.8f);

                buffer.pos(xSize - 1, ySize - 1, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize - 1, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize, zSize).color(0f, 0f, 0.5f, 0.8f);

                buffer.pos(xSize - 1, ySize - 1, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize - 1, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize - 1, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize - 1, zSize).color(0f, 0f, 0.5f, 0.8f);

                buffer.pos(xSize - 1, ySize, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize - 1, ySize, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize, zSize).color(0f, 0f, 0.5f, 0.8f);
                buffer.pos(xSize, ySize, zSize - 1).color(0f, 0f, 0.5f, 0.8f);
            }
            tess.draw();

            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.shadeModel(GL11.GL_FLAT);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
        }
    }
}
