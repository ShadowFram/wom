package org.plugin.worldofmurloc;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

	public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Worldofmurloc.MOD_ID, "xyeta"));
	public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModItems.SILVER_SWORD))
			.displayName(Text.translatable("itemGroup.xyeta"))
			.build();



	public static final FoodComponent ROTTEN_FOOD = new FoodComponent.Builder()
			.alwaysEdible() // ВСЕГДА МОЖНО СЪЕСТЬ
			.snack() // FOOD
			// The duration is in ticks, 20 ticks = 1 second
			.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 6 * 20, 1), 0.5f)
			.build(); // Создать(?)


	public static final Item a = register(
			new Item(new Item.Settings()),
			"a"
	);

	public static final Item POISONOUS_APPLE = register(
			new Item(new Item.Settings().food(ROTTEN_FOOD)), // Еда
			"poisonous_apple"
	);


	public static final Item SILVER_SWORD = register(new SwordItem(SilverMaterial.SILVER, new Item.Settings()
                        .attributeModifiers(SwordItem.createAttributeModifiers(SilverMaterial.SILVER, 3, -2.4f))),
            "silver_sword");

	public static final Item LIGHTSTICK = register(new LightningStick(new Item.Settings()),
			"lightstick");

	public static Item register(Item item, String id) {
		// Create the identifier for the item.
		Identifier itemID = Identifier.of(Worldofmurloc.MOD_ID, id);

        // Return the registered item!
		return Registry.register(Registries.ITEM, itemID, item);

	}
	public static void initialize() {
	Worldofmurloc.LOGGER.info("ХУЙНЯ-ПРЕДМЕТЫ ЗАПУСТИЛИСЬ");

	ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
				entries.add(SILVER_SWORD);
			}
			);
		// Register the group.
		Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

// Register items to the custom item group.
		ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.add(ModItems.a);
			itemGroup.add(ModItems.POISONOUS_APPLE);
			itemGroup.add(ModItems.SILVER_SWORD);
			// ...
		});
	}
}