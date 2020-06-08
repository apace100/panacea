package io.github.apace100.panacea.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class CharcoalBurnTimeMixin extends LockableContainerBlockEntity {

    protected CharcoalBurnTimeMixin(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(at = @At("HEAD"), method = "getFuelTime", cancellable = true)
    protected void getFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> info) {
        if(fuel.getItem() == Items.CHARCOAL) {
            info.setReturnValue(800);
        }
    }
}
