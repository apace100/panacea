package io.github.apace100.panacea.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeFixMixin {

    @Inject(at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, method = "Lnet/minecraft/recipe/BrewingRecipeRegistry;craft(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", cancellable = true)
    private static void craftItem(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> info, Potion potion, Item item, int i, int j, BrewingRecipeRegistry.Recipe recipe) {
        Item newItem = (Item)((BrewingRecipeAccessor)recipe).getOutput();
        ItemStack newStack = new ItemStack(newItem);
        newStack.setTag(ingredient.getOrCreateTag());
        PotionUtil.setPotion(newStack, potion);
        info.setReturnValue(newStack);
    }

    @Inject(at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT, method = "Lnet/minecraft/recipe/BrewingRecipeRegistry;craft(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", cancellable = true)
    private static void craftPotion(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> info, Potion potion, Item item, int i, int j, BrewingRecipeRegistry.Recipe recipe) {
        ItemStack newStack = new ItemStack(item);
        newStack.setTag(ingredient.getOrCreateTag());
        PotionUtil.setPotion(newStack, (Potion)((BrewingRecipeAccessor) recipe).getOutput());
        info.setReturnValue(newStack);
    }
}
