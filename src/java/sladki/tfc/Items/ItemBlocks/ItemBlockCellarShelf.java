package sladki.tfc.Items.ItemBlocks;

import net.minecraft.block.Block;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;

public class ItemBlockCellarShelf extends ItemTerraBlock {

	public ItemBlockCellarShelf(Block block) {
		super(block);
		this.setCreativeTab(TFCTabs.TFC_DEVICES);
	}
	
}
