package net.zerostudios.scrapwars.setup;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.menu.ImprovisedWorkbenchMenu;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModIds.MOD_ID);

    public static final RegistryObject<MenuType<ImprovisedWorkbenchMenu>> IMPROVISED_WORKBENCH_MENU =
            registerMenu("improvised_workbench", ImprovisedWorkbenchMenu::new);

    private ModMenus() {
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenu(
            String name,
            IContainerFactory<T> factory
    ) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}