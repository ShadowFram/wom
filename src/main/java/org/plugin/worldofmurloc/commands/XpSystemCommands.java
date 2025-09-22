package org.plugin.worldofmurloc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.plugin.worldofmurloc.ModComponents;

public class XpSystemCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("wom").requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.literal("playerInfo")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> {
                                            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                            return playerInfo(context.getSource(), target);
                                        })
                                )
                        )
                        .then(CommandManager.literal("setXp")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                                    ModComponents.WOMDATA.get(target).setXp(amount);
                                                    context.getSource().sendFeedback(() ->
                                                                    Text.literal("Установлено " + amount + " XP для " + target.getName().getString()),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("addXp")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                                    ModComponents.WOMDATA.get(target).addXp(amount);
                                                    context.getSource().sendFeedback(() ->
                                                                    Text.literal("Добавлено " + amount + " XP для " + target.getName().getString()),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("setLvl")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                                    ModComponents.WOMDATA.get(target).setLvl(amount);
                                                    context.getSource().sendFeedback(() ->
                                                                    Text.literal("Установлен " + amount + " уровень для " + target.getName().getString()),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("setMana")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                                .executes(context -> {
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                                    float amount = FloatArgumentType.getFloat(context, "amount");
                                                    ModComponents.WOMDATA.get(target).setMana(amount);
                                                    context.getSource().sendFeedback(() ->
                                                                    Text.literal("Установлено " + amount + " маны для " + target.getName().getString()),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("setManaSpeed")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                                .executes(context -> {
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                                    float amount = FloatArgumentType.getFloat(context, "amount");
                                                    ModComponents.WOMDATA.get(target).setManaSpeed(amount);
                                                    context.getSource().sendFeedback(() ->
                                                                    Text.literal("Установлен " + amount + " множитель маны для " + target.getName().getString()),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }

    private static int playerInfo(ServerCommandSource source, ServerPlayerEntity target) {
        // Получаем данные игрока из вашего компонента
        var data = ModComponents.WOMDATA.get(target);

        // Формируем информационное сообщение
        Text info = Text.literal("Информация о игроке " + target.getName().getString())
                .append(String.format("\nУровень: %d", data.getLvl()))
                .append(String.format("\nОпыт: %d", data.getXp()))
                .append(String.format("\nМаксимальная манна: %f", data.getMaxMana()))
                .append(String.format("\n Множитель для манны: %f", data.getManaSpeedMulti()));

        source.sendFeedback(() -> info, false);
        return 1;
    }
}
