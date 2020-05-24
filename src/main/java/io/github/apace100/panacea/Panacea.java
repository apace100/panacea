package io.github.apace100.panacea;

import io.github.apace100.panacea.block.ModBlocks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Panacea implements ModInitializer {

	public static final String MODID = "panacea";

	@Override
	public void onInitialize() {
		ModBlocks.register();
	}


}
