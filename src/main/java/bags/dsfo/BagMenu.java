package bags.dsfo;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BagMenu extends AbstractContainerMenu {
   private final int rows;
   private final ItemStack bag;

   public BagMenu(MenuType<?> type, int containerId, Inventory playerInv, Container container, int rows, Predicate<ItemStack> canStore, ItemStack bag) {
      super(type, containerId);
      checkContainerSize(container, rows * 9);
      this.rows = rows;
      this.bag = bag;

      for (int r = 0; r < rows; r++) {
         for (int c = 0; c < 9; c++) {
            this.addSlot(new BagMenu.RestrictedSlot(container, c + r * 9, 8 + c * 18, 18 + r * 18, canStore));
         }
      }

      int offset = (rows - 4) * 18;

      for (int r = 0; r < 3; r++) {
         for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(playerInv, c + r * 9 + 9, 8 + c * 18, 103 + r * 18 + offset));
         }
      }

      for (int c = 0; c < 9; c++) {
         this.addSlot(new Slot(playerInv, c, 8 + c * 18, 161 + offset));
      }
   }

   public static boolean isBag(ItemStack stack) {
      return stack.getItem() instanceof BagItem || stack.getItem() instanceof EnderBagItem;
   }

   public boolean stillValid(Player player) {
      Inventory inv = player.getInventory();

      for (int i = 0; i < inv.getContainerSize(); i++) {
         if (inv.getItem(i) == this.bag) {
            return true;
         }
      }

      return false;
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack result = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot.hasItem()) {
         ItemStack stack = slot.getItem();
         result = stack.copy();
         int bagSlots = this.rows * 9;
         if (index < bagSlots) {
            if (!this.moveItemStackTo(stack, bagSlots, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(stack, 0, bagSlots, false)) {
            return ItemStack.EMPTY;
         }

         if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return result;
   }

   private static class RestrictedSlot extends Slot {
      private final Predicate<ItemStack> canStore;

      RestrictedSlot(Container container, int index, int x, int y, Predicate<ItemStack> canStore) {
         super(container, index, x, y);
         this.canStore = canStore;
      }

      public boolean mayPlace(ItemStack stack) {
         return this.canStore.test(stack);
      }
   }
}
