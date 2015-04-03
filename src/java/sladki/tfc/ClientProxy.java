package sladki.tfc;

import net.minecraft.item.ItemStack;
import sladki.tfc.Blocks.BlockCellarShelf;
import sladki.tfc.Render.RenderCellarShelf;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRenderInformation()	{
		RenderingRegistry.registerBlockHandler(BlockCellarShelf.renderId = RenderingRegistry.getNextAvailableRenderId(), new RenderCellarShelf());
	}
	
	@Override
	public boolean isRemote () {
		return true;
	}
	
	@Override
	public void hideItemsNEI() {
		if(Loader.isModLoaded("NotEnoughItems")) {
			codechicken.nei.api.API.hideItem(new ItemStack(ModManager.CellarDoorBlock));
		}
	}
}