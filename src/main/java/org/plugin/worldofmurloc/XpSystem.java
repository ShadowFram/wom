package org.plugin.worldofmurloc;

import net.minecraft.entity.player.PlayerEntity;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class XpSystem {
    public static void addXp(PlayerEntity player, int amount) {
        PlayerComponent component = ModComponents.WOMDATA.get(player);
        component.addXp(amount);
        Worldofmurloc.LOGGER.info("Added " + amount + " XP to player " + player.getName().getString());
    }

    public static int getCurrentXp(PlayerEntity player) {
        return ModComponents.WOMDATA.get(player).getXp();
    }

    public static int getLevel(PlayerEntity player) {
        return ModComponents.WOMDATA.get(player).getLvl();
    }

    public static int getXpForNewLevel(PlayerEntity player) {
        return ModComponents.WOMDATA.get(player).getXpForNewLevel();
    }
}