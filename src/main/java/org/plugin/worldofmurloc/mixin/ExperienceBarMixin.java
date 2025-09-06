package org.plugin.worldofmurloc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.MathHelper;
import org.plugin.worldofmurloc.ModComponents;
import org.plugin.worldofmurloc.util.XpHudState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ExperienceBarMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void showOriginalExpirienceBar(DrawContext context, int x, CallbackInfo ci) {
        if (client.player == null) return;

        if (!XpHudState.shouldShowXp()) {
            var component = ModComponents.WOMDATA.get(client.player);
            int screenWidth = context.getScaledWindowWidth();
            int screenHeight = context.getScaledWindowHeight();
            int barWidth = 182;
            int barHeight = 5;
            int barX = (screenWidth - barWidth) / 2;
            int barY = screenHeight - 32;

            float progress = component.getXpForNewLevel() > 0 ? (float) component.getXp() / component.getXpForNewLevel() : 0f;
            progress = MathHelper.clamp(progress, 0f, 1f);

            int filledWidth = (int) (progress * barWidth);

            context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF7F7F7F); // Фон
            context.fill(barX, barY, barX + filledWidth, barY + barHeight, 0xFF3D00AF); // Заполнение

            String text = component.getXp() + "/" + component.getXpForNewLevel();
            context.drawText(
                    client.textRenderer, text,
                    barX + barWidth / 2 - client.textRenderer.getWidth(text) / 2,
                    barY - 3, 0xFFFFFF, true
            );
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void replaceXpLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!XpHudState.shouldShowXp()) {
            ci.cancel();
        }
    }
}
