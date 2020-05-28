package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {

    public static void register() {

    }

    private static void register(String typeName, BlockEntityType<?> type) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Panacea.MODID, typeName), type);
    }
}
