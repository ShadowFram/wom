package org.plugin.worldofmurloc;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class ExperienceBarHud implements HudRenderCallback {
    private static final Identifier TEXTURE = Identifier.of("wom", "textures/gui/experience_bar.png");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        PlayerComponent component = ModComponents.WOMDATA.get(client.player);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int barWidth = 182;
        int barHeight = 5;
        int x = (screenWidth - barWidth) / 2;
        int y = screenHeight - 32;

        float progress = (float) component.getXp() / component.getXpForNewLevel();
        int filledWidth = (int)(progress * barWidth);

        // Рисуем фон
        drawContext.drawTexture(
                TEXTURE, x, y,
                0, 0, barWidth, barHeight,
                182, 10
        );

        // Рисуем заполненную часть
        if (filledWidth > 0) {
            drawContext.drawTexture(
                    TEXTURE, x, y,
                    0, 5, filledWidth, barHeight,
                    182, 10
            );
        }

        // Текст XP
        String text = component.getXp() + "/" + component.getXpForNewLevel();
        drawContext.drawText(
                client.textRenderer, text,
                x + barWidth / 2 - client.textRenderer.getWidth(text) / 2,
                y - 3, 0xFFFFFF, true
        );

        // Текст уровня
        String lvlText = "lvl: " + component.getLvl();
        int padding = 2;
        int textWidth = client.textRenderer.getWidth(lvlText);

        // Фон
        drawContext.fill(
                10 - padding, 10 - padding,
                10 + textWidth + padding, 10 + client.textRenderer.fontHeight + padding,
                0x80000000
        );

        // Текст
        drawContext.drawText(
                client.textRenderer, lvlText,
                10, 10, 0xFFFFFF, true
        );
    }
}