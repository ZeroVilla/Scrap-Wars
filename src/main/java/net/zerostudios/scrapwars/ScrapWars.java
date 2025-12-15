package net.zerostudios.scrapwars;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.zerostudios.scrapwars.registry.ModCreativeTabs;
import net.zerostudios.scrapwars.registry.ModItems;

@Mod(ScrapWars.MOD_ID)
public class ScrapWars {

    public static final String MOD_ID = "scrapwars";

    public ScrapWars() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
