package sladki.tfc.Handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import sladki.tfc.ModManager;

public class EntityEventHandler {
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onSpawn(EntityJoinWorldEvent event) {
		if(event.world.isRemote) {
			return;
		}
		
		if(event.entity instanceof EntityMob) {
			Entity entity = event.entity;
			int blocks = 0;
			for(int y = -1; y <= 2; y++) {
				Block block = event.world.getBlock((int)(entity.posX - 0.5), (int)entity.posY + y, (int)(entity.posZ - 0.5));
				if(block == ModManager.CellarWallBlock) {
					blocks++;
				}
			}
			
			if(blocks > 1) {
				event.setCanceled(true);;
			}
			
			return;
		}

	}

}
