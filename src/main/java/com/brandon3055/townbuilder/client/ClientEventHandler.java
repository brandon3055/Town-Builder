package com.brandon3055.townbuilder.client;

import com.brandon3055.townbuilder.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 14/01/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void renderWorld(RenderWorldLastEvent event)
	{

		if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == ModItems.schematicTool)
		{
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			ItemStack tool = player.getHeldItem();

			if (!tool.hasTagCompound() ) return;

			int x1 = tool.getTagCompound().getInteger("Pos1X");
			int y1 = tool.getTagCompound().getInteger("Pos1Y");
			if (y1 == -1) return;
			int z1 = tool.getTagCompound().getInteger("Pos1Z");
			int x2 = tool.getTagCompound().getInteger("Pos2X");
			int y2 = tool.getTagCompound().getInteger("Pos2Y");
			int z2 = tool.getTagCompound().getInteger("Pos2Z");

			int ph;
			if (x2 < x1){ ph = x1; x1 = x2; x2 = ph; }
			if (y2 < y1){ ph = y1; y1 = y2; y2 = ph; }
			if (z2 < z1){ ph = z1; z1 = z2; z2 = ph; }

			int xSize = x2 - x1 + 1;
			int ySize = y2 - y1 + 1;
			int zSize = z2 - z1 + 1;

			double trX = x1 - player.prevPosX - (player.posX - player.prevPosX) * (double)event.partialTicks;
			double trY = y1 - player.prevPosY - (player.posY - player.prevPosY) * (double)event.partialTicks;
			double trZ = z1 - player.prevPosZ - (player.posZ - player.prevPosZ) * (double)event.partialTicks;

//			int xSize = x2 - x1 + 1;
//			int ySize = y2 - y1 + 1;
//			int zSize = z2 - z1 + 1;


			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDepthMask(false);

			GL11.glPushMatrix();

			GL11.glTranslated(trX, trY, trZ);

			Tessellator tess = Tessellator.instance;

			tess.startDrawingQuads();
			tess.setColorRGBA_F(1f, 0f, 0f, 0.5f);

			{//main Cube
				tess.addVertex(0, 0, 0);
				tess.addVertex(0, ySize, 0);
				tess.addVertex(xSize, ySize, 0);
				tess.addVertex(xSize, 0, 0);

				tess.setColorRGBA_F(1f, 1f, 0f, 0.5f);

				tess.addVertex(0, 0, 0);
				tess.addVertex(0, 0, zSize);
				tess.addVertex(0, ySize, zSize);
				tess.addVertex(0, ySize, 0);

				tess.setColorRGBA_F(1f, 0f, 1f, 0.5f);

				tess.addVertex(xSize, 0, 0);
				tess.addVertex(xSize, ySize, 0);
				tess.addVertex(xSize, ySize, zSize);
				tess.addVertex(xSize, 0, zSize);

				tess.setColorRGBA_F(0f, 1f, 1f, 0.5f);

				tess.addVertex(0, 0, zSize);
				tess.addVertex(xSize, 0, zSize);
				tess.addVertex(xSize, ySize, zSize);
				tess.addVertex(0, ySize, zSize);

				tess.setColorRGBA_F(0f, 1f, 0f, 0.5f);

				tess.addVertex(0, 0, 0);
				tess.addVertex(xSize, 0, 0);
				tess.addVertex(xSize, 0, zSize);
				tess.addVertex(0, 0, zSize);

				tess.setColorRGBA_F(0f, 0f, 1f, 0.5f);

				tess.addVertex(0, ySize, 0);
				tess.addVertex(0, ySize, zSize);
				tess.addVertex(xSize, ySize, zSize);
				tess.addVertex(xSize, ySize, 0);
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

			trX = x1 - player.prevPosX - (player.posX - player.prevPosX) * (double)event.partialTicks;
			trY = y1 - player.prevPosY - (player.posY - player.prevPosY) * (double)event.partialTicks;
			trZ = z1 - player.prevPosZ - (player.posZ - player.prevPosZ) * (double)event.partialTicks;


			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			GL11.glPushMatrix();

			GL11.glTranslated(trX, trY, trZ);

			tess.startDrawingQuads();
			{//Pos1
				tess.setColorRGBA_F(0f, 0.5f, 0f, 0.8f);

				tess.addVertex(0, 0, 0);
				tess.addVertex(0, 1, 0);
				tess.addVertex(1, 1, 0);
				tess.addVertex(1, 0, 0);

				tess.addVertex(0, 0, 0);
				tess.addVertex(0, 0, 1);
				tess.addVertex(0, 1, 1);
				tess.addVertex(0, 1, 0);

				tess.addVertex(1, 0, 0);
				tess.addVertex(1, 1, 0);
				tess.addVertex(1, 1, 1);
				tess.addVertex(1, 0, 1);

				tess.addVertex(0, 0, 1);
				tess.addVertex(1, 0, 1);
				tess.addVertex(1, 1, 1);
				tess.addVertex(0, 1, 1);

				tess.addVertex(0, 0, 0);
				tess.addVertex(1, 0, 0);
				tess.addVertex(1, 0, 1);
				tess.addVertex(0, 0, 1);

				tess.addVertex(0, 1, 0);
				tess.addVertex(0, 1, 1);
				tess.addVertex(1, 1, 1);
				tess.addVertex(1, 1, 0);
			}
			{//Pos2
				tess.setColorRGBA_F(0f, 0f, 0.5f, 0.8f);

				tess.addVertex(xSize-1, ySize-1, zSize-1);
				tess.addVertex(xSize-1, ySize, zSize-1);
				tess.addVertex(xSize, ySize, zSize-1);
				tess.addVertex(xSize, ySize-1, zSize-1);

				tess.addVertex(xSize-1, ySize-1, zSize-1);
				tess.addVertex(xSize-1, ySize-1, zSize);
				tess.addVertex(xSize-1, ySize, zSize);
				tess.addVertex(xSize-1, ySize, zSize-1);

				tess.addVertex(xSize, ySize-1, zSize-1);
				tess.addVertex(xSize, ySize, zSize-1);
				tess.addVertex(xSize, ySize, zSize);
				tess.addVertex(xSize, ySize-1, zSize);

				tess.addVertex(xSize-1, ySize-1, zSize);
				tess.addVertex(xSize, ySize-1, zSize);
				tess.addVertex(xSize, ySize, zSize);
				tess.addVertex(xSize-1, ySize, zSize);

				tess.addVertex(xSize-1, ySize-1, zSize-1);
				tess.addVertex(xSize, ySize-1, zSize-1);
				tess.addVertex(xSize, ySize-1, zSize);
				tess.addVertex(xSize-1, ySize-1, zSize);

				tess.addVertex(xSize-1, ySize, zSize-1);
				tess.addVertex(xSize-1, ySize, zSize);
				tess.addVertex(xSize, ySize, zSize);
				tess.addVertex(xSize, ySize, zSize-1);
			}
			tess.draw();

			GL11.glPopMatrix();
			GL11.glDepthMask(true);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}
}
