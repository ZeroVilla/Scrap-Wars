package net.zerostudios.scrapwars.setup;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zerostudios.scrapwars.common.blockentity.ImprovisedWorkbenchBlockEntity;
import net.zerostudios.scrapwars.common.util.ModIds;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModIds.MOD_ID);

    public static final RegistryObject<BlockEntityType<ImprovisedWorkbenchBlockEntity>> IMPROVISED_WORKBENCH_BE =
            BLOCK_ENTITIES.register("improvised_workbench",
                    () -> BlockEntityType.Builder.of(
                            ImprovisedWorkbenchBlockEntity::new,
                            ModBlocks.IMPROVISED_WORKBENCH.get()
                    ).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
