package io.github.apace100.panacea;

import io.github.apace100.panacea.loot.modifier.ModifierManager;
import io.github.apace100.panacea.loot.modifier.ModifierTypes;
import io.github.apace100.panacea.loot.unique.UniqueManager;
import io.github.apace100.panacea.loot.unique.UniqueTypes;
import io.github.apace100.panacea.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Panacea implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger(Panacea.class);

	public static final String MODID = "panacea";
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "general"), () -> new ItemStack(ModItems.WART_CATALYST));

	@Override
	public void onInitialize() {
		LOGGER.info("Loading Panacea...");
		ModBlocks.register();
		ModItems.register();
		ModComponents.register();
		ModBlockEntities.register();
		ModScreenHandlers.register();
		ModFeatures.register();
		ModRecipes.register();
		ModCriteria.register();
		ModLootFunctions.register();
		ModifierTypes.register();
		UniqueTypes.register();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ModifierManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new UniqueManager());
	}


}
