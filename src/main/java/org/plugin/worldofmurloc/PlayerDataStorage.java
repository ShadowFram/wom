//package org.plugin.worldofmurloc;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.server.network.ServerPlayerEntity;
//
//
//public class PlayerDataStorage {
//    public final class PlayerDataManager {
//        private static final String DATA_KEY = "WomData";
//
//        // Получение данных
//        public static PlayerData get(ServerPlayerEntity player) {
//            NbtCompound nbt = player.getComponents();
//            PlayerData data = new PlayerData();
//
//            if (nbt != null && nbt.contains(DATA_KEY)) {
//                data.fromNbt(nbt.getCompound(DATA_KEY));
//            }
//            return data;
//        }
//
//        // Сохранение данных
//        public static void save(ServerPlayerEntity player, PlayerData data) {
//            NbtCompound nbt = player.getNbt() != null ? player.getNbt() : new NbtCompound();
//            nbt.put(DATA_KEY, data.toNbt());
//            player.setNbt(nbt);
//        }
//    }
//}