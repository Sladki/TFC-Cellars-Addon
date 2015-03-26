package sladki.tfc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sladki.tfc.Blocks.BlockCellarDoor;
import sladki.tfc.Blocks.BlockCellarShelf;
import sladki.tfc.Blocks.BlockCellarWall;
import sladki.tfc.Blocks.BlockIceBunker;
import sladki.tfc.Items.ItemCellarDoor;
import sladki.tfc.Items.ItemBlocks.ItemBlockCellarShelf;
import sladki.tfc.Items.ItemBlocks.ItemBlockCellarWall;
import sladki.tfc.Items.ItemBlocks.ItemBlockIceBunker;
import sladki.tfc.Items.Tools.ItemIceSaw;
import sladki.tfc.Items.Tools.ItemIceSawHead;

import com.bioxx.tfc.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;
import com.bioxx.tfc.api.Crafting.AnvilReq;
import com.bioxx.tfc.api.Crafting.PlanRecipe;
import com.bioxx.tfc.api.Enums.RuleEnum;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModManager {

	public static Block CellarWallBlock;
	public static Block IceBunkerBlock;
	public static Block CellarShelfBlock;
	public static Block CellarDoorBlock;
	
	public static Item CellarDoorItem;
	
	public static Item BismuthBronzeIceSawHead;
	public static Item BlackBronzeIceSawHead;
	public static Item BlackSteelIceSawHead;
	public static Item BlueSteelIceSawHead;
	public static Item BronzeIceSawHead;
	public static Item CopperIceSawHead;
	public static Item WroughtIronIceSawHead;
	public static Item RedSteelIceSawHead;
	public static Item SteelIceSawHead;
	
	public static Item BismuthBronzeIceSaw;
	public static Item BlackBronzeIceSaw;
	public static Item BlackSteelIceSaw;
	public static Item BlueSteelIceSaw;
	public static Item BronzeIceSaw;
	public static Item CopperIceSaw;
	public static Item WroughtIronIceSaw;
	public static Item RedSteelIceSaw;
	public static Item SteelIceSaw;
	
	public static void loadBlocks() {
		CellarWallBlock = new BlockCellarWall(Material.wood).setBlockName("CellarWall").setHardness(4);
		IceBunkerBlock = new BlockIceBunker(Material.wood).setBlockName("IceBunker").setHardness(5);
		CellarShelfBlock = new BlockCellarShelf(Material.wood).setBlockName("CellarShelf").setHardness(3);
		CellarDoorBlock = new BlockCellarDoor(Material.wood).setBlockName("CellarDoor").setHardness(4);
	}
	
	public static void registerBlocks() {
		GameRegistry.registerBlock(CellarWallBlock, ItemBlockCellarWall.class, "CellarWall");
		GameRegistry.registerBlock(IceBunkerBlock, ItemBlockIceBunker.class, "IceBunker");
		GameRegistry.registerBlock(CellarShelfBlock, ItemBlockCellarShelf.class, "CellarShelf");
		GameRegistry.registerBlock(CellarDoorBlock, "CellarDoor");
	}
	
	public static void loadItems() {
		CellarDoorItem = new ItemCellarDoor();
		
		BismuthBronzeIceSawHead = new ItemIceSawHead().setUnlocalizedName("BismuthBronzeIceSawBlade");
		BlackBronzeIceSawHead = new ItemIceSawHead().setUnlocalizedName("BlackBronzeIceSawBlade");
		BlackSteelIceSawHead = new ItemIceSawHead().setUnlocalizedName("BlackSteelIceSawBlade");
		BlueSteelIceSawHead = new ItemIceSawHead().setUnlocalizedName("BlueSteelIceSawBlade");
		BronzeIceSawHead = new ItemIceSawHead().setUnlocalizedName("BronzeIceSawBlade");
		CopperIceSawHead = new ItemIceSawHead().setUnlocalizedName("CopperIceSawBlade");
		WroughtIronIceSawHead = new ItemIceSawHead().setUnlocalizedName("WroughtIronIceSawBlade");
		RedSteelIceSawHead = new ItemIceSawHead().setUnlocalizedName("RedSteelIceSawBlade");
		SteelIceSawHead = new ItemIceSawHead().setUnlocalizedName("SteelIceSawBlade");
		
		BismuthBronzeIceSaw = new ItemIceSaw(TFCItems.BismuthBronzeToolMaterial).setUnlocalizedName("BismuthBronzeIceSaw").setMaxDamage(TFCItems.BismuthBronzeUses);
		BlackBronzeIceSaw = new ItemIceSaw(TFCItems.BlackBronzeToolMaterial).setUnlocalizedName("BlackBronzeIceSaw").setMaxDamage(TFCItems.BlackBronzeUses);
		BlackSteelIceSaw = new ItemIceSaw(TFCItems.BlackSteelToolMaterial).setUnlocalizedName("BlackSteelIceSaw").setMaxDamage(TFCItems.BlackSteelUses);
		BlueSteelIceSaw = new ItemIceSaw(TFCItems.BlueSteelToolMaterial).setUnlocalizedName("BlueSteelIceSaw").setMaxDamage(TFCItems.BlueSteelUses);
		BronzeIceSaw = new ItemIceSaw(TFCItems.BronzeToolMaterial).setUnlocalizedName("BronzeIceSaw").setMaxDamage(TFCItems.BronzeUses);
		CopperIceSaw = new ItemIceSaw(TFCItems.CopperToolMaterial).setUnlocalizedName("CopperIceSaw").setMaxDamage(TFCItems.CopperUses);
		WroughtIronIceSaw = new ItemIceSaw(TFCItems.IronToolMaterial).setUnlocalizedName("WroughtIronIceSaw").setMaxDamage(TFCItems.WroughtIronUses);
		RedSteelIceSaw = new ItemIceSaw(TFCItems.RedSteelToolMaterial).setUnlocalizedName("RedSteelIceSaw").setMaxDamage(TFCItems.RedSteelUses);
		SteelIceSaw = new ItemIceSaw(TFCItems.SteelToolMaterial).setUnlocalizedName("SteelIceSaw").setMaxDamage(TFCItems.SteelUses);
	}
	
	public static void registerItems() {
		GameRegistry.registerItem(CellarDoorItem, "CellarDoorItem");
		
		GameRegistry.registerItem(BismuthBronzeIceSawHead, "BismuthBronzeIceSawHead");
		GameRegistry.registerItem(BlackBronzeIceSawHead, "BlackBronzeIceSawHead");
		GameRegistry.registerItem(BlackSteelIceSawHead, "BlackSteelIceSawHead");
		GameRegistry.registerItem(BlueSteelIceSawHead, "BlueSteelIceSawHead");
		GameRegistry.registerItem(BronzeIceSawHead, "BronzeIceSawHead");
		GameRegistry.registerItem(CopperIceSawHead, "CopperIceSawHead");
		GameRegistry.registerItem(WroughtIronIceSawHead, "WroughtIronIceSawHead");
		GameRegistry.registerItem(RedSteelIceSawHead, "RedSteelIceSawHead");
		GameRegistry.registerItem(SteelIceSawHead, "SteelIceSawHead");
		
		GameRegistry.registerItem(BismuthBronzeIceSaw, "BismuthBronzeIceSaw");
		GameRegistry.registerItem(BlackBronzeIceSaw, "BlackBronzeIceSaw");
		GameRegistry.registerItem(BlackSteelIceSaw, "BlackSteelIceSaw");
		GameRegistry.registerItem(BlueSteelIceSaw, "BlueSteelIceSaw");
		GameRegistry.registerItem(BronzeIceSaw, "BronzeIceSaw");
		GameRegistry.registerItem(CopperIceSaw, "CopperIceSaw");
		GameRegistry.registerItem(WroughtIronIceSaw, "WroughtIronIceSaw");
		GameRegistry.registerItem(RedSteelIceSaw, "RedSteelIceSaw");
		GameRegistry.registerItem(SteelIceSaw, "SteelIceSaw");
	}
	
	public static void registerRecipes() {
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CellarDoorItem, 1, 0), new Object[] {
			"PCP", "PSP", "PCP",
			Character.valueOf('P'), "woodLumber",
			Character.valueOf('C'),	new ItemStack(TFCItems.ClayBall),
			Character.valueOf('S'),	new ItemStack(TFCItems.Straw)
		}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CellarWallBlock, 1, 0), new Object[] {
			"PSP", "C C", "PSP",
			Character.valueOf('P'), "woodLumber",
			Character.valueOf('C'),	new ItemStack(TFCItems.ClayBall),
			Character.valueOf('S'),	new ItemStack(TFCItems.Straw)
		}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CellarWallBlock, 1, 0), new Object[] {
			"PCP", "S S", "PCP",
			Character.valueOf('P'), "woodLumber",
			Character.valueOf('C'),	new ItemStack(TFCItems.ClayBall),
			Character.valueOf('S'),	new ItemStack(TFCItems.Straw)
		}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(IceBunkerBlock, 1, 0), new Object[] {
			"P P", "W W", "P P",
			Character.valueOf('P'), "woodLumber",
			Character.valueOf('W'),	"plankWood"
		}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CellarShelfBlock, 1, 0), new Object[] {
			"P P", "PPP", "P P",
			Character.valueOf('P'), "woodLumber"
		}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BismuthBronzeIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(BismuthBronzeIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlackBronzeIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(BlackBronzeIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlackSteelIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(BlackSteelIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlueSteelIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(BlueSteelIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BronzeIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(BronzeIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CopperIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(CopperIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(WroughtIronIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(WroughtIronIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RedSteelIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(RedSteelIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteelIceSaw, 1), new Object[] { "#","I", Character.valueOf('#'),
			new ItemStack(SteelIceSawHead, 1, 0), Character.valueOf('I'), "stickWood"}));
		
		AnvilManager manager = AnvilManager.getInstance();
		manager.addPlan("iceSaw", new PlanRecipe(new RuleEnum[]{RuleEnum.DRAWNOTLAST, RuleEnum.UPSETSECONDFROMLAST, RuleEnum.HITLAST}));
		
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.BismuthBronzeIngot2x), null, "iceSaw", AnvilReq.BISMUTHBRONZE, new ItemStack(BismuthBronzeIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.BlackBronzeIngot2x), null, "iceSaw", AnvilReq.BLACKBRONZE, new ItemStack(BlackBronzeIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.BlackSteelIngot2x), null, "iceSaw", AnvilReq.BLACKSTEEL, new ItemStack(BlackSteelIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.BlueSteelIngot2x), null, "iceSaw", AnvilReq.BLUESTEEL, new ItemStack(BlueSteelIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.BronzeIngot2x), null, "iceSaw", AnvilReq.BRONZE, new ItemStack(BronzeIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.CopperIngot2x), null, "iceSaw", AnvilReq.COPPER, new ItemStack(CopperIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.WroughtIronIngot2x), null, "iceSaw", AnvilReq.WROUGHTIRON, new ItemStack(WroughtIronIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.RedSteelIngot2x), null, "iceSaw", AnvilReq.REDSTEEL, new ItemStack(RedSteelIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.SteelIngot2x), null, "iceSaw", AnvilReq.STEEL, new ItemStack(SteelIceSawHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
	}
	
}