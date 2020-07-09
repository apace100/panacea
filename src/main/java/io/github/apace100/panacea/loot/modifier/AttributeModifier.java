package io.github.apace100.panacea.loot.modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Random;
import java.util.UUID;

public class AttributeModifier extends Modifier {

    private EntityAttribute attribute;
    private EntityAttributeModifier.Operation operation;
    private double minValue;
    private double maxValue;

    @Override
    public void apply(Random random, ItemStack stack) {
        super.apply(random, stack);
        ListTag modListTag;
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("AttributeModifiers", NbtType.LIST)) {
            modListTag = (ListTag)tag.get("AttributeModifiers");
        } else {
            modListTag = new ListTag();
        }
        CompoundTag modTag = new CompoundTag();
        modTag.putString("Slot", getSlot(stack));
        String attributeName = Registry.ATTRIBUTE.getId(attribute).toString();
        modTag.putString("AttributeName", attributeName);
        modTag.putInt("Operation", operation.getId());
        modTag.putString("Name", attributeName);
        modTag.putDouble("Amount", random.nextDouble() * (maxValue - minValue) + minValue);
        modTag.putUuid("UUID", UUID.randomUUID());
        modListTag.add(modTag);
        tag.put("AttributeModifiers", modListTag);
    }

    private String getSlot(ItemStack stack) {
        if(stack.getItem() instanceof ArmorItem) {
            return ((ArmorItem)stack.getItem()).getSlotType().getName();
        } else if(stack.getItem() instanceof ShieldItem) {
            return EquipmentSlot.OFFHAND.getName();
        } else {
            return EquipmentSlot.MAINHAND.getName();
        }
    }

    @Override
    protected void read(JsonObject jsonObject) {
        super.read(jsonObject);
        JsonObject attrObj = jsonObject.getAsJsonObject("attribute");
        Identifier attrId = Identifier.tryParse(attrObj.get("id").getAsString());
        if(!Registry.ATTRIBUTE.containsId(attrId)) {
            throw new JsonParseException("Modifier JSON encountered a problem: could not find attribute with id " + attrId.toString());
        }
        attribute = Registry.ATTRIBUTE.get(attrId);
        if(!attrObj.has("operation")) {
            throw new JsonParseException("Modifier JSON requires \"operation\" key in \"attribute\" object.");
        }
        JsonPrimitive opElement = attrObj.getAsJsonPrimitive("operation");
        if(opElement.isString()) {
            operation = EntityAttributeModifier.Operation.valueOf(opElement.getAsString().toUpperCase());
        } else if(opElement.isNumber()) {
            operation = EntityAttributeModifier.Operation.fromId(opElement.getAsInt());
        } else {
            throw new JsonParseException("Modifier JSON requires \"operation\" key in \"attribute\" object to be an integer or a string.");
        }
        if(!attrObj.has("value")) {
            throw new JsonParseException("Modifier JSON requires \"value\" key in \"attribute\" object.");
        }
        JsonElement valueElem = attrObj.get("value");
        if(valueElem.isJsonPrimitive()) {
            minValue = maxValue = valueElem.getAsDouble();
        } else {
            JsonObject valueObj = valueElem.getAsJsonObject();
            if(valueObj.has("min") && valueObj.has("max")) {
                minValue = valueObj.get("min").getAsDouble();
                maxValue = valueObj.get("max").getAsDouble();
            } else {
                throw new JsonParseException("Modifier JSON requires \"value\" to be a double or an object containing \"min\" and \"max\".");
            }
        }
    }
}
