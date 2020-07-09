package io.github.apace100.panacea.loot.unique;

import io.github.apace100.panacea.Panacea;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UniqueTypes {

    public static Registry<UniqueType> REGISTRY = FabricRegistryBuilder.createSimple(UniqueType.class, new Identifier(Panacea.MODID, "unique_types")).attribute(RegistryAttribute.MODDED).buildAndRegister();

    public static void register() {
        Registry.register(REGISTRY, new Identifier(Panacea.MODID, "modifier"), new ModifierUniqueType());
        Registry.register(REGISTRY, new Identifier(Panacea.MODID, "conversion"), new ConversionUniqueType());
    }
}
