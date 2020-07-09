package io.github.apace100.panacea.loot.modifier;

import com.google.gson.JsonObject;

public abstract class ModifierType<T extends Modifier> {

    public abstract T createFromJson(JsonObject json);
}
