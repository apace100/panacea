package io.github.apace100.panacea.block;

import io.github.apace100.panacea.Panacea;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block LAVONG = new LavongBlock(FabricBlockSettings.of(Material.PLANT).strength(0.2F, 3.0F).ticksRandomly().sounds(BlockSoundGroup.METAL).nonOpaque());

    public static void register() {
        registerBlock("lavong", LAVONG);
    }

    private static void registerBlock(String blockName, Block block) {
        registerBlock(blockName, block, true);
    }

    private static void registerBlock(String blockName, Block block, boolean withBlockItem) {
        Registry.register(Registry.BLOCK, new Identifier(Panacea.MODID, blockName), block);
        if(withBlockItem) {
            Registry.register(Registry.ITEM, new Identifier(Panacea.MODID, blockName), new BlockItem(block, new Item.Settings().group(ItemGroup.MISC)));
        }
    }
}
