package io.github.apace100.panacea.loot.modifier;

import io.github.apace100.panacea.Panacea;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModifierTypes {

    public static Registry<ModifierType> REGISTRY = FabricRegistryBuilder.createSimple(ModifierType.class, new Identifier(Panacea.MODID, "modifier_types")).attribute(RegistryAttribute.MODDED).buildAndRegister();

    public static void register() {
        Registry.register(REGISTRY, new Identifier(Panacea.MODID, "attribute"), new AttributeModifierType());
    }
}
