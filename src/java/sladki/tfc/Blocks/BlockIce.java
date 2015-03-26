package sladki.tfc.Blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.bioxx.tfc.Blocks.Vanilla.BlockCustomIce;

public class BlockIce extends BlockCustomIce {

	public BlockIce() {
		super();
	}
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return world.setBlock(x, y, z, Blocks.air, 0, 2);
	}
	
	@Override
	protected Block getBlockMelt(World world, int i, int j, int k, boolean moving) {
		int meta = world.getBlockMetadata(i,j,k);
		Block block = world.getBlock(i,j,k);
		
		if(block != this) {
			return block;
		}
			
		return Blocks.air;
	}
	
}
