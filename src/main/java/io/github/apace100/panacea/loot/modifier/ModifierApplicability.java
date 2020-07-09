package io.github.apace100.panacea.loot.modifier;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

import java.util.function.Predicate;

public enum ModifierApplicability {

    HELMET(stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem)stack.getItem()).getSlotType() == EquipmentSlot.HEAD),
    CHESTPLATE(stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem)stack.getItem()).getSlotType() == EquipmentSlot.CHEST),
    LEGGINGS(stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem)stack.getItem()).getSlotType() == EquipmentSlot.LEGS),
    BOOTS(stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem)stack.getItem()).getSlotType() == EquipmentSlot.FEET),
    MELEE(stack -> stack.getItem() instanceof SwordItem),
    SHIELD(stack -> stack.getItem() instanceof ShieldItem),
    RANGED(stack -> stack.getItem() instanceof RangedWeaponItem);

    private final Predicate<ItemStack> predicate;

    ModifierApplicability(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    public boolean isApplicable(ItemStack stack) {
        return predicate.test(stack);
    }
}
