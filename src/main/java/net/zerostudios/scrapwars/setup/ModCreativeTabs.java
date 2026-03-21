package net.zerostudios.scrapwars.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModIds.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SCRAP_WARS_TAB = CREATIVE_MODE_TABS.register(
            "scrap_wars_tab",
            () -> CreativeModeTab.builder()
                    .title(scrapWarsTabTitle())
                    .icon(() -> new ItemStack(ModItems.SCRAP_METAL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SCRAP_METAL.get());
                        output.accept(ModItems.WIRE_BUNDLE.get());
                        output.accept(ModItems.MECHANICAL_PARTS.get());
                        output.accept(ModItems.CIRCUIT_FRAGMENT.get());
                        output.accept(ModItems.DAMAGED_BATTERY.get());
                        output.accept(ModBlocks.SCRAP_PILE.get());
                    })
                    .build()
    );

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    @SuppressWarnings("null")
    private static Component scrapWarsTabTitle() {
        return Component.translatable("item_group.scrapwars.scrap_wars");
    }
}