package org.plugin.worldofmurloc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.plugin.worldofmurloc.ModComponents;
import org.plugin.worldofmurloc.ModConfigModel;
import org.plugin.worldofmurloc.Worldofmurloc;
import org.plugin.worldofmurloc.client.ModHud;
import org.plugin.worldofmurloc.util.XpHudState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void replaceOriginalExperienceBar(DrawContext context, int x, CallbackInfo ci) {
        if (client.player == null) return;
        var component = ModComponents.WOMDATA.get(client.player);
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        if (XpHudState.shouldShowModXp() || !Worldofmurloc.CONFIG.showMcXp()) {
            ci.cancel();
            ModHud.renderExperienceBar(context, component, client.textRenderer, screenWidth, screenHeight);
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void replaceXpLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (XpHudState.shouldShowModXp() || !Worldofmurloc.CONFIG.showMcXp()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void replaceOriginalHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        if (Worldofmurloc.CONFIG.uiType() == ModConfigModel.UI_TYPES.NEW) {
            ci.cancel();
            float currentHealth = player.getHealth() + 0.5f;
            ModHud.renderHealthBar(context, client, maxHealth, currentHealth);
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void replaceOriginalFoodBar(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        if (Worldofmurloc.CONFIG.uiType() == ModConfigModel.UI_TYPES.NEW) {
            ci.cancel();
            ModHud.renderHungerBar(context, client, player.getHungerManager().getFoodLevel() + 0.5f);
        }
    }
}
