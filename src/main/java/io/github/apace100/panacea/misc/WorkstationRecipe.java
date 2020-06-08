package io.github.apace100.panacea.misc;

import io.github.apace100.librerecipe.recipe.CountedIngredient;
import io.github.apace100.librerecipe.recipe.CountedIngredientRecipe;
import io.github.apace100.panacea.registry.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.List;

public class WorkstationRecipe extends CountedIngredientRecipe {

    public WorkstationRecipe(List<CountedIngredient> inputs, ItemStack outputStack, Identifier id) {
        super(inputs, outputStack, id);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.Workstation.SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.Workstation.TYPE;
    }
}
