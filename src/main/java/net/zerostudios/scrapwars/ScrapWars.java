package net.zerostudios.scrapwars;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.zerostudios.scrapwars.network.ModMessages;
import net.zerostudios.scrapwars.client.ClientSetup;
import net.zerostudios.scrapwars.common.util.ModIds;
import net.zerostudios.scrapwars.setup.ModBlockEntities;
import net.zerostudios.scrapwars.setup.ModBlocks;
import net.zerostudios.scrapwars.setup.ModCreativeTabs;
import net.zerostudios.scrapwars.setup.ModItems;
import net.zerostudios.scrapwars.setup.ModMenus;
import net.zerostudios.scrapwars.setup.ModRecipeSerializers;
import net.zerostudios.scrapwars.setup.ModRecipeTypes;

import org.slf4j.Logger;

@Mod(ModIds.MOD_ID)
public class ScrapWars {
    public static final Logger LOGGER = LogUtils.getLogger();

    public ScrapWars() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModMessages.register();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.register(modEventBus);
        }
    }
}
