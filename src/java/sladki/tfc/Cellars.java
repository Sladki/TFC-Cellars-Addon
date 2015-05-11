package sladki.tfc;

import com.bioxx.tfc.TerraFirmaCraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import sladki.tfc.Handlers.ChunkEventHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import sladki.tfc.Handlers.InitWorldPacket;
import sladki.tfc.Handlers.PlayerTracker;

@Mod(name = Cellars.MODNAME, modid = Cellars.MODID, version = Cellars.VERSION, dependencies = "required-after:terrafirmacraft")
public class Cellars {
	
	public static final String MODID = "tfccellars";
	public static final String MODNAME = "CellarsAddon";
	public static final String VERSION = "1.06";
	
	@Instance("tfccellars")
    public static Cellars instance;
	
	@SidedProxy(clientSide = "sladki.tfc.ClientProxy", serverSide = "sladki.tfc.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.registerTickHandler();

	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {

		TerraFirmaCraft.packetPipeline.registerPacket(InitWorldPacket.class);
		FMLCommonHandler.instance().bus().register(new PlayerTracker());
		MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
		
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