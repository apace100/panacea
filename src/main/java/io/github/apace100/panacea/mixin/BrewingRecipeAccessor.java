package io.github.apace100.panacea.mixin;

import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrewingRecipeRegistry.Recipe.class)
public interface BrewingRecipeAccessor {

    @Accessor
    public Object getOutput();
}
