package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.screen.TransmutationTableScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ModScreens {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ScreenRegistry.register(ModScreenHandlers.TRANSMUTATION_TABLE, TransmutationTableScreen::new);
    }
}
