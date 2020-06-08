package io.github.apace100.panacea.helper;

import io.github.apace100.panacea.registry.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.LinkedList;
import java.util.List;

public class Imbuing {

    public static ItemStack imbue(ItemStack armor, List<StatusEffectInstance> effects) {
        ItemStack toImbue = armor.copy();
        ListTag list = new ListTag();
        effects.forEach(sei -> {
            StatusEffectInstance shortEffect = new StatusEffectInstance(sei.getEffectType(), 300, sei.getAmplifier(), false, false, true);
            list.add(shortEffect.toTag(new CompoundTag()));
        });
        toImbue.getOrCreateTag().put("PotionEffects", list);
        toImbue.getOrCreateTag().putBoolean("Imbued", true);
        return toImbue;
    }

    public static boolean isWartedArmor(Item item) {
        return  item == ModItems.WARTED_BOOTS ||
                item == ModItems.WARTED_LEGGINGS ||
                item == ModItems.WARTED_CHESTPLATE ||
                item == ModItems.WARTED_HELMET;
    }

    public static boolean isImbued(ItemStack stack) {
        return stack.getOrCreateTag().contains("Imbued") && stack.getOrCreateTag().getBoolean("Imbued") && stack.getOrCreateTag().contains("PotionEffects");
    }

    public static List<StatusEffectInstance> getImbuements(ItemStack stack) {
        ListTag list = (ListTag)stack.getOrCreateTag().get("PotionEffects");
        List<StatusEffectInstance> imbuements = new LinkedList<>();
        for(int i = 0; i < list.size(); i++) {
            imbuements.add(StatusEffectInstance.fromTag(list.getCompound(i)));
        }
        return imbuements;
    }
}
