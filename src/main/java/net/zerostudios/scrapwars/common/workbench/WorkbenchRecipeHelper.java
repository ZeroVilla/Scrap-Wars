package net.zerostudios.scrapwars.common.workbench;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipe;

public class WorkbenchRecipeHelper {

    public static boolean hasMaterials(Player player, ImprovisedWorkbenchRecipe recipe) {

        for (WorkbenchIngredient ingredient : recipe.getIngredientsList()) {

            int needed = ingredient.count();
            int found = 0;

            for (ItemStack stack : player.getInventory().items) {
                if (ingredient.ingredient().test(stack)) {
                    found += stack.getCount();
                }
            }

            if (found < needed) {
                return false;
            }
        }

        return true;
    }

    public static void consumeMaterials(Player player, ImprovisedWorkbenchRecipe recipe) {

        for (WorkbenchIngredient ingredient : recipe.getIngredientsList()) {

            int remaining = ingredient.count();

            for (ItemStack stack : player.getInventory().items) {

                if (ingredient.ingredient().test(stack)) {

                    int remove = Math.min(stack.getCount(), remaining);
                    stack.shrink(remove);
                    remaining -= remove;

                    if (remaining <= 0) break;
                }
            }
        }
    }
}