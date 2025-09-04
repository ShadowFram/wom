package org.plugin.worldofmurloc;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.plugin.worldofmurloc.commands.XpSystemCommands;
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

        CommandRegistrationCallback.EVENT.register(((dispatcher,
                                                     registryAccess,
                                                     environment) -> XpSystemCommands.register(dispatcher)
                ));

        LOGGER.info("World of Murloc initialized!");
    }

}