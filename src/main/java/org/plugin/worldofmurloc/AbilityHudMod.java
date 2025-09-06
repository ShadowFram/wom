package org.plugin.worldofmurloc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class AbilityHudMod implements ClientModInitializer {
    public static final KeyBinding[] ABILITY_KEYS = new KeyBinding[6];
    
    @Override
    public void onInitializeClient() {
        // Регистрируем клавиши для способностей
        for (int i = 0; i < 6; i++) {
            ABILITY_KEYS[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ability." + i,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_Z + i, // Z, X, C, V, B, N
                "category.abilities"
            ));
        }
        // Регистрируем обработчик ввода
        InputHandler.register();
        
    }
}