package net.zerostudios.scrapwars.setup;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipe;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipeType;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ModIds.MOD_ID);

    public static final RegistryObject<RecipeType<ImprovisedWorkbenchRecipe>> IMPROVISED_WORKBENCH_RECIPE =
            RECIPE_TYPES.register("improvised_workbench", () -> ImprovisedWorkbenchRecipeType.INSTANCE);

    private ModRecipeTypes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}