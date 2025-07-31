package org.plugin.worldofmurloc;

import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<PlayerComponent> WOMDATA =
            ComponentRegistry.getOrCreate(
                    Identifier.of(Worldofmurloc.MOD_ID, "playerdata"),
                    PlayerComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                WOMDATA,
                PlayerComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}