package sladki.tfc.Blocks;

import java.util.Random;

import com.bioxx.tfc.Blocks.Vanilla.BlockCustomIce;
import com.bioxx.tfc.Core.TFC_Climate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockIce extends BlockCustomIce {

	public BlockIce() {
		super();
	}
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {		
		return world.setBlock(x, y, z, Blocks.air, 0, 2);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if(TFC_Climate.getHeightAdjustedTemp(world, x, y, z) > 3) {
			world.setBlockToAir(x, y, z);
		}
	}
	
}