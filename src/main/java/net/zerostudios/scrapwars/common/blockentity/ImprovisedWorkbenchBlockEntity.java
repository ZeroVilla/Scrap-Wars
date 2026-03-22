package net.zerostudios.scrapwars.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.zerostudios.scrapwars.common.inventory.ModItemStackHandler;
import net.zerostudios.scrapwars.common.menu.ImprovisedWorkbenchMenu;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipe;
import net.zerostudios.scrapwars.setup.ModBlockEntities;
import net.zerostudios.scrapwars.setup.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.Optional;

public class ImprovisedWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_INPUT_A = 0;
    public static final int SLOT_INPUT_B = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_COUNT = 3;

    private final ModItemStackHandler itemHandler = new ModItemStackHandler(SLOT_COUNT, this::onInventoryChanged);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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
        return new ImprovisedWorkbenchMenu(containerId, playerInventory, this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public ModItemStackHandler getItemHandler() {
        return itemHandler;
    }

    private void onInventoryChanged() {
        if (level == null || level.isClientSide) {
            return;
        }

        updateOutputSlot();
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    private void updateOutputSlot() {
        Optional<ImprovisedWorkbenchRecipe> match = getCurrentRecipe();
        if (match.isPresent()) {
            ItemStack result = match.get().getResultItem(level.registryAccess()).copy();
            itemHandler.setStackInSlotNoUpdate(SLOT_OUTPUT, result);
        } else {
            itemHandler.setStackInSlotNoUpdate(SLOT_OUTPUT, ItemStack.EMPTY);
        }
    }

    public Optional<ImprovisedWorkbenchRecipe> getCurrentRecipe() {
        if (level == null) {
            return Optional.empty();
        }

        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, itemHandler.getStackInSlot(SLOT_INPUT_A));
        container.setItem(1, itemHandler.getStackInSlot(SLOT_INPUT_B));

        return level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.IMPROVISED_WORKBENCH_RECIPE.get(), container, level);
    }

    public boolean hasRecipe() {
        return getCurrentRecipe().isPresent();
    }

    public void craftItem() {
        Optional<ImprovisedWorkbenchRecipe> match = getCurrentRecipe();
        if (match.isEmpty() || level == null) {
            return;
        }

        ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);
        if (output.isEmpty()) {
            return;
        }

        itemHandler.extractItem(SLOT_INPUT_A, 1, false);
        itemHandler.extractItem(SLOT_INPUT_B, 1, false);

        updateOutputSlot();
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
    }
}