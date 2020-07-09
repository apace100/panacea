package io.github.apace100.panacea.world;

import com.mojang.serialization.Codec;
import io.github.apace100.panacea.Panacea;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class MagmaTroveFeature extends Feature<DefaultFeatureConfig> {

    public static final Identifier MAGMA_TROVE_CHEST = new Identifier(Panacea.MODID, "chests/magma_trove");

    public MagmaTroveFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockState magma = Blocks.MAGMA_BLOCK.getDefaultState();
        BlockState air = Blocks.AIR.getDefaultState();
        BlockState basalt = Blocks.BASALT.getDefaultState();
        int w = 10;
        int h = 8;
        if(random.nextInt(40) == 0) {
            for(int tries = 0; tries < 16; tries++) {
                BlockPos center = new BlockPos(pos).add(random.nextInt(16), random.nextInt(100) + 16, random.nextInt(16));
                BlockPos min = center.add(-w / 2, -1, -w / 2);
                BlockPos max = center.add(w / 2, h - 1, w / 2);
                if(isPositionValid(world, min, max)) {
                    BlockPos.Mutable q = new BlockPos.Mutable();
                    for(int x = min.getX(); x <= max.getX(); x++) {
                        for(int y = min.getY(); y <= max.getY(); y++) {
                            for(int z = min.getZ(); z <= max.getZ(); z++) {
                                q.set(x, y, z);
                                BlockState state = air;
                                switch(getPositionType(q, min, max)) {
                                    case FLOOR:
                                        state = random.nextInt(8) == 0 ? basalt : magma;
                                        break;
                                    case WALL: case CEILING:
                                        state = magma;
                                        break;
                                    case OUTER_WALL:
                                        state = random.nextInt(4) == 0 ? magma : null;
                                        break;
                                }
                                if(state != null) {
                                    world.setBlockState(q, state, 2);
                                }
                            }
                        }
                    }
                    generateInside(world, accessor, generator, random, center, config);
                    break;
                }
            }
        }

        return true;
    }

    private boolean isPositionValid(ServerWorldAccess world, BlockPos min, BlockPos max) {
        float densityMin = 0.6F;
        float densityMax = 0.9F;
        float solidCount = 0F;
        int count = 0;
        for (BlockPos p:
             BlockPos.iterate(min, max)) {
           if(world.getBlockState(p).isFullCube(world, p)) {
               solidCount += 1F;
           }
           count++;
        }
        solidCount /= count;
        return solidCount >= densityMin && solidCount <= densityMax;
    }

    private void generateInside(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos center, DefaultFeatureConfig confi) {
        BlockPos chestPos = center.up(3);

        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.fromHorizontal(random.nextInt(4))), 2);
        LootableContainerBlockEntity.setLootTable(world, random, chestPos, MAGMA_TROVE_CHEST);

        BlockPos spawnerPos = chestPos.up();
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        BlockEntity blockEntity = world.getBlockEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.BLAZE);
        }

        spawnerPos = chestPos.down();
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        blockEntity = world.getBlockEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.MAGMA_CUBE);
        }
        BlockState chain = Blocks.CHAIN.getDefaultState();
        BlockState obsidian = Blocks.OBSIDIAN.getDefaultState();

        BlockPos.Mutable mutable = chestPos.mutableCopy();
        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = -2; dy <= 2; dy++) {
                for(int dz = -1; dz <= 1; dz++) {
                    mutable.set(chestPos.getX() + dx, chestPos.getY() + dy, chestPos.getZ() + dz);
                    BlockState state = chain;
                    if(dy == -2 || dy == 2) {
                        state = obsidian;
                    } else {
                        if(dx == 0 && dz == 0) {
                            continue;
                        }
                    }
                    world.setBlockState(mutable, state, 3);
                }
            }
        }
    }

    private PositionType getPositionType(BlockPos pos, BlockPos min, BlockPos max) {
        boolean xBorder = pos.getX() == min.getX() || pos.getX() == max.getX();
        boolean yBorder = pos.getY() == min.getY() || pos.getY() == max.getY();
        boolean zBorder = pos.getZ() == min.getZ() || pos.getZ() == max.getZ();
        if(!yBorder && (xBorder ^ zBorder)) {
            return PositionType.WALL;
        }
        if((xBorder && yBorder) || (zBorder && yBorder) || (xBorder && zBorder)) {
            return PositionType.OUTER_WALL;
        }
        if(pos.getY() == min.getY()) {
            return PositionType.FLOOR;
        }
        if(pos.getY() == max.getY()) {
            return PositionType.CEILING;
        }
        if(pos.getY() == min.getY() + 1) {
            return PositionType.ON_GROUND;
        }
        return PositionType.INSIDE;
    }

    private enum PositionType {
        WALL, FLOOR, CEILING, ON_GROUND, INSIDE, OUTER_WALL
    }
}
