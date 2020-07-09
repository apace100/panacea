package io.github.apace100.panacea.loot.unique;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.apace100.panacea.Panacea;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UniqueManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private static HashMap<Identifier, Unique> uniques = new HashMap<>();
    private static HashMap<Unique, Identifier> uniqueToIdMap = new HashMap<>();

    public UniqueManager() {
        super(GSON, "uniques");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> loader, ResourceManager manager, Profiler profiler) {
        uniques.clear();
        uniqueToIdMap.clear();
        loader.forEach((id, jo) -> {
            try {
                Unique unique = Unique.fromJson(jo);
                uniques.put(id, unique);
                uniqueToIdMap.put(unique, id);
            } catch(Exception e) {
                Panacea.LOGGER.error("There was a problem reading modifier file " + id.toString() + " (skipping): " + e.getMessage());
            }
        });

    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Panacea.MODID, "uniques");
    }

    public static Optional<Unique> get(Identifier id) {
        if(!uniques.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(uniques.get(id));
    }

    public static Optional<Identifier> getId(Unique unique) {
        if(!uniqueToIdMap.containsKey(unique)) {
            return Optional.empty();
        }
        return Optional.of(uniqueToIdMap.get(unique));
    }

    public static List<Unique.Weighted> getApplicableUniques(ItemStack stack) {
        return getApplicableUniques(stack, u -> true);
    }

    public static List<Unique.Weighted> getApplicableUniques(ItemStack stack, Predicate<Unique> filter) {
        return uniques.values().stream().filter(unique -> unique.isApplicable(stack) && filter.test(unique)).map(unique -> new Unique.Weighted(unique)).collect(Collectors.toList());
    }
}
