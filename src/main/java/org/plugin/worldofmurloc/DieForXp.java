package org.plugin.worldofmurloc;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
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
            if (source.getAttacker() instanceof ServerPlayerEntity playerEntity) {
                var player = ModComponents.WOMDATA.get(playerEntity);
                // Проверяем теги моба
                if (entity.getType().isIn(XP10_TAG)) {
                    // TODO: Заменить на обычные компоненты
                    player.addXp(10);
                    playerEntity.sendMessage(Text.literal("+10 XP за убийство ").append(entity.getName()), false);
                }
                else if (entity.getType().isIn(XP20_TAG)) {
                    player.addXp(20);
                    playerEntity.sendMessage(Text.literal("+20 XP за убийство ").append(entity.getName()), false);
                }
                else {
                    // Стандартное количество XP для мобов без тега
                    player.addXp(5);
                    playerEntity.sendMessage(Text.literal("+5 XP за убийство ").append(entity.getName()), false);
                }
            }
            return true; // Разрешаем смерть
        });
    }
}