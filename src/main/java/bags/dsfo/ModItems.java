package bags.dsfo;

import java.util.function.Function;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {
	public static final Item BOLSA_3_FILAS = register("bolsa_3_filas", p -> new BagItem(p, 3), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final Item BOLSA_4_FILAS = register("bolsa_4_filas", p -> new BagItem(p, 4), new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	public static final Item BOLSA_ENDER = register("bolsa_ender", EnderBagItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
	public static final Item BOLSA_PHORA = register("bolsa_phora", p -> new BagItem(p, 6), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

	private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
		return Registry.register(BuiltInRegistries.ITEM, DSFOBAGS.id(name), factory.apply(properties));
	}

	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.accept(BOLSA_3_FILAS);
			entries.accept(BOLSA_4_FILAS);
			entries.accept(BOLSA_ENDER);
			entries.accept(BOLSA_PHORA);
		});
	}
}
