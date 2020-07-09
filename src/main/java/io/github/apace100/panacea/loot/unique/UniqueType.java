package io.github.apace100.panacea.loot.unique;

import com.google.gson.JsonObject;

public abstract class UniqueType<T extends Unique> {

    public abstract T createFromJson(JsonObject json);
}
