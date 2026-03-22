package net.zerostudios.scrapwars.common.recipe.improvised;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.zerostudios.scrapwars.common.workbench.WorkbenchCategory;
import net.zerostudios.scrapwars.common.workbench.WorkbenchIngredient;
import net.zerostudios.scrapwars.setup.ModRecipeSerializers;
import net.zerostudios.scrapwars.setup.ModRecipeTypes;

import java.util.List;

public class ImprovisedWorkbenchRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final WorkbenchCategory category;
    private final List<WorkbenchIngredient> ingredients;
    private final ItemStack result;

    public ImprovisedWorkbenchRecipe(ResourceLocation id,
                                     WorkbenchCategory category,
                                     List<WorkbenchIngredient> ingredients,
                                     ItemStack result) {
        this.id = id;
        this.category = category;
        this.ingredients = ingredients;
        this.result = result;
    }

    public WorkbenchCategory getCategory() {
        return category;
    }

    public List<WorkbenchIngredient> getIngredientsList() {
        return ingredients;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return false; // ya no usamos slots
    }

    @Override
    public ItemStack assemble(SimpleContainer container, net.minecraft.core.RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.IMPROVISED_WORKBENCH_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.IMPROVISED_WORKBENCH_RECIPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}