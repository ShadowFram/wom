package org.plugin.worldofmurloc.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import org.plugin.worldofmurloc.util.XpHudState;

public class WorldofmurlocClient implements ClientModInitializer {
    public static final KeyBinding[] ABILITY_KEYS = new KeyBinding[6];

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ModHud());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            XpHudState.tick();
        });
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
