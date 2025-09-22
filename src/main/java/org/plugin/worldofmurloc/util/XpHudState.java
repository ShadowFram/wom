package org.plugin.worldofmurloc.util;

public class XpHudState {
    private static int xpTimer = 0; // в тиках (20 тиков = 1 сек)
    
    public static void showXpBar() {
        xpTimer = 60; // 3 секунды * 20 тиков
    }
    
    public static boolean shouldShowModXp() {
        return xpTimer <= 0;
    }
    
    public static void tick() {
        if (xpTimer > 0) {
            xpTimer--;
        }
    }
}
