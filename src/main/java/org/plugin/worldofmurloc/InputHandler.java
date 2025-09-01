package org.plugin.worldofmurloc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.plugin.worldofmurloc.AbilityHudMod;
import org.plugin.worldofmurloc.ModComponents;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class InputHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (client.player != null) {
                for (int i = 0; i < AbilityHudMod.ABILITY_KEYS.length; i++) {
                    if (AbilityHudMod.ABILITY_KEYS[i].wasPressed()) {
                        handleAbilityUse(i, client.player);
                    }
                }
            }
        });
    }

    private static void handleAbilityUse(int slotIndex, PlayerEntity player) {
        // Получаем стоимость маны для способности
        float manaCost = getManaCost(slotIndex);

        // Получаем компонент игрока
        PlayerComponent component = ModComponents.WOMDATA.get(player);

        // Проверяем достаточно ли маны
        if (component.hasEnoughMana(manaCost)) {
            // Используем способность
            useAbility(slotIndex, player);
            // Отнимаем ману
            component.consumeMana(manaCost);

            // Воспроизводим звук
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        } else {
            // Сообщение о недостатке маны
            player.sendMessage(Text.literal("Недостаточно маны!"), true);
            //player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL, 1.0f, 0.5f);
        }
    }

    static float getManaCost(int slotIndex) {
        // Стоимость маны для каждой способности
        return switch (slotIndex) {
            case 0 -> 10.0f;
            case 1 -> 15.0f;
            case 2 -> 20.0f;
            case 3 -> 25.0f;
            case 4 -> 30.0f;
            case 5 -> 35.0f;
            default -> 0.0f;
        };
    }

    private static void useAbility(int slotIndex, PlayerEntity player) {
        PlayerComponent component = ModComponents.WOMDATA.get(player);

        switch (slotIndex) {
            //case 0 -> useFireball(player, component);
        }
    }
    
    private static void useFireball(PlayerEntity player) {
        // Логика файрбола
    }
    
    // ... другие методы способностей
}