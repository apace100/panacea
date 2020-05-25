package io.github.apace100.panacea.world;

import com.mojang.datafixers.Dynamic;
import io.github.apace100.panacea.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class LavongFeature extends Feature<DefaultFeatureConfig> {

    public LavongFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockState lavong = ModBlocks.LAVONG.getDefaultState();
        BlockPos.Mutable p = new BlockPos.Mutable(pos);
        p.setX(pos.getX() + random.nextInt(16));
        p.setZ(pos.getZ() + random.nextInt(16));
        for(int i = 8; i < 24; i++) {
            p.setY(i);
            if(world.isAir(p) && lavong.canPlaceAt(world, p)) {
                world.setBlockState(p, lavong, 2);
                BlockPos.Mutable q = new BlockPos.Mutable(p);
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
        return true;
    }
}
