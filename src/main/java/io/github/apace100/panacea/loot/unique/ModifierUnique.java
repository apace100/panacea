package io.github.apace100.panacea.loot.unique;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.apace100.panacea.loot.modifier.Modifier;
import io.github.apace100.panacea.loot.modifier.ModifierManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.Random;

public class ModifierUnique extends Unique {

    private ApplicableMod[] modifiers;

    @Override
    public ItemStack create(Random random, ItemStack stack) {
        for(int i = 0; i < modifiers.length; i++) {
            modifiers[i].getModifier().apply(random, stack);
        }
        return super.create(random, stack);
    }

    @Override
    protected void read(JsonObject jsonObject) {
        super.read(jsonObject);
        if(!jsonObject.has("modifiers") || !jsonObject.get("modifiers").isJsonArray()) {
            throw new JsonParseException("Unique JSON requires \"modifiers\" array!");
        }
        JsonArray modifiers = jsonObject.getAsJsonArray("modifiers");
        this.modifiers = new ApplicableMod[modifiers.size()];
        for(int i = 0; i < modifiers.size(); i++) {
            this.modifiers[i] = ApplicableMod.fromJson(modifiers.get(i));
        }
    }

    private static class ApplicableMod {
        private Identifier modifierId;
        private Modifier modifier;

        public ApplicableMod(String id) {
            this.modifierId = Identifier.tryParse(id);
        }

        public ApplicableMod(Modifier mod) {
            this.modifier = mod;
        }

        public Modifier getModifier() {
            if(modifier == null) {
                Optional<Modifier> mod = ModifierManager.get(modifierId);
                if(mod.isPresent()) {
                    modifier = mod.get();
                } else if(!mod.isPresent()) {
                    throw new RuntimeException("ModifierUnique had invalid modifier id: " + modifierId);
                }
            }
            return modifier;
        }

        public static ApplicableMod fromJson(JsonElement element) {
            if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return new ApplicableMod(element.getAsString());
            } else if(element.isJsonObject()) {
                return new ApplicableMod(Modifier.fromJson(element));
            }
            throw new JsonParseException("Could not get modifier from JSON in unique modifiers array!");
        }
    }
}
