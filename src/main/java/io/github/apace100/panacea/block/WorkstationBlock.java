package io.github.apace100.panacea.block;

import io.github.apace100.panacea.registry.ModScreenHandlers;
import io.github.apace100.panacea.screen.WorkstationScreenHandler;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorkstationBlock extends CraftingTableBlock {
    private static final TranslatableText SCREEN_TITLE = new TranslatableText("container.panacea.workstation");

    public WorkstationBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
            (i, playerInventory, playerEntity)
                -> new WorkstationScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), SCREEN_TITLE);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            ContainerProviderRegistry.INSTANCE.openContainer(ModScreenHandlers.WORKSTATION, player, buf -> {
                buf.writeBlockPos(pos);
            });
            return ActionResult.CONSUME;
        }
    }
}
