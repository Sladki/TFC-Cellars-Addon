package sladki.tfc.Blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sladki.tfc.Cellars;
import sladki.tfc.ModManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCellarDoor extends Block {
	
	private static IIcon textureSide;
	private static IIcon textureUpper;
	private static IIcon textureLower;
	private static IIcon textureUpperFlipped;
	private static IIcon textureLowerFlipped;

	public BlockCellarDoor(Material material) {
		super(material);
		this.setStepSound(Block.soundTypeWood);
		//this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if((meta & 8) == 0) {
			Block block = world.getBlock(x, y + 1, z);
			if(block != null && (block == ModManager.CellarDoorBlock || block == Blocks.air)) {
				drops.add(new ItemStack(ModManager.CellarDoorItem));
			}
		} else {
			Block block = world.getBlock(x, y - 1, z);
			if(block != null && (block == ModManager.CellarDoorBlock || block == Blocks.air)) {
				drops.add(new ItemStack(ModManager.CellarDoorItem));
			}
		}
		return drops;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return y >= 255 ? false : World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) &&
				super.canPlaceBlockAt(world, x, y, z) && 
				super.canPlaceBlockAt(world, x, y + 1, z);
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}
	
	@Override
	public int getRenderType() {
		return 7;
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, startVec, endVec);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int meta = world.getBlockMetadata(x, y, z);
		
		if((meta & 8) == 0) {
			
			if(world.getBlock(x, y + 1,z) != this) {
				world.setBlockToAir(x, y, z);
			}
			
			if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
				world.setBlockToAir(x, y, z);
				dropBlockAsItem(world, x, y, z, meta, 0);
				if(world.getBlock(x, y + 1, z) == this) {
					world.setBlockToAir(x, y + 1, z);
				}
			}
		} else {
			if(world.getBlock(x, y - 1, z) != this) {
				world.setBlockToAir(x, y, z);
			}
			
			if(block != this) {
				onNeighborBlockChange(world, x, y - 1, z, block);
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ) {
		int meta = getDoorMetadata(world, x, y, z);
		int metaResult = meta & 7;
		metaResult ^= 4;
		
		if((meta & 8) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, metaResult, 3);
			world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		} else {
			world.setBlockMetadataWithNotify(x, y - 1, z, metaResult, 3);
			world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
		}
		
		world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
		return true;
	}
	
	public int getDoorOrientation(IBlockAccess block, int x, int y, int z) {
		return getDoorMetadata(block, x, y, z) & 3;
	}
	
	public boolean isDoorOpen(IBlockAccess block, int x, int y, int z) {
		return (getDoorMetadata(block, x, y, z) & 4) != 0;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z) {
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
		int meta = getDoorMetadata(block, x, y, z);
		int orientation = meta & 3;
		
		boolean closed = (meta & 4) != 0;
		boolean hingeLeft = (meta & 16) != 0;
		
		if(orientation == 0) {
			if(closed) {
				if(hingeLeft) {
					this.setBlockBounds(0.0f, 0.0f, 0.8125f, 1.0f, 1.0f, 1.0f);
				} else {
					this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1875f);
				}
			} else {
				this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.1875f, 1.0f, 1.0f);
			}
		} else
		if(orientation == 1) {
			if(closed) {
				if(hingeLeft) {
					this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.1875f, 1.0f, 1.0f);
				} else {
					this.setBlockBounds(0.8125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
				}
			} else {
				this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1875f);
			}
		} else
		if(orientation == 2) {
			if(closed) {
				if(hingeLeft) {
					this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1875f);
				} else {
					this.setBlockBounds(0.0f, 0.0f, 0.8125f, 1.0f, 1.0f, 1.0f);
				}
			} else {
				this.setBlockBounds(0.8125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			}
		} else
		if(orientation == 3) {
			if(closed) {
				if(hingeLeft) {
					this.setBlockBounds(0.8125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
				} else {
					this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.1875f, 1.0f, 1.0f);
				}
			} else {
				this.setBlockBounds(0.0f, 0.0f, 0.8125f, 1.0f, 1.0f, 1.0f);
			}
		}
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(ModManager.CellarDoorItem);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean getBlocksMovement(IBlockAccess block, int x, int y, int z) {
		int meta = getDoorMetadata(block, x, y, z);
		return (meta & 4) != 0;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister registerer) {
		textureSide = registerer.registerIcon(Cellars.MODID + ":" + "cellarDoorSide");
		textureUpper = registerer.registerIcon(Cellars.MODID + ":" + "cellarDoorUpper");
		textureLower = registerer.registerIcon(Cellars.MODID + ":" + "cellarDoorLower");
		textureUpperFlipped = new IconFlipped(textureUpper, true, false);
		textureLowerFlipped = new IconFlipped(textureLower, true, false);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return textureUpper;
	}
	
	@Override
	public IIcon getIcon(IBlockAccess block, int x, int y, int z, int side) {
		if(side != 1 && side != 0) {
			int meta = getDoorMetadata(block, x, y, z);
			int orientation = meta & 3;
			
			boolean open = (meta & 4) != 0;
			boolean facing = false;
			boolean flipped = false;
			boolean hingeLeft = false;
			boolean upper = (meta & 8) != 0;
			
			if(open) {
				if(orientation == 0) {
					if(side == 3) {
						facing = true;
					} else if(side == 2) {
						facing = true;
						flipped = true;
					}
				} else if(orientation == 1) {
					if(side == 4) {
						facing = true;
					} else if(side == 5) {
						facing = true;
						flipped = true;
					}
				} else if(orientation == 2) {
					if(side == 2) {
						facing = true;
					} else if(side == 3) {
						facing = true;
						flipped = true;
					}
				} else if(orientation == 3) {
					if(side == 5) {
						facing = true;
					} else if(side == 4) {
						facing = true;
						flipped = true;
					}
				}
			} else {
				if(orientation == 0) {
					if(side == 4) {
						facing = true;
					} else if(side == 5) {
						facing = true;
						flipped = true;
					}
				} else if(orientation == 1) {
					if(side == 2) {
						facing = true;
					} else if(side == 3) {
						facing = true;
						flipped = true;
					}
				} else if(orientation == 2) {
					if(side == 5) {
						facing = true;
					} else if(side == 4) {
						facing = true;
						flipped = true;
					}
				} else if(orientation == 3) {
					if(side == 3) {
						facing = true;
					} else if(side == 2) {
						facing = true;
						flipped = true;
					}
				}	
				
				if((meta & 16) != 0) {
					flipped = !flipped;
				}
			}
			
			if(upper && facing) {
				if(flipped) {
					return textureUpperFlipped;
				}
				return textureUpper;
			} else if(!upper && facing) {
				if(flipped) {
					return textureLowerFlipped;
				}
				return textureLower;
			}
		}
		return textureSide;
	}
	
	private int getDoorMetadata(IBlockAccess block, int x, int y, int z) {
		int metaLower;
		int metaUpper;
		
		int meta = block.getBlockMetadata(x, y, z);
		boolean upper = (meta & 8) != 0;

		if(upper) {
			metaLower = block.getBlockMetadata(x, y - 1, z);
			metaUpper = meta;
		} else {
			metaUpper = block.getBlockMetadata(x, y + 1, z);
			metaLower = meta;
		}
		
		boolean hingeLeft = (metaUpper & 1) != 0;
		return metaLower & 7 | (upper ? 8 : 0) | (hingeLeft ? 16 : 0);
	}

}