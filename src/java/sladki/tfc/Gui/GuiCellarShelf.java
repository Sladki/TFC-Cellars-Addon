package sladki.tfc.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import sladki.tfc.Cellars;
import sladki.tfc.Containers.ContainerCellarShelf;
import sladki.tfc.TileEntities.TECellarShelf;

public class GuiCellarShelf extends GuiContainer {

	private static ResourceLocation texture = new ResourceLocation(Cellars.MODID , "textures/gui/gui_cellarShelf.png");
	private TECellarShelf cellarShelf;

	public GuiCellarShelf(InventoryPlayer inventoryPlayer, TECellarShelf tileEntity) {
		super(new ContainerCellarShelf(inventoryPlayer, tileEntity));
		
		cellarShelf = tileEntity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mx, int my) {
		String name;	
		name = StatCollector.translateToLocal("container.CellarShelf.name"); 

		fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		
		//Info "button"
		if(mx >= guiLeft + 5 && mx <= guiLeft + 15 && my >= guiTop + 5 && my <= guiTop + 15) {
			List<String> infoText = new ArrayList<String>();
			float temperature = cellarShelf.getTemperature();
			
			if(temperature == -1000) {
				infoText.add("[!] The shelf is not inside a cellar");
			} else {
				if(temperature < 0) {
					infoText.add("Temperature: below zero");
				} else {
					infoText.add("Temperature: " + String.format("%.2f", temperature));
				}
			}
			
			this.drawHoveringText(infoText, mx - guiLeft, my - guiTop + 15, this.fontRendererObj);
		}
	}
	
}
