package org.plugin.worldofmurloc.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.plugin.worldofmurloc.ModComponents;

interface PlayerDataComponent extends Component, ServerTickingComponent {
    // XP и уровни
    void setXp(int amount);
    int getXp();
    int getLvl();
    void setLvl(int amount);
    void addXp(int amount);
    int getXpForNewLevel();

    // Мана
    void setMana(float amount);
    float getManaSpeedMulti();
    void setManaSpeed(float amount); // устанавливает мультипликатор (2.1 быстрее, 0.5 медленнее)
    float getMana();
    float getMaxMana();
    void addMana(float amount);
    void consumeMana(float amount);
    boolean hasEnoughMana(float amount);
}

public class PlayerComponent implements PlayerDataComponent, AutoSyncedComponent {
    private static final int DEFAULT_BASE_TICKS_PER_MANA = 10;
    private final PlayerEntity player;
    private int xp = 0;
    private int level = 1;
    private float mana = 100.0f;
    private float maxMana = 100.0f;
    private float manaSpeedMulti = 1.0f;
    private float manaAccumulator = 0.0f;

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
        this.maxMana = calculateMaxMana();
        this.mana = this.maxMana;
    }

    // XP методы
    @Override
    public void setXp(int amount) {
        this.xp = amount;
        ModComponents.WOMDATA.sync(player);
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
        this.level = Math.max(1, amount);
        this.maxMana = calculateMaxMana();
        this.mana = Math.min(this.mana, this.maxMana);
        ModComponents.WOMDATA.sync(player);
    }

    @Override
    public void addXp(int amount) {
        this.xp += amount;
        levelUp();
        ModComponents.WOMDATA.sync(player);
    }

    @Override
    public int getXpForNewLevel() {
        if (level <= 1) return 100;
        return (int) (100 * Math.pow(1.2, level - 1));
    }

    private void levelUp() {
        while (this.xp >= getXpForNewLevel()) {
            this.xp -= getXpForNewLevel();
            this.level++;
            this.maxMana = calculateMaxMana();
            this.mana = this.maxMana;
        }
    }

    // Мана методы
    @Override
    public void setMana(float amount) {
        float clamped = Math.min(Math.max(amount, 0f), this.maxMana);
        if (Math.abs(this.mana - clamped) > 0.0001f) {
            this.mana = clamped;
            ModComponents.WOMDATA.sync(player);
        }
    }

    @Override
    public float getManaSpeedMulti() {
        return manaSpeedMulti;
    }

    @Override
    public void setManaSpeed(float amount) {
        if (amount <= 0f) {
            amount = 0.0001f; // защита от нуля/отрицательных
        }
        this.manaSpeedMulti = amount;
        ModComponents.WOMDATA.sync(player);
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

    private void regenerateManaPerTick() {
        if (this.mana >= this.maxMana) return;
        // система регена
        float regenPerTick = this.manaSpeedMulti / (float) DEFAULT_BASE_TICKS_PER_MANA;

        this.manaAccumulator += regenPerTick;

        if (this.manaAccumulator >= 1.0f) {
            int give = (int) Math.floor(this.manaAccumulator);
            addMana(give);
            this.manaAccumulator -= give;
        }
    }

    private float calculateMaxMana() {
        return 100.0f + (level - 1) * 20.0f;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.xp = tag.getInt("xp");
        this.level = tag.getInt("level");
        this.mana = tag.getFloat("mana");
        this.maxMana = tag.getFloat("maxMana");
        this.manaAccumulator = tag.getFloat("manaAccumulator");
        this.manaSpeedMulti = tag.getFloat("manaSpeedMulti");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("xp", this.xp);
        tag.putInt("level", this.level);
        tag.putFloat("mana", this.mana);
        tag.putFloat("maxMana", this.maxMana);
        tag.putFloat("manaSpeedMulti", this.manaSpeedMulti);
        tag.putFloat("manaAccumulator", this.manaAccumulator);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return this.player == player;
    }

    @Override
    public void serverTick() {
        regenerateManaPerTick();
    }
}