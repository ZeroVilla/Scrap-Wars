package net.zerostudios.scrapwars.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.ScrapWars;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, ScrapWars.MOD_ID);

    public static final RegistryObject<Item> SCRAP =
        ITEMS.register("scrap", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TECH_SCRAP =
        ITEMS.register("tech_scrap", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCRAP_FRAGMENTS =
        ITEMS.register("scrap_fragments", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MECHANICAL_COMPONENT =
        ITEMS.register("mechanical_component", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OLD_BATTERY =
        ITEMS.register("old_battery", () -> new Item(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
