package net.zerostudios.scrapwars.common.recipe.improvised;

import net.minecraft.world.item.crafting.RecipeType;

public class ImprovisedWorkbenchRecipeType implements RecipeType<ImprovisedWorkbenchRecipe> {
    public static final ImprovisedWorkbenchRecipeType INSTANCE = new ImprovisedWorkbenchRecipeType();
    public static final String ID = "improvised_workbench";

    private ImprovisedWorkbenchRecipeType() {
    }

    @Override
    public String toString() {
        return ID;
    }
}