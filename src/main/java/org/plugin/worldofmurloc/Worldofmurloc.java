package org.plugin.worldofmurloc;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.plugin.worldofmurloc.component.PlayerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worldofmurloc implements ModInitializer {
    public static final String MOD_ID = "wom";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Инициализация всех систем мода
        ModItems.initialize();
        ModBlocks.initialize();
        DieForXp.register();

        // Регистрация команд
        registerCommands();

        LOGGER.info("World of Murloc initialized!");
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // Команда для получения информации о игроке
            dispatcher.register(CommandManager.literal("playerinfo")
                    .executes(context -> {
                        if (context.getSource().getPlayer() instanceof ServerPlayerEntity player) {
                            PlayerComponent component = ModComponents.WOMDATA.get(player);
                            context.getSource().sendFeedback(() ->
                                            Text.literal(String.format("XP: %d/%d | Level: %d",
                                                    component.getXp(),
                                                    component.getXpForNewLevel(),
                                                    component.getLvl())),
                                    false
                            );
                            return 1;
                        }
                        context.getSource().sendError(Text.literal("This command can only be used by players"));
                        return 0;
                    }));

            // Команда для добавления опыта (только для операторов)
            dispatcher.register(CommandManager.literal("addxp")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                            .executes(context -> {
                                int amount = IntegerArgumentType.getInteger(context, "amount");
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player != null) {
                                    XpSystem.addXp(player, amount);
                                    context.getSource().sendFeedback(() ->
                                                    Text.literal("Added " + amount + " XP to " + player.getName().getString()),
                                            true
                                    );
                                    return 1;
                                }
                                return 0;
                            })));
        });
    }
}