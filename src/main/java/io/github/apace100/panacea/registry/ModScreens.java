package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.screen.WorkstationScreen;
import io.github.apace100.panacea.screen.WorkstationScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ModScreens {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ModScreens.<WorkstationScreenHandler>register(ModScreenHandlers.WORKSTATION, (container) -> new WorkstationScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("container.panacea.workstation")));
    }

    private static <T extends ScreenHandler> void register(Identifier identifier, ContainerScreenFactory<T> factory) {
        ScreenProviderRegistry.INSTANCE.<T>registerFactory(identifier, factory);
    }
}
