package io.github.apace100.panacea.loot.modifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;
import net.minecraft.util.collection.WeightedPicker;

import java.util.Optional;
import java.util.Random;

public abstract class Modifier {

    protected ModifierLocation location;
    protected String translationKey;
    protected int weight;
    protected ModifierApplicability[] applicabilities;

    public void apply(Random random, ItemStack stack) {
        if(!getOrCreateTranslationKey().isEmpty()) {
            Text nameText = stack.getName();
            if(nameText instanceof MutableText) {
                switch (location) {
                    case PREFIX:
                        nameText = new TranslatableText(translationKey).append(" ").append(nameText);
                        break;
                    case SUFFIX:
                        nameText = ((MutableText)nameText).append(" ").append(new TranslatableText(translationKey));
                        break;
                }
            }
            stack.setCustomName(nameText);
        }
    }

    protected String getOrCreateTranslationKey() {
        if (this.translationKey == null) {
            Optional<Identifier> identifier = ModifierManager.getId(this);
            if(identifier.isPresent()) {
                this.translationKey = Util.createTranslationKey("modifier", identifier.get());
            } else {
                this.translationKey = "";
            }
        }

        return this.translationKey;
    }

    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }

    public ModifierLocation getLocation() {
        return location;
    }

    public TranslatableText getText() {
        return new TranslatableText(translationKey);
    }

    public int getWeight() {
        return weight;
    }

    public boolean isApplicable(ItemStack stack) {
        for(int i = 0; i < applicabilities.length; i++) {
            if(applicabilities[i].isApplicable(stack)) {
                return true;
            }
        }
        return false;
    }

    protected void read(JsonObject jsonObject) {
        if(!jsonObject.has("location")) {
            throw new JsonParseException("Modifier JSON requires \"location\" key!");
        }
        this.location = ModifierLocation.valueOf(jsonObject.get("location").getAsString().toUpperCase());
        this.translationKey = JsonHelper.getString(jsonObject, "translation", null);
        this.weight = JsonHelper.getInt(jsonObject, "weight", 0);
        if(jsonObject.has("applicable")) {
            JsonArray applicableArray = jsonObject.getAsJsonArray("applicable");
            applicabilities = new ModifierApplicability[applicableArray.size()];
            for(int i = 0; i < applicableArray.size(); i++) {
                applicabilities[i] = ModifierApplicability.valueOf(applicableArray.get(i).getAsString().toUpperCase());
            }
        } else {
            applicabilities = new ModifierApplicability[0];
        }
    }

    public static Modifier fromJson(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        if(!json.has("type")) {
            throw new JsonParseException("Modifier JSON requires a 'type'!");
        }
        Identifier typeId = Identifier.tryParse(json.get("type").getAsString());
        if(!ModifierTypes.REGISTRY.containsId(typeId)) {
            throw new JsonParseException("Modifier JSON 'type' was not registered!");
        }
        ModifierType<?> type = ModifierTypes.REGISTRY.get(typeId);
        Modifier modifier = type.createFromJson(json);
        return modifier;
    }

    public static class Weighted extends WeightedPicker.Entry {
        private final Modifier modifier;

        public Weighted(Modifier modifier) {
            super(modifier.weight);
            this.modifier = modifier;
        }

        public Modifier getModifier() {
            return modifier;
        }
    }
}
