package sladki.tfc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import sladki.tfc.Handlers.ChunkEventHandler;
import sladki.tfc.Handlers.EntityEventHandler;

@Mod(name = Cellars.MODNAME, modid = Cellars.MODID, version = Cellars.VERSION, dependencies = "after:terrafirmacraft")
public class Cellars {
	
	public static final String MODID = "tfccellars";
	public static final String MODNAME = "CellarsAddon";
	public static final String VERSION = "1.1";
	
	@Instance(Cellars.MODID)
    public static Cellars instance;
	
	@SidedProxy(clientSide = "sladki.tfc.ClientProxy", serverSide = "sladki.tfc.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.loadConfig(event);
	}
	
	@EventHandler
	public void initialize(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		
		ModManager.loadBlocks();
		ModManager.registerBlocks();
				
		ModManager.loadItems();
		ModManager.registerItems();
		
		ModManager.registerTileEntities();
		ModManager.registerRecipes();
		
		proxy.registerRenderInformation();
		proxy.registerGuiHandler();
		proxy.hideItemsNEI();
	}
	
}