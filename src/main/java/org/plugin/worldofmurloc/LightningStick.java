package org.plugin.worldofmurloc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightningStick extends Item {
	public LightningStick(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// Проверяем, что действие происходит на сервере
		if (!world.isClient && user instanceof ServerPlayerEntity player) {
			BlockPos frontOfPlayer = user.getBlockPos().offset(user.getHorizontalFacing(), 10);

			// Создаем молнию
			LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
			if (lightning != null) {
				lightning.refreshPositionAfterTeleport(frontOfPlayer.getX(), frontOfPlayer.getY(), frontOfPlayer.getZ());
				world.spawnEntity(lightning);
			}

			// Добавляем XP игроку
			XpSystem.addXp(player, 100);
			player.sendMessage(Text.literal("Вы получили 100 XP за использование молниевого жезла!"), false);

		}

		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}