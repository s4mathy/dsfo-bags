package bags.dsfo;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BagLimits {
   public static final int MAX_PRIMITIVA = 4;
   public static final int MAX_GLEBANOIDE = 1;
   public static final int MAX_ENDER = 1;
   public static final int MAX_PHORA = 1;
   private static final int CHECK_INTERVAL = 10;
   private static int ticks = 0;

   public static void init() {
      ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> {
         if (++ticks >= 10) {
            ticks = 0;

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
               if (!player.isCreative() && !player.isSpectator()) {
                  enforce(player, ModItems.BOLSA_3_FILAS, 4);
                  enforce(player, ModItems.BOLSA_4_FILAS, 1);
                  enforce(player, ModItems.BOLSA_ENDER, 1);
                  enforce(player, ModItems.BOLSA_PHORA, 1);
               }
            }
         }
      });
   }

   private static void enforce(ServerPlayer player, Item item, int max) {
      Inventory inv = player.getInventory();
      int kept = 0;
      Component name = null;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (stack.is(item)) {
            if (kept < max) {
               kept++;
            } else {
               name = stack.getHoverName();
               BagUtil.dropItem(player, stack.copy());
               inv.setItem(i, ItemStack.EMPTY);
            }
         }
      }

      if (name != null) {
         BagUtil.notify(player, Component.literal("Límite en el inventario: " + max + " × ").append(name));
      }
   }
}
