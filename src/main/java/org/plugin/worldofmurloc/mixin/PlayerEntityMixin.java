package org.plugin.worldofmurloc.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.plugin.worldofmurloc.util.XpHudState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "addExperience", at = @At("HEAD"))
    private void onAddExperience(int experience, CallbackInfo ci) {
        if (experience > 0) {
            XpHudState.showXpBar();
        }
    }
}
