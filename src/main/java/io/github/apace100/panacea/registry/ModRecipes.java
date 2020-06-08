package io.github.apace100.panacea.registry;

import io.github.apace100.librerecipe.recipe.CountedIngredientRecipe;
import io.github.apace100.librerecipe.recipe.brewing.BrewingRecipes;
import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.misc.MixingBrewingRecipe;
import io.github.apace100.panacea.misc.MushroomBrewingRecipe;
import io.github.apace100.panacea.misc.WorkstationRecipe;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {

    public static class Workstation {
        public static Identifier ID = new Identifier(Panacea.MODID, "workstation");
        public static RecipeType<WorkstationRecipe> TYPE = new CountedIngredientRecipe.Type();
        public static RecipeSerializer<WorkstationRecipe> SERIALIZER = new CountedIngredientRecipe.CountedIngredientRecipeSerializer<>(data -> new WorkstationRecipe(data.inputs, data.outputStack, data.identifier));
    }

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, Workstation.ID.toString(), Workstation.SERIALIZER);
        Registry.register(Registry.RECIPE_TYPE, Workstation.ID.toString(), Workstation.TYPE);

        BrewingRecipes.addRecipe(new MixingBrewingRecipe());
        BrewingRecipes.addIngredient(ModItems.WART_CATALYST);
        BrewingRecipes.addRecipe(new MushroomBrewingRecipe());
        BrewingRecipes.addIngredient(Items.RED_MUSHROOM);
    }
}
