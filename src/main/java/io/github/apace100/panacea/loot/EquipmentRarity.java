package io.github.apace100.panacea.loot;

import io.github.apace100.panacea.loot.modifier.Modifier;
import io.github.apace100.panacea.loot.modifier.ModifierLocation;
import io.github.apace100.panacea.loot.modifier.ModifierManager;
import io.github.apace100.panacea.loot.unique.Unique;
import io.github.apace100.panacea.loot.unique.UniqueManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedPicker;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public enum EquipmentRarity {
    MAGIC, RARE, UNIQUE;

    public static ItemStack applyRarity(Random random, ItemStack stack, EquipmentRarity rarity) {
        return applyRarity(random, stack, rarity, null, null, null);
    }

    public static ItemStack applyRarity(Random random, ItemStack stack, EquipmentRarity rarity, Set<Identifier> uniquePool, Set<Identifier> prefixPool, Set<Identifier> suffixPool) {
        ItemStack appliedStack = stack;
        switch (rarity) {
            case MAGIC:
                boolean isPrefix = random.nextBoolean() && (prefixPool == null || prefixPool.size() > 0);
                ModifierLocation loc = isPrefix ? ModifierLocation.PREFIX : ModifierLocation.SUFFIX;
                Set<Identifier> pool = isPrefix ? prefixPool : suffixPool;
                Predicate<Modifier> filter = mod -> true;
                if(pool != null) {
                    filter = mod -> pool.contains(ModifierManager.getId(mod).get());
                }
                ModifierManager.addRandomModifier(random, stack, loc, filter);
                setNameColor(stack, Formatting.BLUE);
                break;
            case RARE:
                Predicate<Modifier> prefixFilter = mod -> true;
                if(prefixPool != null) {
                    prefixFilter = mod -> prefixPool.contains(ModifierManager.getId(mod).get());
                }
                ModifierManager.addRandomModifier(random, stack, ModifierLocation.PREFIX, prefixFilter);
                Predicate<Modifier> suffixFilter = mod -> true;
                if(prefixPool != null) {
                    suffixFilter = mod -> prefixPool.contains(ModifierManager.getId(mod).get());
                }
                ModifierManager.addRandomModifier(random, stack, ModifierLocation.SUFFIX, suffixFilter);
                setNameColor(stack, Formatting.YELLOW);
                break;
            case UNIQUE:
                Predicate<Unique> uniqueFilter = unique -> true;
                if(uniquePool != null) {
                    uniqueFilter = unique -> uniquePool.contains(UniqueManager.getId(unique).get());
                }
                List<Unique.Weighted> uniques = UniqueManager.getApplicableUniques(stack, uniqueFilter);
                if(uniques.size() > 0) {
                    Unique unique = WeightedPicker.getRandom(random, uniques).getUnique();
                    appliedStack = unique.create(random, stack);
                    setNameColor(appliedStack, Formatting.GOLD);
                }
                break;
        }
        return appliedStack;
    }

    private static void setNameColor(ItemStack stack, Formatting color) {
        CompoundTag displayTag = stack.getOrCreateSubTag("display");
        Text name = stack.getName();
        if(name instanceof MutableText) {
            displayTag.putString("Name", Text.Serializer.toJson(((MutableText)name).formatted(color)));
        }
    }
}
