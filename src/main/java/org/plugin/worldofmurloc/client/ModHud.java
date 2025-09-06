package org.plugin.worldofmurloc.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.plugin.worldofmurloc.ModComponents;
import org.plugin.worldofmurloc.Worldofmurloc;
import org.plugin.worldofmurloc.component.PlayerComponent;

public class ModHud implements HudRenderCallback {
    int screenWidth;
    int screenHeight;
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        screenHeight = drawContext.getScaledWindowHeight();
        screenWidth = drawContext.getScaledWindowWidth();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.options.hudHidden) return;

        var component = ModComponents.WOMDATA.get(client.player);

        renderExperienceBar(drawContext, component, client.textRenderer);
        renderPlayerAndLevel(drawContext, component, client);
        renderAbilityHotbar(drawContext, component, client.textRenderer);

    }

    public void renderExperienceBar(DrawContext context, PlayerComponent component, TextRenderer textRenderer) {
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
                textRenderer, text,
                barX + barWidth / 2 - textRenderer.getWidth(text) / 2,
                barY - 3, 0xFFFFFF, true
        );

    }

    private void renderPlayerAndLevel(DrawContext context, PlayerComponent component, MinecraftClient client) {
        String lvlText = "" + component.getLvl();
        int padding = 2;
        int textWidth = client.textRenderer.getWidth(lvlText);
        assert client.player != null;
        Identifier playerTexture = client.getSkinProvider().getSkinTextures(client.player.getGameProfile()).texture();
        context.drawTexture(playerTexture, 10, 10, 24, 24, 8, 8, 8, 8, 64, 64);

        // Фон
        context.fill(
                24, 25 + padding,
                24 + textWidth + padding, 26 + client.textRenderer.fontHeight + padding,
                0x80000000
        );

        // Текст
        context.drawText(
                client.textRenderer, lvlText,
                25, 28, 0xFFFFFF, true
        );
    }

    private void renderAbilityHotbar(DrawContext context, PlayerComponent component, TextRenderer textRenderer) {
        int y = Worldofmurloc.CONFIG.abilityOnRight() ? screenHeight - 22 : 0;
        int leftX = (screenWidth - 315) / 2;
        int rightX = (screenWidth + 186) / 2;
        // TODO: Текстуру поменять + подправить положение (я плох с циферками)
        Identifier HOTBAR_ID = Identifier.ofVanilla("textures/gui/sprites/hud/hotbar.png");
        RenderSystem.enableBlend();
        // Левая сторона
        context.drawTexture(HOTBAR_ID, leftX, y, 64, 23, 0, 0, 22, 22, 66, 22);
        // Правая сторона
        context.drawTexture(HOTBAR_ID, rightX, y, 64, 23, 0, 0, 22, 22, 66, 22);
        RenderSystem.disableBlend(); // Нужно для прозрачности (наверн)
    }
}
