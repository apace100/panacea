package io.github.apace100.panacea;

import io.github.apace100.panacea.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class PanaceaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVONG, RenderLayer.getTranslucent());
        System.out.println("Initializing Panacea client");
    }
}
