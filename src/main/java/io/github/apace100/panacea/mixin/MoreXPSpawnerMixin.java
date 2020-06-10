package io.github.apace100.panacea.mixin;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.SpawnerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SpawnerBlock.class)
public abstract class MoreXPSpawnerMixin extends BlockWithEntity {

    protected MoreXPSpawnerMixin(Settings settings) {
        super(settings);
    }

    @ModifyConstant(method = "onStacksDropped", constant = @Constant(ordinal = 0, intValue = 15))
    private int onStacksDropped(int in) {
        return in * 4;
    }
}
