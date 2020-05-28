package io.github.apace100.panacea;

import io.github.apace100.panacea.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Panacea implements ModInitializer {

	public static final String MODID = "panacea";
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "general"), () -> new ItemStack(Items.EMERALD));

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModBlockEntities.register();
		ModContainers.register();
		ModFeatures.register();
	}


}
