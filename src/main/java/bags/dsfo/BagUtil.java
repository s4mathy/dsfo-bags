package bags.dsfo;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class BagUtil {
	public static void dropItem(ServerPlayer player, ItemStack stack) {
		ItemEntity entity = player.spawnAtLocation(stack);
		if (entity != null) {
			entity.setPickUpDelay(40);
		}
	}

	public static void notify(ServerPlayer player, Component message) {
		player.displayClientMessage(message, true);
	}
}
