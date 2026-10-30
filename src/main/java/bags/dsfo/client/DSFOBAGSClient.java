package bags.dsfo.client;

import bags.dsfo.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.component.DyedItemColor;

public class DSFOBAGSClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// En 1.21.1 el tinte lo da el ColorProvider (en 1.21.4+ lo daba items/*.json)
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> tintIndex == 0 ? DyedItemColor.getOrDefault(stack, -1) : -1,
			ModItems.BOLSA_3_FILAS
		);
	}
}
