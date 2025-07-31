package org.plugin.worldofmurloc;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DieForXp {
    private static final TagKey<EntityType<?>> XP10_TAG =
            TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Worldofmurloc.MOD_ID, "xp_10"));

    private static final TagKey<EntityType<?>> XP20_TAG =
            TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Worldofmurloc.MOD_ID, "xp_20"));

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            // Проверяем, что убийца - игрок
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                // Проверяем теги моба
                if (entity.getType().isIn(XP10_TAG)) {
                    XpSystem.addXp(player, 10);
                    player.sendMessage(Text.literal("+10 XP за убийство ").append(entity.getName()), false);
                }
                else if (entity.getType().isIn(XP20_TAG)) {
                    XpSystem.addXp(player, 20);
                    player.sendMessage(Text.literal("+20 XP за убийство ").append(entity.getName()), false);
                }
                else {
                    // Стандартное количество XP для мобов без тега
                    XpSystem.addXp(player, 5);
                    player.sendMessage(Text.literal("+5 XP за убийство ").append(entity.getName()), false);
                }
            }
            return true; // Разрешаем смерть
        });
    }
}