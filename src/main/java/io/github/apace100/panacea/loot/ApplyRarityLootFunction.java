package io.github.apace100.panacea.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.HashSet;
import java.util.Set;

public class ApplyRarityLootFunction extends ConditionalLootFunction {

    private final EquipmentRarity rarity;
    private final Set<Identifier> uniquePool;
    private final Set<Identifier> prefixPool;
    private final Set<Identifier> suffixPool;

    private ApplyRarityLootFunction(LootCondition[] conditions, EquipmentRarity rarity, Set<Identifier> uniquePool, Set<Identifier> prefixPool, Set<Identifier> suffixPool) {
        super(conditions);
        this.rarity = rarity;
        this.uniquePool = uniquePool;
        this.prefixPool = prefixPool;
        this.suffixPool = suffixPool;
    }

    public LootFunctionType getType() {
        return LootFunctionTypes.SET_NBT;
    }

    public ItemStack process(ItemStack stack, LootContext context) {
        ItemStack appliedStack = EquipmentRarity.applyRarity(context.getRandom(), stack, rarity, uniquePool, prefixPool, suffixPool);
        return appliedStack;
    }

    public static Builder<?> builder(EquipmentRarity rarity, Set<Identifier> uniquePool, Set<Identifier> prefixPool, Set<Identifier> suffixPool) {
        return builder((conditions) -> {
            return new ApplyRarityLootFunction(conditions, rarity, uniquePool, prefixPool, suffixPool);
        });
    }

    public static class Serializer extends net.minecraft.loot.function.ConditionalLootFunction.Serializer<ApplyRarityLootFunction> {
        public Serializer() {
        }

        public void toJson(JsonObject jsonObject, ApplyRarityLootFunction function, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, function, jsonSerializationContext);
            jsonObject.addProperty("rarity", function.rarity.toString());
            if(function.uniquePool != null) {
                JsonArray uniquePoolArray = new JsonArray();
                function.uniquePool.forEach(entry -> uniquePoolArray.add(entry.toString()));
                jsonObject.add("uniques", uniquePoolArray);
            }
            if(function.prefixPool != null) {
                JsonArray prefixPoolArray = new JsonArray();
                function.prefixPool.forEach(entry -> prefixPoolArray.add(entry.toString()));
                jsonObject.add("prefixes", prefixPoolArray);
            }
            if(function.suffixPool != null) {
                JsonArray suffixPoolArray = new JsonArray();
                function.suffixPool.forEach(entry -> suffixPoolArray.add(entry.toString()));
                jsonObject.add("prefixes", suffixPoolArray);
            }
        }

        public ApplyRarityLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            EquipmentRarity rarity = EquipmentRarity.valueOf(JsonHelper.getString(jsonObject, "rarity").toUpperCase());
            Set<Identifier> uniquePool = null;
            Set<Identifier> prefixPool = null;
            Set<Identifier> suffixPool = null;
            JsonArray array;
            if(jsonObject.has("uniques") && jsonObject.get("uniques").isJsonArray()) {
                array = jsonObject.getAsJsonArray("uniques");
                uniquePool = new HashSet<>();
                for(int i = 0; i < array.size(); i++) {
                    uniquePool.add(Identifier.tryParse(array.get(i).getAsString()));
                }
            }
            if(jsonObject.has("prefixes") && jsonObject.get("prefixes").isJsonArray()) {
                array = jsonObject.getAsJsonArray("prefixes");
                prefixPool = new HashSet<>();
                for(int i = 0; i < array.size(); i++) {
                    prefixPool.add(Identifier.tryParse(array.get(i).getAsString()));
                }
            }
            if(jsonObject.has("suffixes") && jsonObject.get("suffixes").isJsonArray()) {
                array = jsonObject.getAsJsonArray("suffixes");
                suffixPool = new HashSet<>();
                for(int i = 0; i < array.size(); i++) {
                    suffixPool.add(Identifier.tryParse(array.get(i).getAsString()));
                }
            }
            return new ApplyRarityLootFunction(lootConditions, rarity, uniquePool, prefixPool, suffixPool);
        }
    }
}