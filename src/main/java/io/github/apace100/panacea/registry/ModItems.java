package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.item.WartedArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static Item WARTED_HELMET = new WartedArmorItem(EquipmentSlot.HEAD, new Item.Settings().group(Panacea.ITEM_GROUP));
    public static Item WARTED_CHESTPLATE = new WartedArmorItem(EquipmentSlot.CHEST, new Item.Settings().group(Panacea.ITEM_GROUP));
    public static Item WARTED_LEGGINGS = new WartedArmorItem(EquipmentSlot.LEGS, new Item.Settings().group(Panacea.ITEM_GROUP));
    public static Item WARTED_BOOTS = new WartedArmorItem(EquipmentSlot.FEET, new Item.Settings().group(Panacea.ITEM_GROUP));

    public static void register() {
        register("warted_helmet", WARTED_HELMET);
        register("warted_chestplate", WARTED_CHESTPLATE);
        register("warted_leggings", WARTED_LEGGINGS);
        register("warted_boots", WARTED_BOOTS);
    }

    private static void register(String path, Item item) {
        Registry.register(Registry.ITEM, new Identifier(Panacea.MODID, path), item);
    }
}
