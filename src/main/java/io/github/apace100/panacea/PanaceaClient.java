package io.github.apace100.panacea;

import io.github.apace100.panacea.registry.ModBlocks;
import io.github.apace100.panacea.registry.ModScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class PanaceaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVONG, RenderLayer.getTranslucent());
        ModScreens.register();
    }
}
