package org.plugin.worldofmurloc;

import net.minecraft.nbt.NbtCompound;

public class PlayerData {
    private int level;
    private int xp;

    // Запись данных в NBT
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("Level", this.level);
        nbt.putInt("Xp", this.xp);
        return nbt;
    }

    // Чтение данных из NBT
    public void fromNbt(NbtCompound nbt) {
        this.level = nbt.getInt("Level");
        this.xp = nbt.getInt("Xp");
    }



}