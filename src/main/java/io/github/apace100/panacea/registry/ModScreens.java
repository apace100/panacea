package io.github.apace100.panacea.registry;

import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class ModScreens {

    public static void register() {
        //ModScreens.<WorkstationContainer>register(ModContainers.WORKSTATION, (container) -> new WorkstationScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("container.panacea.workstation")));
    }

    private static <T extends ScreenHandler> void register(Identifier identifier, ContainerScreenFactory<T> factory) {
        ScreenProviderRegistry.INSTANCE.<T>registerFactory(identifier, factory);
    }
}
