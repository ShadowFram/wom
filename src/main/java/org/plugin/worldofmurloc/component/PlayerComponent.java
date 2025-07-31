package org.plugin.worldofmurloc.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.plugin.worldofmurloc.ModComponents;

interface PlayerDataComponent extends Component {
    void setXp(int amount);
    int getXp();
    int getLvl();
    void setLvl(int amount);
    void addXp(int amount);
    int getXpForNewLevel();
    void levelUp();

    void sync(ServerPlayerEntity player);

    void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient);

    void applySyncPacket(PacketByteBuf buf);
}



public class PlayerComponent implements PlayerDataComponent, AutoSyncedComponent {
    private int xp = 0;
    private int level = 1;
    private final PlayerEntity player;

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }

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
        }
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        ModComponents.WOMDATA.sync(player);
    }

    private void sync() {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            sync(serverPlayer);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.xp = tag.getInt("xp");
        this.level = tag.getInt("level");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("xp", this.xp);
        tag.putInt("level", this.level);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeInt(this.xp);
        buf.writeInt(this.level);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.xp = buf.readInt();
        this.level = buf.readInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return this.player == player;
    }
}