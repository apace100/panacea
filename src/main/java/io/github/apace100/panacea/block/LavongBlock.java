package io.github.apace100.panacea.block;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class LavongBlock extends Block implements Fertilizable {
	public static final IntProperty AGE = Properties.AGE_2;

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			VoxelShapes.cuboid(6.0D / 16D, 10.0D / 16D, 6.0D / 16D, 10.0D / 16D, 16.0D / 16D, 10.0D / 16D),
			VoxelShapes.cuboid(5.0D / 16D, 8.0D / 16D, 5.0D / 16D, 11.0D / 16D, 16.0D / 16D, 11.0D / 16D),
			VoxelShapes.cuboid(4.0D / 16D, 6.0D / 16D, 4.0D / 16D, 12.0D / 16D, 16.0D / 16D, 12.0D / 16D)
	};
	
	public LavongBlock(Block.Settings settings) {
		super(settings);
		this.setDefaultState(getStateManager().getDefaultState().with(AGE, 0));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_AGE[state.get(AGE)];
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if(!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!canPlaceAt(state, world, pos)) {
			world.breakBlock(pos, true);
		} else if (!isMaxAge(state)) {
			if (random.nextFloat() < getGrowthChance(state, world, pos)) {
				grow(world, random, pos, state);
			}
		}
	}

	public float getGrowthChance(BlockState state, ServerWorld world, BlockPos pos) {
		BlockPos below = pos.down();
		while(!World.isHeightInvalid(below)) {
			if(FluidTags.LAVA.contains(world.getFluidState(below).getFluid())) {
				break;
			}
			if(!world.isAir(below)) {
				return 0F;
			}
			below = below.down();
		}
		if(World.isHeightInvalid(below)) {
			return 0F;
		}
		int lavaCount = 1;
		for(int dx = -1; dx <= 1; dx++) {
			for(int dz = -1; dz <= 1; dz++) {
				if(!World.isHeightInvalid(pos)) {
					if(FluidTags.LAVA.contains(world.getFluidState(below.add(dx, 0, dz)).getFluid())) {
						lavaCount++;
					}
				}
			}
		}
		return lavaCount * 0.03F;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
		//if(!BlockTags.STONE.contains(worldIn.getBlockState(pos.up()).getBlock())) {
		if(worldIn.getBlockState(pos.up()).getBlock() != Blocks.STONE) {
			return false;
		}
		BlockPos below = pos.down();
		while(!World.isHeightInvalid(below)) {
			if(FluidTags.LAVA.contains(worldIn.getFluidState(below).getFluid())) {
				break;
			}
			if(!worldIn.isAir(below)) {
				return false;
			}
			below = below.down();
		}
		if(World.isHeightInvalid(below)) {
			return false;
		}
		return pos.getY() - below.getY() <= 3;
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return !isMaxAge(state);
	}
	@Override
	public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
		int i = this.getAge(state) + 1;
		if (i > 2) {
			i = 2;
		}

		world.setBlockState(pos, this.withAge(i), 2);
	}

	public IntProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return 2;
	}

	protected int getAge(BlockState state) {
		return state.get(this.getAgeProperty());
	}

	public BlockState withAge(int age) {
		return this.getDefaultState().with(this.getAgeProperty(), Integer.valueOf(age));
	}

	public boolean isMaxAge(BlockState state) {
		return state.get(this.getAgeProperty()) >= this.getMaxAge();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty());
	}

}
