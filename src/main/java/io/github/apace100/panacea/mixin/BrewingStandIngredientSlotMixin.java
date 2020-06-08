package io.github.apace100.panacea.mixin;

import io.github.apace100.panacea.registry.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/screen/BrewingStandScreenHandler$IngredientSlot")
public abstract class BrewingStandIngredientSlotMixin extends Slot {

    public BrewingStandIngredientSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(at = @At("HEAD"), method = "canInsert(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if(stack.getItem() == ModItems.WART_CATALYST || stack.getItem() == Items.RED_MUSHROOM) {
            info.setReturnValue(true);
        }
    }
}
