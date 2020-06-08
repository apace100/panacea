package io.github.apace100.panacea.misc;

import io.github.apace100.librerecipe.recipe.brewing.MultiBrewingRecipe;
import io.github.apace100.panacea.registry.ModItems;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MixingBrewingRecipe extends MultiBrewingRecipe {

    @Override
    public boolean isApplicable(ItemStack ingredient, ItemStack[] input) {
        return ingredient.getItem() == ModItems.WART_CATALYST && getMixables(input).size() > 1;
    }

    @Override
    public ItemStack[] apply(ItemStack ingredient, ItemStack[] input) {
        Set<Integer> mix = getMixables(input);
        ItemStack potion = createMixedPotion(mix.stream().map(i -> input[i]).collect(Collectors.toList()));
        ItemStack[] output = new ItemStack[input.length];
        int outputSlot = 1;
        if(!mix.contains(1)) {
            outputSlot = mix.stream().findFirst().get();
        }
        for(int i = 0; i < output.length; i++) {
            output[i] = i == outputSlot ? potion : mix.contains(i) ? new ItemStack(input[i].getItem().getRecipeRemainder()) : input[i];
        }
        ingredient.decrement(1);
        return output;
    }

    private Set<Integer> getMixables(ItemStack[] input) {
        HashMap<Item, Set<Integer>> itemCounts = new HashMap<>();
        for(int i = 0; i < input.length; i++) {
            if(!input[i].isEmpty()) {
                if(!input[i].getOrCreateTag().getBoolean("IsMixed")) {
                    Item item = input[i].getItem();
                    if(itemCounts.containsKey(item)) {
                        itemCounts.get(item).add(i);
                    } else {
                        HashSet<Integer> mixSet = new HashSet<>();
                        mixSet.add(i);
                        itemCounts.put(item, mixSet);
                    }
                }
            }
        }
        Set<Integer> largestSet = new HashSet<>();
        int largestSetSize = -1;
        for(Set<Integer> set : itemCounts.values()) {
            if(set.size() > largestSetSize) {
                largestSetSize = set.size();
                largestSet = set;
            }
        }
        return largestSet;
    }

    private ItemStack createMixedPotion(List<ItemStack> potionStacksIn) {
        ItemStack result = new ItemStack(potionStacksIn.get(0).getItem());
        HashMap<StatusEffect, StatusEffectInstance> effects = new HashMap<>();
        for(ItemStack is : potionStacksIn) {
            PotionUtil.getPotionEffects(is).forEach(sei -> {
                if(effects.containsKey(sei.getEffectType())) {
                    StatusEffectInstance stored = effects.get(sei.getEffectType());
                    if(stored.getAmplifier() < sei.getAmplifier()) {
                        effects.put(sei.getEffectType(), sei);
                    } else if(stored.getAmplifier() == sei.getAmplifier()){
                        if(sei.getDuration() > stored.getDuration()) {
                            effects.put(sei.getEffectType(), sei);
                        }
                    }
                } else {
                    effects.put(sei.getEffectType(), sei);
                }
            });
        }
        PotionUtil.setCustomPotionEffects(result, effects.values());
        result.setCustomName(new LiteralText("§r").append(new TranslatableText("panacea.mixed_potion")));
        result.getOrCreateTag().putBoolean("IsMixed", true);
        return result;
    }
}
