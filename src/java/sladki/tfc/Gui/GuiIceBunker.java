package sladki.tfc.Gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import sladki.tfc.Cellars;
import sladki.tfc.Containers.ContainerIceBunker;
import sladki.tfc.TileEntities.TEIceBunker;

public class GuiIceBunker extends GuiContainer {
	
	private static ResourceLocation texture = new ResourceLocation(Cellars.MODID , "textures/gui/gui_iceBunker.png");
	
	TEIceBunker iceBunker;

	public GuiIceBunker(InventoryPlayer inventoryPlayer, TEIceBunker tileEntity) {
		super(new ContainerIceBunker(inventoryPlayer, tileEntity));
		iceBunker = tileEntity;
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
		name = StatCollector.translateToLocal("container.IceBunker.name"); 

		fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		
		//Info "button"
		if(mx >= guiLeft + 5 && mx <= guiLeft + 15 && my >= guiTop + 5 && my <= guiTop + 15) {
			List<String> infoText = new ArrayList<String>();
			float temperature = iceBunker.getTemperature();
			
			if(temperature <= -1000) {
				switch((int)(temperature * 0.001f)) {
					case -1: infoText.add("[!] The cellar has a problem with walls or has no doors");
						break;
					case -2: infoText.add("[!] A block prevents the cellar from working correctly");
						break;
					case -3: infoText.add("[!] The cellar has no or more than one entrance");
						break;
				}
				infoText.add("Check the structure if the error will appear after 1 minute");
				infoText.add("or break and place the Ice Bunker block again");
				
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
