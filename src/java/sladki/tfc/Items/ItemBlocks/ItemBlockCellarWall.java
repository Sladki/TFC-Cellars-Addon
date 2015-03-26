package sladki.tfc.Items.ItemBlocks;

import net.minecraft.block.Block;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;

public class ItemBlockCellarWall extends ItemTerraBlock {

	public ItemBlockCellarWall(Block block) {
		super(block);
		this.setCreativeTab(TFCTabs.TFCBuilding);
	}
	
}
