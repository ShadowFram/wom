package org.plugin.worldofmurloc.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.plugin.worldofmurloc.ExperienceBarHud;

public class WorldofmurlocClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ExperienceBarHud());
    }
}
