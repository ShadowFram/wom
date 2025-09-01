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

public class AbilityHudRenderer implements HudRenderCallback {
    private static final Identifier TEXTURE = Identifier.of(Worldofmurloc.MOD_ID, "textures/gui/experience_bar.png"); //rename to abilities
    private static final int SLOT_SIZE = 22;
    private static final int MARGIN = 2;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();


        if (client.options.hudHidden) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Позиции для слотов (3 слева и 3 справа от hotbar)
        int hotbarCenterX = screenWidth / 2;
        int hotbarY = screenHeight - 22;

        // Рендерим левые слоты
        for (int i = 0; i < 3; i++) {
            int x = hotbarCenterX - 91 - (SLOT_SIZE + MARGIN) * (3 - i);
            renderAbilitySlot(drawContext, x, hotbarY, i, client);
        }

        // Рендерим правые слоты
        for (int i = 3; i < 6; i++) {
            int x = hotbarCenterX + 91 + (SLOT_SIZE + MARGIN) * (i - 3);
            renderAbilitySlot(drawContext, x, hotbarY, i, client);
        }
    }

    private void renderAbilitySlot(DrawContext context, int x, int y, int slotIndex, MinecraftClient client) {

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

    private float getManaCost(int slotIndex) {
        return switch (slotIndex) {
            case 0 -> 10.0f;
            case 1 -> 15.0f;
            case 2 -> 20.0f;
            case 3 -> 25.0f;
            case 4 -> 30.0f;
            case 5 -> 35.0f;
            default -> 0.0f;
        };
        }
}