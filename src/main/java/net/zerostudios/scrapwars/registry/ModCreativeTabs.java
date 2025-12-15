package net.zerostudios.scrapwars.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.ScrapWars;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ScrapWars.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SCRAPWARS_TAB =
            CREATIVE_TABS.register("scrapwars_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.scrapwars"))
                    .icon(() -> new ItemStack(ModItems.SCRAP.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SCRAP.get());
                    })
                    .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
