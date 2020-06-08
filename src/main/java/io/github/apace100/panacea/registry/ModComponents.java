package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.Panacea;
import io.github.apace100.panacea.component.AltarLookupComponent;
import io.github.apace100.panacea.component.WorldAltarLookupComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.minecraft.util.Identifier;

public class ModComponents {

    public static final ComponentType<AltarLookupComponent> ALTAR_LOOKUP = register("altar_lookup", AltarLookupComponent.class);

    public static void register() {
        WorldComponentCallback.EVENT.register((world, components) -> components.put(ALTAR_LOOKUP, new WorldAltarLookupComponent()));
    }

    private static <T extends Component> ComponentType<T> register(String path, Class<T> clazz) {
        return ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(Panacea.MODID, path), clazz);
    }
}
