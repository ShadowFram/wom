package org.plugin.worldofmurloc;

import net.minecraft.entity.player.PlayerEntity;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class ManaSystem {
    public static final String MANA_KEY = "ability_mana";
    
    public static int getMana(PlayerEntity player) {
        PlayerComponent component = ModComponents.WOMDATA.get(player);
        return(int) component.getMana();
    }
    
    public static void setMana(PlayerEntity player, int amount) {
        PlayerComponent component = ModComponents.WOMDATA.get(player);
        component.setMana(amount);
    }

    public static void addMana(PlayerEntity player, int amount) {
        setMana(player, getMana(player) + amount);
    }

    public static void consumeMana(PlayerEntity player, int amount) {
        setMana(player, getMana(player) - amount);
    }
}