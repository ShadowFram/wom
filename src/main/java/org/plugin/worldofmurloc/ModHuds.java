package org.plugin.worldofmurloc;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.Drawable;
import org.plugin.worldofmurloc.component.PlayerComponent;

import static org.plugin.worldofmurloc.InputHandler.getManaCost;

public class ModHuds implements HudRenderCallback {
    private static final Identifier TEXTURE = Identifier.of("wom", "textures/gui/experience_bar.png");
    private static final int SLOT_SIZE = 22;
    private static final int ICON_SIZE = 16;
    private static final int TEXTURE_SIZE = 64;
    private static final int MARGIN = 2;

    void renderAbilitySlot(DrawContext context, int x, int y, int slotIndex, MinecraftClient client) {

// Простая версия (если текстура 64x64 и слот 22x22)
        context.drawTexture(TEXTURE, x, y, 0, 0, SLOT_SIZE, SLOT_SIZE);

// Полная версия с явным указанием размеров
        context.drawTexture(
                TEXTURE,           // идентификатор текстуры
                x,                 // X на экране
                y,                 // Y на экране
                SLOT_SIZE,         // ширина отрисовки
                SLOT_SIZE,         // высота отрисовки
                0,                 // U (X в текстуре)
                0,                 // V (Y в текстуре)
                SLOT_SIZE,         // ширина области в текстуре
                SLOT_SIZE,         // высота области в текстуре
                64,                // ширина всей текстуры
                64                 // высота всей текстуры
        );

// Иконка 16x16 в текстуре, отрисовывается 16x16 на экране
        context.drawTexture(
                TEXTURE,           // та же текстура
                x + 3,             // X на экране (с отступом 3 пикселя)
                y + 3,             // Y на экране (с отступом 3 пикселя)
                16,                // ширина отрисовки
                16,                // высота отрисовки
                slotIndex * 16,    // U: смещение по X в текстуре (16px per slot)
                16,                // V: смещение по Y в текстуре
                16,                // ширина области в текстуре
                16,                // высота области в текстуре
                64,                // ширина всей текстуры
                64                 // высота всей текстуры
        );
        // Получаем компонент для отображения маны
        PlayerComponent component = ModComponents.WOMDATA.get(client.player);

        // Рендерим количество маны
        String manaCost = String.format("%.0f", getManaCost(slotIndex));

        context.drawText(
                client.textRenderer,           // TextRenderer
                manaCost,                      // Текст
                x + SLOT_SIZE - 8,             // X позиция
                y + SLOT_SIZE - 8,             // Y позиция
                component.hasEnoughMana(getManaCost(slotIndex)) ? 0xFFFFFF : 0xFF5555,                     // Цвет
                true                           // Тень (shadow)
        );

        // Дополнительно: можно отображать текущую ману игрока
        if (slotIndex == 0) { // Только для первого слота
            String currentMana = String.format("%.0f/%.0f", component.getMana(), component.getMaxMana());
            context.drawText(
                    client.textRenderer,           // TextRenderer
                    currentMana,                      // Текст
                    x,             // X позиция
                    y -10,             // Y позиция
                    0x55FFFF,                     // Цвет
                    true                           // Тень (shadow)
            );
        }

    }
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

            if (client.options.hudHidden) return;

            int abilityScreenWidth = client.getWindow().getScaledWidth();
            int abilityScreenHeight = client.getWindow().getScaledHeight();

            // Позиции для слотов (3 слева и 3 справа от hotbar)
            int hotbarCenterX = abilityScreenHeight / 2;
            int hotbarY = abilityScreenWidth - 22;

            // Рендерим левые слоты
            for (int i = 0; i < 3; i++) {
                x = hotbarCenterX - 91 - (SLOT_SIZE + MARGIN) * (3 - i);
                renderAbilitySlot(drawContext, x, hotbarY, i, client);
            }

            // Рендерим правые слоты
            for (int i = 3; i < 6; i++) {
                x = hotbarCenterX + 91 + (SLOT_SIZE + MARGIN) * (i - 3);
                renderAbilitySlot(drawContext, x, hotbarY, i, client);
            }

        }
    }

