package io.github.apace100.panacea.loot.unique;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class ConversionUnique extends Unique {

    private Item convertTo;

    @Override
    public ItemStack create(Random random, ItemStack stack) {
        ItemStack converted = new ItemStack(convertTo, stack.getCount());
        if(stack.hasTag()) {
            converted.setTag(stack.getTag().copy());
        }
        return super.create(random, converted);
    }

    @Override
    protected void read(JsonObject jsonObject) {
        super.read(jsonObject);
        if(!jsonObject.has("item")) {
            throw new JsonParseException("Unique JSON of type \"panacea:conversion\" requires \"item\" id!");
        }
        Identifier itemId = Identifier.tryParse(jsonObject.get("item").getAsString());
        convertTo = Registry.ITEM.get(itemId);
    }
}
