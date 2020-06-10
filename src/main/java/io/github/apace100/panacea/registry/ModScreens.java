package io.github.apace100.panacea.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class ModScreens {

    @Environment(EnvType.CLIENT)
    public static void register() {

    }

    private static <T extends ScreenHandler> void register(Identifier identifier, ContainerScreenFactory<T> factory) {
        ScreenProviderRegistry.INSTANCE.registerFactory(identifier, factory);
    }
}
