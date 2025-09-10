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
    private enum textPlaces {
        up, down, left, right
    }
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        screenHeight = drawContext.getScaledWindowHeight();
        screenWidth = drawContext.getScaledWindowWidth();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.options.hudHidden) return;

        var component = ModComponents.WOMDATA.get(client.player);

        renderPlayerAndLevel(drawContext, component, client);
        renderAbilityHotbar(drawContext, component, client.textRenderer);
        renderManaBar(drawContext, component, client.textRenderer);
    }

    private static int countFilledWidth(float current, float max, int barWidth) {
        if (max <= 0f) return 0;
        float clampedCurrent = MathHelper.clamp(current, 0.0F, max);
        float ratio = clampedCurrent / max;
        return (int) (barWidth * ratio);
    }


    private static void drawBar(DrawContext context, TextRenderer textRenderer, textPlaces textPlace, int barWidth, int barHeight, int barX, int barY, int barBackColor, int barFillColor, float currentAmount, float maxAmount) {
        currentAmount = MathHelper.ceil(currentAmount * 2f) / 2f;
        int filledWidth = countFilledWidth(currentAmount, maxAmount, barWidth);
        int textX;
        int textY;
        String text = String.format("%d/%d", (int) (currentAmount), (int) (maxAmount));
        switch (textPlace) {
            case up -> {
                textX = barX + barWidth / 2 - textRenderer.getWidth(text) / 2;
                textY = barY - barHeight;
            }
            case down -> {
                textX = barX + barWidth / 2 - textRenderer.getWidth(text) / 2;
                textY = barY + barHeight;
            }
            case left -> {
                textX = barX - textRenderer.getWidth(text) - 5;
                textY = barY + (barHeight - textRenderer.fontHeight) / 2;
            }
            case right -> {
                textX = barX + barWidth + 5;
                textY = barY + (barHeight - textRenderer.fontHeight) / 2;
            }
            default -> {
                textX = 0;
                textY = 0;
            }
        }

        context.fill(barX, barY, barX + barWidth, barY + barHeight , barBackColor); // Фон
        context.fill(barX, barY, barX + filledWidth, barY + barHeight, barFillColor); // Заполнение

        context.drawText(textRenderer, text, textX, textY, 0xFFFFFF, true);
    }

    public static void renderExperienceBar(DrawContext context, PlayerComponent component, TextRenderer textRenderer, int screenWidth, int screenHeight) {
        int barWidth = 182;
        int barHeight = 5;
        int barX = (screenWidth - barWidth) / 2;
        int barY = screenHeight - 28;

        drawBar(
                context,
                textRenderer,
                textPlaces.up,
                barWidth,
                barHeight,
                barX,
                barY,
                0xFF7F7F7F,
                0xFF3D00AF,
                component.getXp(),
                component.getXpForNewLevel()
        );
    }

    private void renderPlayerAndLevel(DrawContext context, PlayerComponent component, MinecraftClient client) {
        String lvlText = "" + component.getLvl();
        int padding = 2;
        int textWidth = client.textRenderer.getWidth(lvlText);
        int textHeight = client.textRenderer.fontHeight;
        int textX = 34 - textWidth / 2;
        int textY = 34 - textHeight / 2;
        assert client.player != null;
        Identifier playerTexture = client.getSkinProvider().getSkinTextures(client.player.getGameProfile()).texture();
        context.drawTexture(playerTexture, 10, 10, 24, 24, 8, 8, 8, 8, 64, 64);

        // Фон
        context.fill(
                textX - padding, textY - padding,
                textX + textWidth + padding, textY + textHeight ,
                0x80000000
        );

        // Текст
        context.drawText(
                client.textRenderer, lvlText,
                textX, textY, 0xFFFFFF, true
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

    public static void renderHealthBar(DrawContext context, TextRenderer textRenderer, float maxHealth, int currentHealth) {
        int width = 100;
        int height = 11;
        int x = 40;
        int y = 25;

        drawBar(
                context,
                textRenderer,
                textPlaces.right,
                width,
                height,
                x,
                y,
                0xFF7F7F7F,
                0xFFFC2525,
                currentHealth,
                maxHealth
        );
    }

    public static void renderHungerBar(DrawContext context, TextRenderer textRenderer, float currentHunger) {
        int width = 100;
        int height = 11;
        int x = 40;
        int y = 40;

        drawBar(
                context,
                textRenderer,
                textPlaces.right,
                width,
                height,
                x,
                y,
                0xFF7F7F7F,
                0xFFB7721F,
                currentHunger,
                20f
        );
    }

    public void renderManaBar(DrawContext context, PlayerComponent component, TextRenderer textRenderer) {
        int width = 100;
        int height = 11;
        int x = 40;
        int y = 10;
        float currentMana = component.getMana();
        float maxMana = component.getMaxMana();

        drawBar(
                context,
                textRenderer,
                textPlaces.right,
                width,
                height,
                x,
                y,
                0xFF7F7F7F,
                0xFF0394FC,
                currentMana,
                maxMana
        );
    }
}