package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModScreenHandlers {

    public static Identifier WORKSTATION = new Identifier(Panacea.MODID, "workstation");

    public static void register() {
        register(WORKSTATION, (syncId, identifier, player, buf) -> {
            final World world = player.world;
            final BlockPos pos = buf.readBlockPos();
            return world.getBlockState(pos).createScreenHandlerFactory(player.world, pos).createMenu(syncId, player.inventory, player);
        });
    }

    private static void register(Identifier identifier, ContainerFactory<ScreenHandler> factory) {
        ContainerProviderRegistry.INSTANCE.registerFactory(identifier, factory);
    }
}
