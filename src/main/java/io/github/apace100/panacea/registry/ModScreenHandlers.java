package io.github.apace100.panacea.registry;

import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static void register() {

    }

    private static void register(Identifier identifier, ContainerFactory<ScreenHandler> factory) {
        ContainerProviderRegistry.INSTANCE.registerFactory(identifier, factory);
    }
}
