package io.github.apace100.panacea.mixin;

import io.github.apace100.panacea.registry.ModItems;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandMixin extends LockableContainerBlockEntity implements SidedInventory, Tickable {

    @Shadow
    private DefaultedList<ItemStack> inventory;

    protected BrewingStandMixin(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(at = @At("HEAD"), method = "canCraft()Z", cancellable = true)
    private void canCraft(CallbackInfoReturnable<Boolean> info) {
        ItemStack itemStack = inventory.get(3);

        if(itemStack.getItem() == ModItems.WART_CATALYST) {
            int count = 0;
            for(int i = 0; i < 3; i++) {
                if(!inventory.get(i).isEmpty()) {
                    count++;
                }
            }
            info.setReturnValue(count > 1);
        }
    }

    @Inject(at = @At("HEAD"), method = "craft()V", cancellable = true)
    private void craft(CallbackInfo info) {
        ItemStack itemStack = inventory.get(3);

        if(itemStack.getItem() == ModItems.WART_CATALYST) {
            assert world != null;
            List<ItemStack> potionsIn = new LinkedList<>();
            boolean isSecondEmpty = false;
            for(int i = 0; i < 3; ++i) {
                ItemStack stack = this.inventory.get(i);
                potionsIn.add(stack);
                if(i == 1 && stack.isEmpty()) {
                    isSecondEmpty = true;
                }
            }

            if(getNonMixableCount(potionsIn) > 0) {
                for(int i = 0; i < 4; i++) {
                    this.inventory.set(i, ItemStack.EMPTY);
                }
                world.createExplosion((Entity)null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, Explosion.DestructionType.DESTROY);
                info.cancel();
                return;
            }
            ItemStack resultingPotion = createMixedPotion(potionsIn);
            for(int i = 0; i < 3; i++) {
                ItemStack stack = inventory.get(i);
                ItemStack remainder = ItemStack.EMPTY;
                if(stack.getItem().hasRecipeRemainder() && !(i == 0 && isSecondEmpty)) {
                    remainder = new ItemStack(stack.getItem().getRecipeRemainder());
                }
                if(i == 1) {
                    this.inventory.set(i, resultingPotion);
                } else {
                    this.inventory.set(i, remainder);
                }
            }
            itemStack.decrement(1);
            BlockPos blockPos = this.getPos();
            this.world.syncWorldEvent(1035, blockPos, 0);
            info.cancel();
            return;
        }
    }

    private int getNonMixableCount(List<ItemStack> potionStacksIn) {
        int count = 0;
        for (ItemStack itemStack : potionStacksIn) {
            if(itemStack.getOrCreateTag().getBoolean("IsMixed")) {
                count++;
            }
        }
        return count;
    }

    private ItemStack createMixedPotion(List<ItemStack> potionStacksIn) {
        ItemStack result = new ItemStack(Items.POTION);
        HashMap<StatusEffect, StatusEffectInstance> effects = new HashMap<>();
        for(ItemStack is : potionStacksIn) {
            if(!is.getOrCreateTag().getBoolean("IsMixed")) {
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
        }
        PotionUtil.setCustomPotionEffects(result, effects.values());
        result.setCustomName(new LiteralText("§r").append(new TranslatableText("panacea.mixed_potion")));
        result.getOrCreateTag().putBoolean("IsMixed", true);
        return result;
    }
}
