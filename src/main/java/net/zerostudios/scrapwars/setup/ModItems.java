package net.zerostudios.scrapwars.setup;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModIds.MOD_ID);

    public static final RegistryObject<Item> SCRAP_METAL = registerSimpleItem("scrap_metal");
    public static final RegistryObject<Item> WIRE_BUNDLE = registerSimpleItem("wire_bundle");
    public static final RegistryObject<Item> MECHANICAL_PARTS = registerSimpleItem("mechanical_parts");
    public static final RegistryObject<Item> CIRCUIT_FRAGMENT = registerSimpleItem("circuit_fragment");
    public static final RegistryObject<Item> DAMAGED_BATTERY = registerSimpleItem("damaged_battery");
    public static final RegistryObject<Item> METAL_PLATE = registerSimpleItem("metal_plate");

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> registerSimpleItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }
}
