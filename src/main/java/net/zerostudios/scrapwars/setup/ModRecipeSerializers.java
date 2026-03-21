package net.zerostudios.scrapwars.setup;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.util.ModIds;

import java.util.function.Supplier;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModIds.MOD_ID);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }

    public static <T extends RecipeSerializer<?>> RegistryObject<T> registerSerializer(String name, Supplier<T> supplier) {
        return RECIPE_SERIALIZERS.register(name, supplier);
    }
}