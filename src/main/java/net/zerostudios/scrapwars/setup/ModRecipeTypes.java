package net.zerostudios.scrapwars.setup;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ModIds.MOD_ID);

    private ModRecipeTypes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }

    public static <T extends RecipeType<?>> RegistryObject<T> registerType(String name, java.util.function.Supplier<T> supplier) {
        return RECIPE_TYPES.register(name, supplier);
    }
}