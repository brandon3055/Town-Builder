package com.brandon3055.townbuilder.client.rendering;

import com.brandon3055.townbuilder.tileentity.TileStructureBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 22/02/2015.
 */
public class RenderTileStructureBuilder extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partial) {
		if (!(tileEntity instanceof TileStructureBuilder) || !((TileStructureBuilder) tileEntity).showPosition) return;
		TileStructureBuilder tile = (TileStructureBuilder)tileEntity;

		int xSize = tile.xSize;
		int ySize = tile.ySize;
		int zSize = tile.zSize;



		Tessellator tess = Tessellator.instance;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		GL11.glTranslated(tile.xOffset, tile.yOffset, tile.zOffset);

		tess.startDrawingQuads();
		tess.setBrightness(200);

		{//main Cube
			tess.setColorRGBA_F(0f, 1f, 0f, 0.5f);

			tess.addVertex(0, 0, 0);
			tess.addVertex(0, ySize, 0);
			tess.addVertex(xSize, ySize, 0);
			tess.addVertex(xSize, 0, 0);

			tess.setColorRGBA_F(0f, 0f, 1f, 0.5f);

			tess.addVertex(0, 0, 0);
			tess.addVertex(0, 0, zSize);
			tess.addVertex(0, ySize, zSize);
			tess.addVertex(0, ySize, 0);

			tess.setColorRGBA_F(0f, 0f, 1f, 0.5f);

			tess.addVertex(xSize, 0, 0);
			tess.addVertex(xSize, ySize, 0);
			tess.addVertex(xSize, ySize, zSize);
			tess.addVertex(xSize, 0, zSize);

			tess.setColorRGBA_F(0f, 1f, 0.4f, 0.5f);

			tess.addVertex(0, 0, zSize);
			tess.addVertex(xSize, 0, zSize);
			tess.addVertex(xSize, ySize, zSize);
			tess.addVertex(0, ySize, zSize);

			tess.setColorRGBA_F(1f, 0f, 0f, 0.5f);

			tess.addVertex(0, 0, 0);
			tess.addVertex(xSize, 0, 0);
			tess.addVertex(xSize, 0, zSize);
			tess.addVertex(0, 0, zSize);

			tess.setColorRGBA_F(1f, 0f, 0f, 0.5f);

			tess.addVertex(0, ySize, 0);
			tess.addVertex(0, ySize, zSize);
			tess.addVertex(xSize, ySize, zSize);
			tess.addVertex(xSize, ySize, 0);
		}
		tess.draw();

		GL11.glPopMatrix();

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

	}
}
