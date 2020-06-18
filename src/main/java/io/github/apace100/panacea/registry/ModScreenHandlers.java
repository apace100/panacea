package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.screen.TransmutationTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static ScreenHandlerType<TransmutationTableScreenHandler> TRANSMUTATION_TABLE;

    public static void register() {
        TRANSMUTATION_TABLE = ScreenHandlerRegistry.registerSimple(new Identifier(Panacea.MODID, "transmutation_table"), TransmutationTableScreenHandler::new);
    }
}
