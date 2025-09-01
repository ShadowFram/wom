package org.plugin.worldofmurloc;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class ManaRegenerationHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerComponent component = ModComponents.WOMDATA.get(player);
                component.regenerateMana();
            }
        });
    }
}