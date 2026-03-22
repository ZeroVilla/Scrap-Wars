package net.zerostudios.scrapwars.common.workbench;

import net.minecraft.world.item.crafting.Ingredient;

public class WorkbenchIngredient {
    private final Ingredient ingredient;
    private final int count;

    public WorkbenchIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public Ingredient ingredient() {
        return ingredient;
    }

    public int count() {
        return count;
    }
}