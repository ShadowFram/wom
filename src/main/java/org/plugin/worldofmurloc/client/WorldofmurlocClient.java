package org.plugin.worldofmurloc.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import org.plugin.worldofmurloc.InputHandler;
import org.plugin.worldofmurloc.ModHuds;

public class WorldofmurlocClient implements ClientModInitializer {
    public static final KeyBinding[] ABILITY_KEYS = new KeyBinding[6];

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ModHud());
//        for (int i = 0; i < 6; i++) {
//            ABILITY_KEYS[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                    "key.ability." + i,
//                    InputUtil.Type.KEYSYM,
//                    InputUtil.GLFW_KEY_Z + i, // Z, X, C, V, B, N
//                    "category.abilities"
//            ));
//        }
//        InputHandler.register();
    }
}
