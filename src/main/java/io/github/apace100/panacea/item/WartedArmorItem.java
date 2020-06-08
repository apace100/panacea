package io.github.apace100.panacea.item;

import io.github.apace100.panacea.helper.Imbuing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class WartedArmorItem extends ArmorItem {

    public WartedArmorItem(EquipmentSlot slot, Settings settings) {
        super(Material.INSTANCE, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient && Imbuing.isImbued(stack) && entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity)entity;
            if(le.getEquippedStack(this.slot) == stack && world.getTime() % 60 == 0) {
                Imbuing.getImbuements(stack).forEach(sei -> le.addStatusEffect(sei));
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if(!Imbuing.isImbued(stack)) {
            tooltip.add(LiteralText.EMPTY);
            tooltip.add(new TranslatableText("panacea.imbuable").formatted(Formatting.GRAY));
        } else {
            tooltip.add(LiteralText.EMPTY);
            tooltip.add(new TranslatableText("item.modifiers." + slot.getName()).formatted(Formatting.GRAY));
            List<StatusEffectInstance> effects = Imbuing.getImbuements(stack);
            for (StatusEffectInstance effect : effects) {
                TranslatableText text = new TranslatableText(effect.getTranslationKey());
                if(effect.getAmplifier() > 0) {
                    text.append(" ").append(new TranslatableText("potion.potency." + effect.getAmplifier()));
                }
                text.formatted(effect.getEffectType().getType().getFormatting());
                tooltip.add(text);
            }
        }
    }

    public static class Material implements ArmorMaterial {

        public static Material INSTANCE = new Material();

        private Material() {}
        @Override
        public int getDurability(EquipmentSlot slot) {
            return 10 * new int[]{1,3,5,2}[slot.getEntitySlotId()];
        }

        @Override
        public int getProtectionAmount(EquipmentSlot slot) {
            return 1;
        }

        @Override
        public int getEnchantability() {
            return 0;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(Blocks.NETHER_WART_BLOCK);
        }

        @Override
        public String getName() {
            return "warted";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }
}
