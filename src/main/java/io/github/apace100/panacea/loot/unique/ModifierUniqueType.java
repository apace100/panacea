package io.github.apace100.panacea.loot.unique;

import com.google.gson.JsonObject;

public class ModifierUniqueType extends UniqueType<ModifierUnique> {
    @Override
    public ModifierUnique createFromJson(JsonObject json) {
        ModifierUnique unique = new ModifierUnique();
        unique.read(json);
        return unique;
    }
}
