package bags.dsfo;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnderBagItem extends Item {
	public EnderBagItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			PlayerEnderChestContainer enderChest = player.getEnderChestInventory();
			player.openMenu(new SimpleMenuProvider(
				(containerId, playerInv, p) -> new BagMenu(MenuType.GENERIC_9x3, containerId, playerInv, enderChest, 3, s -> !(s.getItem() instanceof EnderBagItem), stack),
				stack.getHoverName()
			));
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
