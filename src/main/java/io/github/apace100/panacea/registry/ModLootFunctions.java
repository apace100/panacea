package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.loot.ApplyRarityLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModLootFunctions {

    public static void register() {
        Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier(Panacea.MODID, "apply_rarity"), new LootFunctionType(new ApplyRarityLootFunction.Serializer()));
    }
}
