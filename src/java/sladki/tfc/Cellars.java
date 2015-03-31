package sladki.tfc;

import net.minecraftforge.common.MinecraftForge;
import sladki.tfc.Handlers.ChunkEventHandler;
import sladki.tfc.Handlers.Network.InitClientWorldPacket;

import com.bioxx.tfc.TerraFirmaCraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(name = Cellars.MODNAME, modid = Cellars.MODID, version = Cellars.VERSION, dependencies = "required-after:terrafirmacraft")
public class Cellars {
	
	public static final String MODID = "tfccellars";
	public static final String MODNAME = "CellarsAddon";
	public static final String VERSION = "1.0";
	
	@Instance("tfccellars")
    public static Cellars instance;
	
	@SidedProxy(clientSide = "sladki.tfc.ClientProxy", serverSide = "sladki.tfc.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLInitializationEvent event) {
		proxy.registerTickHandler();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TerraFirmaCraft.packetPipeline.registerPacket(InitClientWorldPacket.class);
		MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());

		ModManager.loadBlocks();
		ModManager.loadItems();
		
		ModManager.registerBlocks();
		ModManager.registerItems();
		ModManager.registerRecipes();
		
		proxy.init();
		proxy.registerRenderInformation();
		
		//Use this to remove the door block from NEI
		proxy.tweakNEI();
	}

}