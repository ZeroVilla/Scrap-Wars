package net.zerostudios.scrapwars.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.zerostudios.scrapwars.common.blockentity.ImprovisedWorkbenchBlockEntity;
import net.zerostudios.scrapwars.common.inventory.slot.OutputSlot;
import net.zerostudios.scrapwars.setup.ModMenus;

public class ImprovisedWorkbenchMenu extends AbstractContainerMenu {
    private final ImprovisedWorkbenchBlockEntity blockEntity;

    public ImprovisedWorkbenchMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData));
    }

    public ImprovisedWorkbenchMenu(int containerId, Inventory playerInventory, ImprovisedWorkbenchBlockEntity blockEntity) {
        super(ModMenus.IMPROVISED_WORKBENCH_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        addWorkbenchSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private static ImprovisedWorkbenchBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf extraData) {
        BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(extraData.readBlockPos());
        if (!(blockEntity instanceof ImprovisedWorkbenchBlockEntity workbench)) {
            throw new IllegalStateException("Expected ImprovisedWorkbenchBlockEntity");
        }
        return workbench;
    }

    private void addWorkbenchSlots() {
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 44, 35));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 62, 35));
        addSlot(new OutputSlot(blockEntity.getItemHandler(), 2, 120, 35) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                blockEntity.craftItem();
                super.onTake(player, stack);
            }
        });
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int slot = 0; slot < 9; slot++) {
            addSlot(new Slot(inventory, slot, 8 + slot * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(
                ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player,
                blockEntity.getBlockState().getBlock()
        );
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public ImprovisedWorkbenchBlockEntity getBlockEntity() {
        return blockEntity;
    }
}