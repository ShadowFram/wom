package org.plugin.worldofmurloc;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "worldofmurloc")
@Config(name = "wom-conf", wrapperName = "ModConfig")
public class ModConfigModel {
        public boolean abilityOnRight = false;
}
