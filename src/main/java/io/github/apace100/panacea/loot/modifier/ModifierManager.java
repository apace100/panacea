package io.github.apace100.panacea.loot.modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.apace100.panacea.Panacea;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.profiler.Profiler;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModifierManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private static HashMap<Identifier, Modifier> modifiers = new HashMap<>();
    private static HashMap<Modifier, Identifier> modToIdMap = new HashMap<>();

    public ModifierManager() {
        super(GSON, "modifiers");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> loader, ResourceManager manager, Profiler profiler) {
        modifiers.clear();
        modToIdMap.clear();
        loader.forEach((id, jo) -> {
            try {
                Modifier modifier = Modifier.fromJson(jo);
                modifiers.put(id, modifier);
                modToIdMap.put(modifier, id);
            } catch(Exception e) {
                Panacea.LOGGER.error("There was a problem reading modifier file " + id.toString() + " (skipping): " + e.getMessage());
            }
        });

    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Panacea.MODID, "modifiers");
    }

    public static Optional<Modifier> get(Identifier id) {
        if(!modifiers.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(modifiers.get(id));
    }

    public static Optional<Identifier> getId(Modifier mod) {
        if(!modToIdMap.containsKey(mod)) {
            return Optional.empty();
        }
        return Optional.of(modToIdMap.get(mod));
    }

    public static List<Modifier.Weighted> getApplicableModifiers(ItemStack stack, ModifierLocation location, Predicate<Modifier> filter) {
        return modifiers.values().stream().filter(modifier -> modifier.getLocation() == location && modifier.isApplicable(stack) && filter.test(modifier)).map(modifier -> new Modifier.Weighted(modifier)).collect(Collectors.toList());
    }

    public static void addRandomModifier(Random random, ItemStack stack, ModifierLocation location, Predicate<Modifier> filter) {
        List<Modifier.Weighted> mods = getApplicableModifiers(stack, location, filter);
        if(mods.size() > 0) {
            Modifier mod = WeightedPicker.getRandom(random, mods).getModifier();
            mod.apply(random, stack);
        }
    }

    public static void addModifier(Random random, ItemStack stack, Modifier modifier) {
        modifier.apply(random, stack);
    }
}
