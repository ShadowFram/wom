package org.plugin.worldofmurloc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.plugin.worldofmurloc.commands.XpSystemCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worldofmurloc implements ModInitializer {
    public static final String MOD_ID = "wom";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final org.plugin.worldofmurloc.ModConfig CONFIG = org.plugin.worldofmurloc.ModConfig.createAndLoad();

    @Override
    public void onInitialize() {
        // Инициализация всех систем мода
        ModItems.initialize();
        DieForXp.register();

        CommandRegistrationCallback.EVENT.register(((dispatcher,
                                                     registryAccess,
                                                     environment) -> XpSystemCommands.register(dispatcher)
                ));

        LOGGER.info("World of Murloc initialized!");
    }

}