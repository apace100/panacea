package io.github.apace100.panacea.block;

import io.github.apace100.panacea.block.entity.EnderAltarBlockEntity;
import io.github.apace100.panacea.component.AltarLookupComponent;
import io.github.apace100.panacea.registry.ModBlocks;
import io.github.apace100.panacea.registry.ModComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Random;

public class EnderAltarBlock extends BlockWithEntity {

    public EnderAltarBlock(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_RESPAWN_ANCHOR_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        addParticleAtPos(world, pos, random);

        BlockPos.Mutable mPos = pos.mutableCopy().move(Direction.NORTH, 2).move(Direction.WEST, 2);
        BlockState mState = world.getBlockState(mPos.up());
        if(mState.getBlock() != ModBlocks.ENDER_ALTAR_FRAME && !mState.isFullCube(world, mPos.up())) {
            addParticleAtPos(world, mPos, random);
        }
        mPos.move(Direction.EAST, 4);
        mState = world.getBlockState(mPos.up());
        if(mState.getBlock() != ModBlocks.ENDER_ALTAR_FRAME && !mState.isFullCube(world, mPos.up())) {
            addParticleAtPos(world, mPos, random);
        }
        mPos.move(Direction.SOUTH, 4);
        mState = world.getBlockState(mPos.up());
        if(mState.getBlock() != ModBlocks.ENDER_ALTAR_FRAME && !mState.isFullCube(world, mPos.up())) {
            addParticleAtPos(world, mPos, random);
        }
        mPos.move(Direction.WEST, 4);
        mState = world.getBlockState(mPos.up());
        if(mState.getBlock() != ModBlocks.ENDER_ALTAR_FRAME && !mState.isFullCube(world, mPos.up())) {
            addParticleAtPos(world, mPos, random);
        }
    }

    private void addParticleAtPos(World world, BlockPos pos, Random random) {
        double d = (double)pos.getX() + 0.5D + (double)(0.5F - random.nextFloat());
        double e = (double)pos.getY() + 1.0D;
        double f = (double)pos.getZ() + 0.5D + (double)(0.5F - random.nextFloat());
        double g = (double)random.nextFloat() * 0.04D;
        world.addParticle(ParticleTypes.REVERSE_PORTAL, d, e, f, 0.0D, g, 0.0D);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if(!world.isClient) {
            if (itemStack.hasCustomName()) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof EnderAltarBlockEntity) {
                    String altarId = itemStack.getName().getString();
                    ((EnderAltarBlockEntity)blockEntity).setAltarId(altarId);
                    AltarLookupComponent lookup = ModComponents.ALTAR_LOOKUP.get(world);
                    lookup.addAltar(pos, altarId);
                }
            } else {
                world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5F, Explosion.DestructionType.BREAK);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnderAltarBlockEntity && !world.isClient) {
            String altarId = ((EnderAltarBlockEntity)blockEntity).getAltarId();
            AltarLookupComponent lookup = ModComponents.ALTAR_LOOKUP.get(world);
            List<BlockPos> possibleDestinations = lookup.getAltarsExcluding(altarId, pos);
            if(possibleDestinations.size() > 0) {
                BlockPos destination = possibleDestinations.get(world.random.nextInt(possibleDestinations.size()));
                Vec3d offset = player.getPos().subtract(pos.getX(), pos.getY(), pos.getZ());
                Vec3d target = offset.add(destination.getX(), destination.getY(), destination.getZ());
                player.requestTeleport(target.getX(), target.getY(), target.getZ());
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new EnderAltarBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
