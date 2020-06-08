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

    public static Item WART_CATALYST = simpleItem();

    public static Item TOOL_HANDLE = simpleItem();
    public static Item SWORD_HANDLE = simpleItem();

    public static Item BLANK_TEMPLATE = simpleItem();
    public static Item CHESTPLATE_TEMPLATE = simpleItem();
    public static Item HELMET_TEMPLATE = simpleItem();
    public static Item LEGGINGS_TEMPLATE = simpleItem();
    public static Item BOOTS_TEMPLATE = simpleItem();

    public static Item TOOL_HANDLE_TEMPLATE = simpleItem();
    public static Item SWORD_HANDLE_TEMPLATE = simpleItem();

    public static void register() {
        register("warted_helmet", WARTED_HELMET);
        register("warted_chestplate", WARTED_CHESTPLATE);
        register("warted_leggings", WARTED_LEGGINGS);
        register("warted_boots", WARTED_BOOTS);

        register("wart_catalyst", WART_CATALYST);

        register("tool_handle", TOOL_HANDLE);
        register("sword_handle", SWORD_HANDLE);

        register("blank_template", BLANK_TEMPLATE);
        register("chestplate_template", CHESTPLATE_TEMPLATE);
        register("helmet_template", HELMET_TEMPLATE);
        register("leggings_template", LEGGINGS_TEMPLATE);
        register("boots_template", BOOTS_TEMPLATE);

        register("sword_handle_template", SWORD_HANDLE_TEMPLATE);
        register("tool_handle_template", TOOL_HANDLE_TEMPLATE);
        for (String part:
            parts) {
            register(part + "_template", simpleItem());
        }
        registerToolParts("wooden");
        registerToolParts("stone");
        registerToolParts("iron");
        registerToolParts("golden");
        registerToolParts("diamond");
    }

    private static final String[] parts = new String[] {"axe_head", "pickaxe_head", "shovel_head", "hoe_head", "sword_blade"};
    private static void registerToolParts(String toolMaterial) {

        for (String part:
             parts) {
            register(toolMaterial + "_" + part, simpleItem());
        }
    }

    private static void register(String path, Item item) {
        Registry.register(Registry.ITEM, new Identifier(Panacea.MODID, path), item);
    }

    private static Item simpleItem() {
        return new Item(new Item.Settings().group(Panacea.ITEM_GROUP));
    }
}
