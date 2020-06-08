package io.github.apace100.panacea.misc;

import io.github.apace100.librerecipe.recipe.brewing.SingleBrewingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

public class MushroomBrewingRecipe extends SingleBrewingRecipe {

    @Override
    public boolean isApplicable(ItemStack ingredient, ItemStack input) {
        return ingredient.getItem() == Items.RED_MUSHROOM && PotionUtil.getPotion(input) == Potions.WATER;
    }

    @Override
    public ItemStack apply(ItemStack ingredient, ItemStack input) {
        ItemStack output = input.copy();
        PotionUtil.setPotion(output, Potions.AWKWARD);
        output.getOrCreateTag().putFloat("DurationMultiplier", 0.2F);
        return output;
    }
}
