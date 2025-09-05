package org.plugin.worldofmurloc.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.plugin.worldofmurloc.ModComponents;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class ModHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.options.hudHidden) return;

        var component = ModComponents.WOMDATA.get(client.player);

        renderExperienceBar(drawContext, component, client);
        renderPlayerAndLevel(drawContext, component, client);

    }

    private void renderExperienceBar(DrawContext context, PlayerComponent component, MinecraftClient client) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        int barWidth = 182;
        int barHeight = 5;
        int barX = (screenWidth - barWidth) / 2;
        int barY = screenHeight - 32;

        float progress = component.getXpForNewLevel() > 0 ? (float) component.getXp() / component.getXpForNewLevel() : 0f;
        progress = MathHelper.clamp(progress, 0f, 1f);

        int filledWidth = (int)(progress * barWidth);

        context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF7F7F7F); // Фон
        context.fill(barX, barY, barX + filledWidth, barY + barHeight, 0xFF3D00AF); // Заполнение

        String text = component.getXp() + "/" + component.getXpForNewLevel();
        context.drawText(
                client.textRenderer, text,
                barX + barWidth / 2 - client.textRenderer.getWidth(text) / 2,
                barY - 3, 0xFFFFFF, true
        );

    }

    private void renderPlayerAndLevel(DrawContext context, PlayerComponent component, MinecraftClient client) {
        int playerIconX = 8;
        int playerIconY = 8;
        String lvlText = "lvl: " + component.getLvl();
        int padding = 2;
        int textWidth = client.textRenderer.getWidth(lvlText);
        assert client.player != null;
        Identifier playerTexture = client.getSkinProvider().getSkinTextures(client.player.getGameProfile()).texture();
        context.drawTexture(playerTexture, 10, 10, 24, 24, 8, 8, 8, 8, 64, 64);

        // Фон
        context.fill(
                10, 38 + padding,
                10 + textWidth + padding, 38 + client.textRenderer.fontHeight + padding,
                0x80000000
        );

        // Текст
        context.drawText(
                client.textRenderer, lvlText,
                10, 38, 0xFFFFFF, true
        );
    }
}
