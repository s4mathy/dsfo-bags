package bags.dsfo;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/** Tine la Bolsa Primitiva con uno o mas tintes (igual que la armadura de cuero). */
public class BagDyeRecipe extends CustomRecipe {
	public BagDyeRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean bag = false;
		boolean dye = false;
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.is(ModItems.BOLSA_3_FILAS)) {
				if (bag) return false;
				bag = true;
			} else if (stack.getItem() instanceof DyeItem) {
				dye = true;
			} else {
				return false;
			}
		}
		return bag && dye;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack bag = ItemStack.EMPTY;
		List<DyeItem> dyes = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.is(ModItems.BOLSA_3_FILAS)) {
				bag = stack;
			} else if (stack.getItem() instanceof DyeItem dyeItem) {
				dyes.add(dyeItem);
			}
		}
		return bag.isEmpty() ? ItemStack.EMPTY : DyedItemColor.applyDyes(bag, dyes);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.BAG_DYE;
	}
}
