package bags.dsfo;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public class BagItem extends Item {
	private final int rows;

	public BagItem(Properties properties, int rows) {
		super(properties);
		this.rows = rows;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			int size = rows * 9;
			BagContainer container = new BagContainer(size, stack);
			ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
			NonNullList<ItemStack> saved = NonNullList.withSize(size, ItemStack.EMPTY);
			contents.copyInto(saved);
			for (int i = 0; i < size; i++) {
				container.setItem(i, saved.get(i));
			}
			container.finishLoading();

			player.openMenu(new SimpleMenuProvider(
				(containerId, playerInv, p) -> new BagMenu(menuTypeFor(rows), containerId, playerInv, container, rows, s -> !BagMenu.isBag(s), stack),
				stack.getHoverName()
			));
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	private static void save(ItemStack stack, SimpleContainer container) {
		NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < items.size(); i++) {
			items.set(i, container.getItem(i));
		}
		stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
	}

	private static MenuType<?> menuTypeFor(int rows) {
		return switch (rows) {
			case 3 -> MenuType.GENERIC_9x3;
			case 4 -> MenuType.GENERIC_9x4;
			case 6 -> MenuType.GENERIC_9x6;
			default -> throw new IllegalArgumentException("Filas no soportadas: " + rows);
		};
	}

	private static class BagContainer extends SimpleContainer {
		private final ItemStack bag;
		private boolean loading = true;

		BagContainer(int size, ItemStack bag) {
			super(size);
			this.bag = bag;
		}

		void finishLoading() {
			loading = false;
		}

		@Override
		public void setChanged() {
			super.setChanged();
			if (!loading) {
				save(bag, this);
			}
		}
	}
}
