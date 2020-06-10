package io.github.apace100.panacea.registry;

import io.github.apace100.librerecipe.recipe.brewing.BrewingRecipes;
import io.github.apace100.panacea.misc.MixingBrewingRecipe;
import io.github.apace100.panacea.misc.MushroomBrewingRecipe;
import net.minecraft.item.Items;

public class ModRecipes {

    public static void register() {
        BrewingRecipes.addRecipe(new MixingBrewingRecipe());
        BrewingRecipes.addIngredient(ModItems.WART_CATALYST);
        BrewingRecipes.addRecipe(new MushroomBrewingRecipe());
        BrewingRecipes.addIngredient(Items.RED_MUSHROOM);
    }
}
