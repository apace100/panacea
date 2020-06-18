package io.github.apace100.panacea.block;

import io.github.apace100.panacea.screen.TransmutationTableScreenHandler;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TransmutationTableBlock extends Block {
   private static final TranslatableText TITLE = new TranslatableText("container.transmutation_table");
   public static final DirectionProperty FACING;
   protected static final VoxelShape SHAPE_X;
   protected static final VoxelShape SHAPE_Z;

   public static Identifier[] TRANSMUTABLE = new Identifier[] {
       new Identifier("minecraft", "walls"),
       new Identifier("minecraft", "banners"),
       new Identifier("minecraft", "coals"),
       new Identifier("minecraft", "carpets"),
       new Identifier("minecraft", "buttons"),
       new Identifier("minecraft", "logs"),
       new Identifier("minecraft", "music_discs"),
       new Identifier("minecraft", "leaves"),
       new Identifier("minecraft", "beds"),
       new Identifier("minecraft", "boats"),
       new Identifier("minecraft", "doors"),
       new Identifier("minecraft", "fences"),
       new Identifier("minecraft", "fishes"),
       new Identifier("minecraft", "flowers"),
       new Identifier("minecraft", "furnace_materials"),
       new Identifier("minecraft", "gold_ores"),
       new Identifier("minecraft", "planks"),
       new Identifier("minecraft", "sand"),
       new Identifier("minecraft", "saplings"),
       new Identifier("minecraft", "signs"),
       new Identifier("minecraft", "slabs"),
       new Identifier("minecraft", "stairs"),
       new Identifier("minecraft", "stone_bricks"),
       new Identifier("minecraft", "trapdoors"),
       new Identifier("minecraft", "wool")
   };

   public TransmutationTableBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
   }

   public BlockState getPlacementState(ItemPlacementContext ctx) {
      return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      if (world.isClient) {
         return ActionResult.SUCCESS;
      } else {
         player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
         return ActionResult.CONSUME;
      }
   }

   public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
      return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
         return new TransmutationTableScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
      }, TITLE);
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

      return state.get(FACING).getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
   }

   public boolean hasSidedTransparency(BlockState state) {
      return true;
   }

   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }

   public BlockState rotate(BlockState state, BlockRotation rotation) {
      return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
   }

   public BlockState mirror(BlockState state, BlockMirror mirror) {
      return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }

   public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
      return false;
   }

   static {
      FACING = HorizontalFacingBlock.FACING;
      VoxelShape bottom_z = Block.createCuboidShape(2.0D, 0.0D, 4.0D, 14.0D, 3.0D, 12.0D);
      VoxelShape middle_z = Block.createCuboidShape(4.0D, 3.0D, 5.0D, 12.0D, 9.0D, 11.0D);
      VoxelShape top_z = Block.createCuboidShape(0.0D, 9.0D, 3.0D, 16.0D, 11.0D, 13.0D);
      VoxelShape bottom_x = Block.createCuboidShape(4.0D, 0.0D, 2.0D, 12.0D, 3.0D, 14.0D);
      VoxelShape middle_x = Block.createCuboidShape(5.0D, 3.0D, 4.0D, 11.0D, 9.0D, 12.0D);
      VoxelShape top_x = Block.createCuboidShape(3.0D, 9.0D, 0.0D, 13.0D, 11.0D, 16.0D);
      SHAPE_X = VoxelShapes.union(bottom_x, middle_x, top_x);
      SHAPE_Z = VoxelShapes.union(bottom_z, middle_z, top_z);
   }
}
