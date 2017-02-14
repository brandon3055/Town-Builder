package com.brandon3055.townbuilder.client.rendering;

import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 22/02/2015.
 */
public class RenderTileStructureBuilder extends TileEntitySpecialRenderer<TileStructureBuilder> {

	@Override
	public void renderTileEntityAt(TileStructureBuilder te, double x, double y, double z, float partialTicks, int destroyStage) {
		if (!te.showPosition.value) {
			return;
		}

		int xSize = te.xSize.value;
		int ySize = te.ySize.value;
		int zSize = te.zSize.value;

		Tessellator tess = Tessellator.getInstance();

		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
//		GlStateManager.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.disableLighting();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		GlStateManager.translate(te.xOffset.value, te.yOffset.value, te.zOffset.value);

		VertexBuffer buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

//		tess.startDrawingQuads();
//		tess.setBrightness(200);

		{//main Cube
//			tess.setColorRGBA_F(0f, 1f, 0f, 0.5f);

			buffer.pos(0, 0, 0).color(0f, 1f, 0f, 0.5f).endVertex();
			buffer.pos(0, ySize, 0).color(0f, 1f, 0f, 0.5f).endVertex();
			buffer.pos(xSize, ySize, 0).color(0f, 1f, 0f, 0.5f).endVertex();
			buffer.pos(xSize, 0, 0).color(0f, 1f, 0f, 0.5f).endVertex();

//			buffer.posGBA_F(0f, 0f, 1f, 0.5f);

			buffer.pos(0, 0, 0).color(0f, 0f, 1f, 0.5f).endVertex();
			buffer.pos(0, 0, zSize).color(0f, 0f, 1f, 0.5f).endVertex();
			buffer.pos(0, ySize, zSize).color(0f, 0f, 1f, 0.5f).endVertex();
			buffer.pos(0, ySize, 0).color(0f, 0f, 1f, 0.5f).endVertex();

//			buffer.posGBA_F(0f, 0f, 1f, 0.5f);

			buffer.pos(xSize, 0, 0).color(0f, 0f, 1f, 0.5f).endVertex();
			buffer.pos(xSize, ySize, 0).color(0f, 0f, 1f, 0.5f).endVertex();
			buffer.pos(xSize, ySize, zSize).color(0f, 0f, 1f, 0.5f).endVertex();
			buffer.pos(xSize, 0, zSize).color(0f, 0f, 1f, 0.5f).endVertex();

//			buffer.posGBA_F(0f, 1f, 0.4f, 0.5f);

			buffer.pos(0, 0, zSize).color(0f, 1f, 0.4f, 0.5f).endVertex();
			buffer.pos(xSize, 0, zSize).color(0f, 1f, 0.4f, 0.5f).endVertex();
			buffer.pos(xSize, ySize, zSize).color(0f, 1f, 0.4f, 0.5f).endVertex();
			buffer.pos(0, ySize, zSize).color(0f, 1f, 0.4f, 0.5f).endVertex();

//			buffer.posGBA_F(1f, 0f, 0f, 0.5f);

			buffer.pos(0, 0, 0).color(1f, 0f, 0f, 0.5f).endVertex();
			buffer.pos(xSize, 0, 0).color(1f, 0f, 0f, 0.5f).endVertex();
			buffer.pos(xSize, 0, zSize).color(1f, 0f, 0f, 0.5f).endVertex();
			buffer.pos(0, 0, zSize).color(1f, 0f, 0f, 0.5f).endVertex();

//			buffer.posGBA_F(1f, 0f, 0f, 0.5f);

			buffer.pos(0, ySize, 0).color(1f, 0f, 0f, 0.5f).endVertex();
			buffer.pos(0, ySize, zSize).color(1f, 0f, 0f, 0.5f).endVertex();
			buffer.pos(xSize, ySize, zSize).color(1f, 0f, 0f, 0.5f).endVertex();
			buffer.pos(xSize, ySize, 0).color(1f, 0f, 0f, 0.5f).endVertex();
		}
		tess.draw();

		GL11.glPopMatrix();

		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();

	}
}
