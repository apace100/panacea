package io.github.apace100.panacea.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(PotionUtil.class)
public abstract class PotionStrengthMixin {

    @Inject(at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, method = "Lnet/minecraft/potion/PotionUtil;getPotionEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", cancellable = true)
    private static void getPotionEffects(CompoundTag tag, CallbackInfoReturnable<List<StatusEffectInstance>> info, List<StatusEffectInstance> list) {
        float multiplier = 1.0F;
        if(tag != null) {
            if(tag.contains("DurationMultiplier")) {
                multiplier = tag.getFloat("DurationMultiplier");
            }
            List<StatusEffectInstance> multipliedEffects = new ArrayList<StatusEffectInstance>(list.size());
            for(StatusEffectInstance sei : list) {
                multipliedEffects.add(new StatusEffectInstance(sei.getEffectType(), (int)Math.ceil(sei.getDuration() * multiplier), sei.getAmplifier(), sei.isAmbient(), sei.shouldShowParticles(), sei.shouldShowIcon()));
            }
            info.setReturnValue(multipliedEffects);
        }
    }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/potion/PotionUtil;buildTooltip(Lnet/minecraft/item/ItemStack;Ljava/util/List;F)V")
    private static void buildTooltip(ItemStack stack, List<Text> list, float f, CallbackInfo info) {
        if(PotionUtil.getPotionEffects(stack).size() == 0 && stack.getOrCreateTag().contains("DurationMultiplier")) {
            float multiplier = stack.getOrCreateTag().getFloat("DurationMultiplier");
            Formatting color = multiplier > 1F ? Formatting.GREEN : Formatting.RED;
            int display = (int)(multiplier * 100);
            list.add(new TranslatableText("panacea.duration_multiplier").append(" " + display + "%").formatted(color));
        }
    }
}
