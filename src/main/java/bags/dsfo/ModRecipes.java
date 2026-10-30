package bags.dsfo;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
	public static final RecipeSerializer<BagDyeRecipe> BAG_DYE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		DSFOBAGS.id("crafting_bag_dye"),
		new SimpleCraftingRecipeSerializer<>(BagDyeRecipe::new)
	);

	public static void init() {
	}
}
