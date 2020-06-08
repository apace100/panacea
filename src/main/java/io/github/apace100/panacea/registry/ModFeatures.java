package io.github.apace100.panacea.registry;

import com.google.common.collect.Lists;
import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.world.LavongFeature;
import io.github.apace100.panacea.world.MagmaTroveFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {

    private static final Feature<DefaultFeatureConfig> LAVONG;
    private static final Feature<DefaultFeatureConfig> MAGMA_TROVE;

    static {
        LAVONG = new LavongFeature(DefaultFeatureConfig.CODEC);
        MAGMA_TROVE = new MagmaTroveFeature(DefaultFeatureConfig.CODEC);
    }

    public static void register() {
        Registry.register(Registry.FEATURE, new Identifier(Panacea.MODID, "lavong"), LAVONG);
        Registry.BIOME.forEach(biome -> {
            biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, LAVONG.configure(new DefaultFeatureConfig()));
        });
        Lists.newArrayList(Biomes.SOUL_SAND_VALLEY, Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST, Biomes.BASALT_DELTAS, Biomes.NETHER_WASTES).forEach(biome -> {
            biome.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, MAGMA_TROVE.configure(new DefaultFeatureConfig()));
        });
    }
}
