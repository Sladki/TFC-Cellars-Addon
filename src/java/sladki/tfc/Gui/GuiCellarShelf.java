package sladki.tfc.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import sladki.tfc.Cellars;
import sladki.tfc.Containers.ContainerCellarShelf;
import sladki.tfc.TileEntities.TECellarShelf;

public class GuiCellarShelf extends GuiContainer {

	private static ResourceLocation texture = new ResourceLocation(Cellars.MODID , "textures/gui/gui_cellarShelf.png");

	public GuiCellarShelf(InventoryPlayer inventoryPlayer, TECellarShelf tileEntity) {
		super(new ContainerCellarShelf(inventoryPlayer, tileEntity));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
}
