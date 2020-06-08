package io.github.apace100.panacea.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(PickaxeItem.class)
public abstract class CoalMiningLevelMixin extends MiningToolItem {

    protected CoalMiningLevelMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    @Inject(at = @At("HEAD"), method = "isEffectiveOn", cancellable = true)
    public void isEffectiveOn(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if(state.getBlock() == Blocks.COAL_ORE && getMaterial().getMiningLevel() < 1) {
            info.setReturnValue(false);
        }
    }
}
