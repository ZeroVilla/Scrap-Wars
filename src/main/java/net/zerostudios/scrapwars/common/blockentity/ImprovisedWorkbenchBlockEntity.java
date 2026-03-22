package net.zerostudios.scrapwars.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zerostudios.scrapwars.common.menu.ImprovisedWorkbenchMenu;
import net.zerostudios.scrapwars.setup.ModBlockEntities;

import javax.annotation.Nullable;

public class ImprovisedWorkbenchBlockEntity extends BlockEntity implements MenuProvider {

    public ImprovisedWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IMPROVISED_WORKBENCH_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.scrapwars.improvised_workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ImprovisedWorkbenchMenu(containerId, playerInventory, worldPosition);
    }
}