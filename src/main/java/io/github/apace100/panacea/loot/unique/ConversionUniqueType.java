package io.github.apace100.panacea.loot.unique;

import com.google.gson.JsonObject;

public class ConversionUniqueType extends UniqueType<ConversionUnique> {
    @Override
    public ConversionUnique createFromJson(JsonObject json) {
        ConversionUnique unique = new ConversionUnique();
        unique.read(json);
        return unique;
    }
}
