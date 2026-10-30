package bags.dsfo;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class ContainerRules {
   public static final int MAX_PRIMITIVA_EN_COFRE = 1;
   public static final int MAX_GLEBANOIDE_EN_COFRE = 1;
   public static final int MAX_PHORA_EN_COFRE = 1;

   public static void init() {
      ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            check(player);
         }
      });
   }

   private static void check(ServerPlayer player) {
      int primitivas = 0;
      int glebanoides = 0;
      int phoras = 0;
      String message = null;

      for (Slot slot : player.containerMenu.slots) {
         Container container = slot.container;
         if (container != player.getInventory()) {
            ItemStack stack = slot.getItem();
            if (!stack.isEmpty() && BagMenu.isBag(stack)) {
               boolean eject = false;
               if (container instanceof ShulkerBoxBlockEntity) {
                  eject = true;
                  message = "Las bolsas no caben en shulker boxes";
               } else if (container instanceof ChestBlockEntity || container instanceof CompoundContainer) {
                  if (stack.is(ModItems.BOLSA_3_FILAS)) {
                     primitivas++;
                     eject = primitivas > 1;
                  } else if (stack.is(ModItems.BOLSA_4_FILAS)) {
                     glebanoides++;
                     eject = glebanoides > 1;
                  } else if (stack.is(ModItems.BOLSA_PHORA)) {
                     phoras++;
                     eject = phoras > 1;
                  }

                  if (eject) {
                     message = "Solo cabe 1 bolsa de cada tipo en un cofre";
                  }
               }

               if (eject) {
                  container.setItem(slot.getContainerSlot(), ItemStack.EMPTY);
                  BagUtil.dropItem(player, stack.copy());
               }
            }
         }
      }

      if (message != null) {
         BagUtil.notify(player, Component.literal(message));
      }
   }
}
