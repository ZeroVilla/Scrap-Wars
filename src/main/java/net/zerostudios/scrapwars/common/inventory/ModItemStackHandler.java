package net.zerostudios.scrapwars.common.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ModItemStackHandler extends ItemStackHandler {
    private final Runnable onContentsChanged;
    private boolean suppressUpdates = false;

    public ModItemStackHandler(int size, Runnable onContentsChanged) {
        super(size);
        this.onContentsChanged = onContentsChanged;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (!suppressUpdates && onContentsChanged != null) {
            onContentsChanged.run();
        }
    }

    public void setStackInSlotNoUpdate(int slot, ItemStack stack) {
        suppressUpdates = true;
        setStackInSlot(slot, stack);
        suppressUpdates = false;
    }
}