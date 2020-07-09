package io.github.apace100.panacea.loot.unique;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;
import net.minecraft.util.collection.WeightedPicker;

import java.util.Optional;
import java.util.Random;

public class Unique {

    protected String translationKey;
    protected Ingredient[] baseItems;
    protected int weight;

    public ItemStack create(Random random, ItemStack stack) {
        stack.setCustomName(new TranslatableText(getOrCreateTranslationKey()));
        return stack;
    }

    protected String getOrCreateTranslationKey() {
        if (this.translationKey == null) {
            Optional<Identifier> identifier = UniqueManager.getId(this);
            if(identifier.isPresent()) {
                this.translationKey = Util.createTranslationKey("unique", identifier.get());
            } else {
                this.translationKey = "";
            }
        }

        return this.translationKey;
    }

    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }

    public boolean isApplicable(ItemStack stack) {
        for(int i = 0; i < baseItems.length; i++) {
            if(baseItems[i].test(stack)) {
                return true;
            }
        }
        return false;
    }

    protected void read(JsonObject jsonObject) {
        if(!jsonObject.has("base_items") || !jsonObject.get("base_items").isJsonArray()) {
            throw new JsonParseException("Unique JSON requires \"base_items\" array!");
        }
        JsonArray baseItemsArray = jsonObject.getAsJsonArray("base_items");
        this.baseItems = new Ingredient[baseItemsArray.size()];
        for(int i = 0; i < baseItemsArray.size(); i++) {
            this.baseItems[i] = Ingredient.fromJson(baseItemsArray.get(i));
        }
        this.translationKey = JsonHelper.getString(jsonObject, "translation", null);
        this.weight = JsonHelper.getInt(jsonObject, "weight", 0);
    }

    public static Unique fromJson(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        if(!json.has("type")) {
            throw new JsonParseException("Unique JSON requires a 'type'!");
        }
        Identifier typeId = Identifier.tryParse(json.get("type").getAsString());
        if(!UniqueTypes.REGISTRY.containsId(typeId)) {
            throw new JsonParseException("Unique JSON 'type' was not registered!");
        }
        UniqueType<?> type = UniqueTypes.REGISTRY.get(typeId);
        Unique modifier = type.createFromJson(json);
        return modifier;
    }

    public static class Weighted extends WeightedPicker.Entry {
        private final Unique unique;

        public Weighted(Unique unique) {
            super(unique.weight);
            this.unique = unique;
        }

        public Unique getUnique() {
            return unique;
        }
    }
}
