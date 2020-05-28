package io.github.apace100.panacea.mixin;

import io.github.apace100.panacea.helper.Imbuing;
import io.github.apace100.panacea.item.WartedArmorItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilImbuingMixin extends ForgingScreenHandler {

    public AnvilImbuingMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Shadow
    private int repairItemUsage;

    @Shadow
    private Property levelCost;

    @Inject(at = @At("HEAD"), method = "updateResult()V", cancellable = true)
    public void updateResult(CallbackInfo info) {
        this.context.run((world, pos) -> {
            if(!world.isClient) {
                ItemStack armorStack = this.input.getStack(0);
                if (armorStack.getItem() instanceof WartedArmorItem && !Imbuing.isImbued(armorStack)) {
                    ItemStack potionStack = this.input.getStack(1);
                    if(potionStack.getItem() instanceof PotionItem) {
                        List<StatusEffectInstance> effectInstances = PotionUtil.getPotionEffects(potionStack);
                        if(effectInstances.size() > 0) {
                            this.output.setStack(0, Imbuing.imbue(armorStack, effectInstances));
                            this.levelCost.set(30);
                            this.sendContentUpdates();
                            info.cancel();
                        }
                    }
                }
            }
        });
    }
}
