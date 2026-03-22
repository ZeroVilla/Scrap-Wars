package net.zerostudios.scrapwars.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.block.ImprovisedWorkbenchBlock;
import net.zerostudios.scrapwars.common.block.ScrapPileBlock;
import net.zerostudios.scrapwars.common.util.ModIds;

import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ModIds.MOD_ID);

    public static final RegistryObject<Block> SCRAP_PILE =
            registerBlock("scrap_pile", ScrapPileBlock::new);

    public static final RegistryObject<Block> IMPROVISED_WORKBENCH =
            registerBlock("improvised_workbench", ImprovisedWorkbenchBlock::new);

    private ModBlocks() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, block);
        return block;
    }

    @SuppressWarnings("null")
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
