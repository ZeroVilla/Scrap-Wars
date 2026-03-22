package net.zerostudios.scrapwars.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.zerostudios.scrapwars.client.screen.ImprovisedWorkbenchScreen;
import net.zerostudios.scrapwars.setup.ModMenus;

public final class ClientSetup {

    private ClientSetup() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientSetup::onClientSetup);
    }

    private static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                MenuScreens.register(ModMenus.IMPROVISED_WORKBENCH_MENU.get(), ImprovisedWorkbenchScreen::new)
        );
    }
}
