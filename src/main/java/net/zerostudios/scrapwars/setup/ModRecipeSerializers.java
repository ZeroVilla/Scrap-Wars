package net.zerostudios.scrapwars.setup;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipeSerializer;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModIds.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> IMPROVISED_WORKBENCH_SERIALIZER =
            RECIPE_SERIALIZERS.register("improvised_workbench", ImprovisedWorkbenchRecipeSerializer::new);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}