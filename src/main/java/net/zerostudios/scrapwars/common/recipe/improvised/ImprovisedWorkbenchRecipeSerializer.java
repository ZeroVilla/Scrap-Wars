package net.zerostudios.scrapwars.common.recipe.improvised;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.zerostudios.scrapwars.common.workbench.WorkbenchCategory;
import net.zerostudios.scrapwars.common.workbench.WorkbenchIngredient;

import java.util.ArrayList;
import java.util.List;

public class ImprovisedWorkbenchRecipeSerializer implements RecipeSerializer<ImprovisedWorkbenchRecipe> {

    @Override
    public ImprovisedWorkbenchRecipe fromJson(ResourceLocation id, JsonObject json) {

        String categoryId = GsonHelper.getAsString(json, "category", "all");
        WorkbenchCategory category = WorkbenchCategory.fromId(categoryId);

        JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");

        List<WorkbenchIngredient> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientsJson.size(); i++) {
            JsonObject obj = ingredientsJson.get(i).getAsJsonObject();

            Ingredient ingredient = Ingredient.fromJson(obj.get("item"));
            int count = GsonHelper.getAsInt(obj, "count", 1);

            ingredients.add(new WorkbenchIngredient(ingredient, count));
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

        return new ImprovisedWorkbenchRecipe(id, category, ingredients, result);
    }

    @Override
    public ImprovisedWorkbenchRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        WorkbenchCategory category = WorkbenchCategory.values()[buf.readInt()];

        int size = buf.readInt();
        List<WorkbenchIngredient> ingredients = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Ingredient ing = Ingredient.fromNetwork(buf);
            int count = buf.readInt();
            ingredients.add(new WorkbenchIngredient(ing, count));
        }

        ItemStack result = buf.readItem();

        return new ImprovisedWorkbenchRecipe(id, category, ingredients, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, ImprovisedWorkbenchRecipe recipe) {

        buf.writeInt(recipe.getCategory().ordinal());

        buf.writeInt(recipe.getIngredientsList().size());

        for (WorkbenchIngredient ing : recipe.getIngredientsList()) {
            ing.ingredient().toNetwork(buf);
            buf.writeInt(ing.count());
        }

        buf.writeItem(recipe.getResultItem(net.minecraft.core.RegistryAccess.EMPTY));
    }
}