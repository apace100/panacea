package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.block.EnderAltarBlock;
import io.github.apace100.panacea.block.AltarFrameBlock;
import io.github.apace100.panacea.block.LavongBlock;
import io.github.apace100.panacea.block.WorkstationBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block LAVONG = new LavongBlock(FabricBlockSettings.of(Material.PLANT).strength(0.2F, 3.0F).ticksRandomly().sounds(BlockSoundGroup.METAL).nonOpaque());
    public static final Block ENDER_ALTAR = new EnderAltarBlock(FabricBlockSettings.of(Material.STONE).strength(0.8F, 3.5F).sounds(BlockSoundGroup.STONE));
    public static final Block ENDER_ALTAR_FRAME = new AltarFrameBlock(FabricBlockSettings.of(Material.STONE).strength(1.2F, 3.0F).sounds(BlockSoundGroup.STONE));
    public static final Block WORKSTATION = new WorkstationBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));

    public static void register() {
        register("lavong", LAVONG);
        register("ender_altar", ENDER_ALTAR);
        register("ender_altar_frame", ENDER_ALTAR_FRAME);
        register("workstation", WORKSTATION);
    }

    private static void register(String blockName, Block block) {
        register(blockName, block, true);
    }

    private static void register(String blockName, Block block, boolean withBlockItem) {
        Registry.register(Registry.BLOCK, new Identifier(Panacea.MODID, blockName), block);
        if(withBlockItem) {
            Registry.register(Registry.ITEM, new Identifier(Panacea.MODID, blockName), new BlockItem(block, new Item.Settings().group(Panacea.ITEM_GROUP)));
        }
    }
}
