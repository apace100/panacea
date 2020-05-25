package io.github.apace100.panacea.world;

import io.github.apace100.panacea.Panacea;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {

    private static final Feature<DefaultFeatureConfig> LAVONG;

    static {
        LAVONG = new LavongFeature(DefaultFeatureConfig::deserialize);
    }

    public static void register() {
        Registry.register(Registry.FEATURE, new Identifier(Panacea.MODID, "lavong"), LAVONG);

        Registry.BIOME.forEach(biome -> {
            biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, LAVONG.configure(new DefaultFeatureConfig()));
        });
    }
}
