package sladki.tfc.Render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import sladki.tfc.Blocks.BlockCellarShelf;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderCellarShelf implements ISimpleBlockRenderingHandler {
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)	{
		renderer.overrideBlockTexture = BlockCellarShelf.textureSide;
		//Columns
		renderer.setRenderBounds(0.0f, 0.5f, 0.0f, 0.125f, 1.0f, 0.125f); //nw
		renderInvBlock(block, renderer);
		renderer.setRenderBounds(0.875f, 0.5f, 0.0f, 1.0f, 1.0f, 0.125f); //ne
		renderInvBlock(block, renderer);
		renderer.setRenderBounds(0.875f, 0.5f, 0.875f, 1.0f, 1.0f, 1.0f); //se
		renderInvBlock(block, renderer);
		renderer.setRenderBounds(0.0f, 0.5f, 0.875f, 0.125f, 1.0f, 1.0f); //sw
		renderInvBlock(block, renderer);
		//Bottom
		renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
		renderInvBlock(block, renderer);
		renderer.clearOverrideBlockTexture();
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderAllFaces = true;
		//Columns
		renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 0.125f, 1.0f, 0.125f); //nw
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.setRenderBounds(0.875f, 0.0f, 0.0f, 1.0f, 1.0f, 0.125f); //ne
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.setRenderBounds(0.875f, 0.0f, 0.875f, 1.0f, 1.0f, 1.0f); //se
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.setRenderBounds(0.0f, 0.0f, 0.875f, 0.125f, 1.0f, 1.0f); //sw
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		//Sides
		renderer.setRenderBounds(0.125f, 0.0625f, 0.03125f, 0.875f, 0.5f, 0.09375f); //n
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.setRenderBounds(0.125f, 0.0625f, 0.90625f, 0.875f, 0.5f, 0.96875f); //s
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.setRenderBounds(0.03125f, 0.0625f, 0.125f, 0.09375f, 0.5f, 0.875f); //w
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.setRenderBounds(0.90625f, 0.0625f, 0.125f, 0.96875f, 0.5f, 0.875f); //e
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		//Bottom
		renderer.setRenderBounds(0.0625f, 0.0625f, 0.0625f, 0.9375f, 0.125f, 0.9375f);
		renderer.renderStandardBlock(Blocks.planks, x, y, z);
		renderer.renderAllFaces = false;
		return true;
	}
	
	public void rotate(RenderBlocks renderer, int i) {
		renderer.uvRotateEast = i;
		renderer.uvRotateWest = i;
		renderer.uvRotateNorth = i;
		renderer.uvRotateSouth = i;
	}
		
	@Override
	public int getRenderId() {
		return 0;
	}
	
	public static void renderInvBlock(Block block, RenderBlocks renderer) {
		Tessellator var14 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		var14.startDrawingQuads();
		var14.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 2));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 2));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 1));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 3));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 0));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 0));
		var14.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
}
