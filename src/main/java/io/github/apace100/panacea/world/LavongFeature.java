package io.github.apace100.panacea.world;

import com.mojang.serialization.Codec;
import io.github.apace100.panacea.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class LavongFeature extends Feature<DefaultFeatureConfig> {

    public LavongFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockState lavong = ModBlocks.LAVONG.getDefaultState();
        BlockPos.Mutable p = new BlockPos.Mutable();
        p.set(pos.asLong());
        p.setX(pos.getX() + random.nextInt(16));
        p.setZ(pos.getZ() + random.nextInt(16));
        for(int tries = 0; tries < 8; tries++) {
            for(int i = 8; i < 24; i++) {
                p.setY(i);
                if(world.isAir(p) && lavong.canPlaceAt(world, p)) {
                    world.setBlockState(p, lavong, 2);
                    BlockPos.Mutable q = new BlockPos.Mutable();
                    q.set(p.asLong());
                    for(int dx = -3; dx <= 3; dx++) {
                        for(int dz = -3; dz <= 3; dz++) {
                            for(int dy = -3; dy <= 3; dy++) {
                                q.setX(p.getX() + dx);
                                q.setY(p.getY() + dy);
                                q.setZ(p.getZ() + dz);
                                if(random.nextInt(8) == 0 && world.isAir(q) && lavong.canPlaceAt(world, q)) {
                                    world.setBlockState(q, lavong, 2);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        return true;
    }
}
