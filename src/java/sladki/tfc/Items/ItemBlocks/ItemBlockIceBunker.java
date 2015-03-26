package sladki.tfc.Items.ItemBlocks;

import net.minecraft.block.Block;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;

public class ItemBlockIceBunker extends ItemTerraBlock {

	public ItemBlockIceBunker(Block block) {
		super(block);
		this.setCreativeTab(TFCTabs.TFCDevices);
	}
	
}
