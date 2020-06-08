package io.github.apace100.panacea.block;

import io.github.apace100.panacea.block.entity.EnderAltarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AltarFrameBlock extends Block {
    public static final DirectionProperty FACING;
    public static final BooleanProperty EYE;
    protected static final VoxelShape FRAME_SHAPE;
    protected static final VoxelShape EYE_SHAPE;
    protected static final VoxelShape FRAME_WITH_EYE_SHAPE;
    private static BlockPattern COMPLETED_FRAME;

    public AltarFrameBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(EYE, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if(stack != null) {
            if(!(Boolean)state.get(EYE) && stack.getItem() == Items.ENDER_EYE) {
                if(!world.isClient()) {
                    world.setBlockState(pos, state.with(EYE, true));
                    tryActivateAltar(world, pos);
                    if(!player.isCreative()) {
                        stack.decrement(1);
                    }
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public static void popEye(BlockState state, World world, BlockPos pos) {
        if(state.get(EYE)) {
            world.setBlockState(pos, state.with(EYE, false));
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ());
            item.setVelocity(world.random.nextGaussian(), 0.5, world.random.nextGaussian());
            world.spawnEntity(item);
        }
    }

    public static void consume(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(EYE, false));
    }

    private void tryActivateAltar(World world, BlockPos pos) {
        for(int i = 0; i < 4; i++) {
            if(tryActivateAltar(world, pos, Direction.fromHorizontal(i))) {
                break;
            }
        }
    }

    private boolean tryActivateAltar(World world, BlockPos pos, Direction direction) {
        BlockPos p = pos.offset(direction, 2).offset(direction.rotateYClockwise(), 2).offset(Direction.DOWN);
        BlockEntity be = world.getBlockEntity(p);
        if(be instanceof EnderAltarBlockEntity) {
            return ((EnderAltarBlockEntity)be).tryActivate();
        }
        return false;
    }

    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (Boolean)state.get(EYE) ? FRAME_WITH_EYE_SHAPE : FRAME_SHAPE;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)((BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite())).with(EYE, false);
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return (Boolean)state.get(EYE) ? 15 : 0;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, EYE);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        EYE = Properties.EYE;
        FRAME_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
        EYE_SHAPE = Block.createCuboidShape(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
        FRAME_WITH_EYE_SHAPE = VoxelShapes.union(FRAME_SHAPE, EYE_SHAPE);
    }
}
