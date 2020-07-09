package io.github.apace100.panacea.loot.modifier;

import com.google.gson.JsonObject;

public class AttributeModifierType extends ModifierType<AttributeModifier> {

    @Override
    public AttributeModifier createFromJson(JsonObject json) {
        AttributeModifier modifier = new AttributeModifier();
        modifier.read(json);
        return modifier;
    }
}
