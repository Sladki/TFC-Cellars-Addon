package sladki.tfc.Blocks;

import com.bioxx.tfc.Blocks.BlockTerra;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.TileEntities.TEBarrel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import sladki.tfc.Cellars;

public class BlockCellarWall extends BlockTerra {


	public BlockCellarWall(Material material) {
		super(material);
		this.setBlockTextureName(Cellars.MODID + ":" + "cellarWall");
		this.setCreativeTab(TFCTabs.TFC_BUILDING);
		this.setStepSound(Block.soundTypeWood);
	}
	
}