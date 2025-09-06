package org.plugin.worldofmurloc.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import org.plugin.worldofmurloc.ModComponents;

interface PlayerDataComponent extends Component, CommonTickingComponent {
    // TODO: Почистить неиспользуемые методы
    // XP и уровни
    void setXp(int amount);
    int getXp();
    int getLvl();
    void setLvl(int amount);
    void addXp(int amount);
    int getXpForNewLevel();
    void levelUp();

    // Мана
    void setMana(float amount);
    float getMana();
    float getMaxMana();
    void addMana(float amount);
    void consumeMana(float amount);
    boolean hasEnoughMana(float amount);
    void regenerateMana();

    void sync(ServerPlayerEntity player);
    void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient);
    void applySyncPacket(PacketByteBuf buf);
}

public class PlayerComponent implements PlayerDataComponent, AutoSyncedComponent {
    private int xp = 0;
    private int level = 1;
    private float mana = 100.0f;
    private float maxMana = 100.0f;
    private final PlayerEntity player;

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    // XP методы
    @Override
    public void setXp(int amount) {
        this.xp = amount;
        sync();
    }

    @Override
    public int getXp() {
        return xp;
    }

    @Override
    public int getLvl() {
        return level;
    }

    @Override
    public void setLvl(int amount) {
        this.level = amount;
        this.maxMana = calculateMaxMana(); // Обновляем ману при изменении уровня
        sync();
    }

    @Override
    public void addXp(int amount) {
        this.xp += amount;
        levelUp();
        sync();
    }

    @Override
    public int getXpForNewLevel() {
        if (level <= 1) return 100;
        return (int)(100 * Math.pow(1.2, level - 1));
    }

    @Override
    public void levelUp() {
        while (this.xp >= getXpForNewLevel()) {
            this.xp -= getXpForNewLevel();
            this.level++;
            this.maxMana = calculateMaxMana(); // Обновляем ману при уровне
            this.mana = this.maxMana; // Восстанавливаем ману при уровне
        }
    }

    // Мана методы
    @Override
    public void setMana(float amount) {
        this.mana = Math.min(Math.max(amount, 0), this.maxMana);
        sync();
    }

    @Override
    public float getMana() {
        return mana;
    }

    @Override
    public float getMaxMana() {
        return maxMana;
    }

    @Override
    public void addMana(float amount) {
        setMana(this.mana + amount);
    }

    @Override
    public void consumeMana(float amount) {
        setMana(this.mana - amount);
    }

    @Override
    public boolean hasEnoughMana(float amount) {
        return this.mana >= amount;
    }

    @Override
    public void regenerateMana() {
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            float regenerationRate = 0.5f; // Регенерация в секунду
            addMana(regenerationRate / 20f); // Регенерация за тик
        }
    }

    private float calculateMaxMana() {
        // Максимальная мана увеличивается с уровнем
        return 100.0f + (level - 1) * 20.0f;
    }

    // Синхронизация
    @Override
    public void sync(ServerPlayerEntity player) {
        ModComponents.WOMDATA.sync(player);
    }

    private void sync() {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            sync(serverPlayer);
        }
    }

    // NBT
    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.xp = tag.getInt("xp");
        this.level = tag.getInt("level");
        this.mana = tag.getFloat("mana");
        this.maxMana = tag.getFloat("maxMana");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("xp", this.xp);
        tag.putInt("level", this.level);
        tag.putFloat("mana", this.mana);
        tag.putFloat("maxMana", this.maxMana);
    }

    // Сетевая синхронизация
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeInt(this.xp);
        buf.writeInt(this.level);
        buf.writeFloat(this.mana);
        buf.writeFloat(this.maxMana);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.xp = buf.readInt();
        this.level = buf.readInt();
        this.mana = buf.readFloat();
        this.maxMana = buf.readFloat();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return this.player == player;
    }

    @Override
    public void tick() {

    }
}