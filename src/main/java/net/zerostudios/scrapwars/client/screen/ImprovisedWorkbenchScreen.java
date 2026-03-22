package net.zerostudios.scrapwars.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.zerostudios.scrapwars.common.menu.ImprovisedWorkbenchMenu;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipe;
import net.zerostudios.scrapwars.common.util.ModLoc;
import net.zerostudios.scrapwars.common.workbench.WorkbenchCategory;
import net.zerostudios.scrapwars.common.workbench.WorkbenchIngredient;
import net.zerostudios.scrapwars.common.workbench.WorkbenchRecipeHelper;
import net.zerostudios.scrapwars.network.CraftSelectedRecipeC2SPacket;
import net.zerostudios.scrapwars.network.ModMessages;
import net.zerostudios.scrapwars.setup.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;

public class ImprovisedWorkbenchScreen extends AbstractContainerScreen<ImprovisedWorkbenchMenu> {
    private static final ResourceLocation TEXTURE = ModLoc.of("textures/gui/improvised_workbench_v2.png");

    private static final int LIST_X = 8;
    private static final int LIST_Y = 36;
    private static final int LIST_WIDTH = 122;
    private static final int ENTRY_HEIGHT = 20;
    private static final int MAX_VISIBLE_RECIPES = 5;

    private WorkbenchCategory selectedCategory = WorkbenchCategory.ALL;
    private final List<ImprovisedWorkbenchRecipe> visibleRecipes = new ArrayList<>();
    private int selectedRecipeIndex = -1;
    private int scrollOffset = 0;

    private Button craftButton;

    public ImprovisedWorkbenchScreen(ImprovisedWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 230;
        this.imageHeight = 207;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 113;
    }

    @Override
    protected void init() {
        super.init();

        rebuildRecipeList();

        craftButton = Button.builder(Component.translatable("gui.scrapwars.craft"), button -> craftSelected())
                .bounds(leftPos + 146, topPos + 88, 70, 20)
                .build();
        addRenderableWidget(craftButton);

        addCategoryButtons();
        updateCraftButtonState();
    }

    private void addCategoryButtons() {
        int startX = leftPos + 8;
        int y = topPos + 18;
        int width = 34;
        int height = 16;
        int gap = 2;

        addRenderableWidget(Button.builder(tabTitle(WorkbenchCategory.ALL),
                b -> setCategory(WorkbenchCategory.ALL)).bounds(startX, y, width, height).build());

        addRenderableWidget(Button.builder(tabTitle(WorkbenchCategory.WEAPONS),
                b -> setCategory(WorkbenchCategory.WEAPONS)).bounds(startX + (width + gap), y, width, height).build());

        addRenderableWidget(Button.builder(tabTitle(WorkbenchCategory.TOOLS),
                b -> setCategory(WorkbenchCategory.TOOLS)).bounds(startX + 2 * (width + gap), y, width, height).build());

        addRenderableWidget(Button.builder(tabTitle(WorkbenchCategory.ELECTRONICS),
                b -> setCategory(WorkbenchCategory.ELECTRONICS)).bounds(startX + 3 * (width + gap), y, width, height).build());

        addRenderableWidget(Button.builder(tabTitle(WorkbenchCategory.COMPONENTS),
                b -> setCategory(WorkbenchCategory.COMPONENTS)).bounds(startX + 4 * (width + gap), y, width, height).build());

        addRenderableWidget(Button.builder(tabTitle(WorkbenchCategory.MATERIALS),
                b -> setCategory(WorkbenchCategory.MATERIALS)).bounds(startX + 5 * (width + gap), y, width, height).build());
    }

    private MutableComponent tabTitle(WorkbenchCategory category) {
        MutableComponent base = categoryComponent(category);
        return isCategorySelected(category)
                ? Component.literal("[").append(base).append("]")
                : base;
    }

    private MutableComponent categoryComponent(WorkbenchCategory category) {
        return switch (category) {
            case ALL -> Component.translatable("gui.scrapwars.category.all");
            case WEAPONS -> Component.translatable("gui.scrapwars.category.weapons");
            case TOOLS -> Component.translatable("gui.scrapwars.category.tools");
            case ELECTRONICS -> Component.translatable("gui.scrapwars.category.electronics");
            case COMPONENTS -> Component.translatable("gui.scrapwars.category.components");
            case MATERIALS -> Component.translatable("gui.scrapwars.category.materials");
        };
    }

    private boolean isCategorySelected(WorkbenchCategory category) {
        return selectedCategory == category;
    }

    private void setCategory(WorkbenchCategory category) {
        this.selectedCategory = category;
        this.selectedRecipeIndex = -1;
        this.scrollOffset = 0;
        rebuildRecipeList();
        this.clearWidgets();
        init();
        updateCraftButtonState();
    }

    private void rebuildRecipeList() {
        visibleRecipes.clear();

        if (minecraft == null || minecraft.level == null) {
            return;
        }

        List<ImprovisedWorkbenchRecipe> allRecipes = new ArrayList<>();

        for (ImprovisedWorkbenchRecipe recipe : minecraft.level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.IMPROVISED_WORKBENCH_RECIPE.get())) {
            allRecipes.add(recipe);
        }

        for (ImprovisedWorkbenchRecipe recipe : allRecipes) {
            if (selectedCategory == WorkbenchCategory.ALL || recipe.getCategory() == selectedCategory) {
                visibleRecipes.add(recipe);
            }
        }

        visibleRecipes.sort((a, b) -> {
            String nameA = a.getResultItem(minecraft.level.registryAccess()).getHoverName().getString();
            String nameB = b.getResultItem(minecraft.level.registryAccess()).getHoverName().getString();
            return nameA.compareToIgnoreCase(nameB);
        });
    }

    private void craftSelected() {
        if (minecraft == null || minecraft.player == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        ModMessages.sendToServer(new CraftSelectedRecipeC2SPacket(menu.getBlockPos(), recipe.getId()));
    }

    private ImprovisedWorkbenchRecipe getSelectedRecipe() {
        int absoluteIndex = scrollOffset + selectedRecipeIndex;
        if (selectedRecipeIndex < 0 || absoluteIndex < 0 || absoluteIndex >= visibleRecipes.size()) {
            return null;
        }
        return visibleRecipes.get(absoluteIndex);
    }

    private void updateCraftButtonState() {
        if (craftButton == null || minecraft == null || minecraft.player == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        craftButton.active = recipe != null && WorkbenchRecipeHelper.hasMaterials(minecraft.player, recipe);
    }

    private int countMatchingItems(WorkbenchIngredient ingredient) {
        if (minecraft == null || minecraft.player == null) {
            return 0;
        }

        int total = 0;

        for (ItemStack stack : minecraft.player.getInventory().items) {
            if (ingredient.ingredient().test(stack)) {
                total += stack.getCount();
            }
        }

        for (ItemStack stack : minecraft.player.getInventory().offhand) {
            if (ingredient.ingredient().test(stack)) {
                total += stack.getCount();
            }
        }

        return total;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        renderRecipeList(guiGraphics);
        renderSelectedRecipePanel(guiGraphics);
        renderScrollbar(guiGraphics);
    }

    private void renderRecipeList(GuiGraphics guiGraphics) {
        if (minecraft == null || minecraft.level == null) {
            return;
        }

        for (int i = 0; i < MAX_VISIBLE_RECIPES; i++) {
            int recipeIndex = scrollOffset + i;
            if (recipeIndex >= visibleRecipes.size()) {
                break;
            }

            ImprovisedWorkbenchRecipe recipe = visibleRecipes.get(recipeIndex);
            int entryX = leftPos + LIST_X;
            int entryY = topPos + LIST_Y + i * ENTRY_HEIGHT;

            boolean selected = selectedRecipeIndex == i;

            int bgColor = selected ? 0xA0D0D0D0 : 0x70303030;
            guiGraphics.fill(entryX, entryY, entryX + LIST_WIDTH, entryY + ENTRY_HEIGHT - 2, bgColor);

            if (selected) {
                guiGraphics.fill(entryX, entryY, entryX + 2, entryY + ENTRY_HEIGHT - 2, 0xFFB6E205);
            }

            ItemStack resultStack = recipe.getResultItem(minecraft.level.registryAccess());
            guiGraphics.renderItem(resultStack, entryX + 2, entryY + 2);
            guiGraphics.drawString(font, resultStack.getHoverName(), entryX + 22, entryY + 6, 0x404040, false);
        }
    }

    private void renderSelectedRecipePanel(GuiGraphics guiGraphics) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        guiGraphics.fill(leftPos + 140, topPos + 32, leftPos + 222, topPos + 108, 0x50202020);
        guiGraphics.fill(leftPos + 142, topPos + 66, leftPos + 220, topPos + 66 + 18 * 4, 0x30303030);

        ItemStack resultStack = recipe.getResultItem(minecraft.level.registryAccess());

        guiGraphics.renderItem(resultStack, leftPos + 146, topPos + 36);
        guiGraphics.drawString(font, resultStack.getHoverName(), leftPos + 166, topPos + 40, 0x404040, false);

        guiGraphics.drawString(font,
                Component.translatable("gui.scrapwars.materials"),
                leftPos + 146, topPos + 58, 0x404040, false);

        int line = 0;
        for (WorkbenchIngredient ingredient : recipe.getIngredientsList()) {
            ItemStack displayStack = ItemStack.EMPTY;
            ItemStack[] stacks = ingredient.ingredient().getItems();
            if (stacks.length > 0) {
                displayStack = stacks[0];
            }

            int y = topPos + 70 + line * 18;

            if (!displayStack.isEmpty()) {
                int available = countMatchingItems(ingredient);
                int color = available >= ingredient.count() ? 0x00AA00 : 0xAA0000;

                guiGraphics.renderItem(displayStack, leftPos + 146, y);
                guiGraphics.drawString(
                        font,
                        Component.literal(displayStack.getHoverName().getString() + " " + available + "/" + ingredient.count()),
                        leftPos + 166,
                        y + 4,
                        color,
                        false
                );
            } else {
                guiGraphics.drawString(
                        font,
                        Component.translatable("gui.scrapwars.unknown").append(" x" + ingredient.count()),
                        leftPos + 146,
                        y + 4,
                        0x404040,
                        false
                );
            }

            line++;
        }

        boolean ready = WorkbenchRecipeHelper.hasMaterials(minecraft.player, recipe);
        guiGraphics.drawString(
                font,
                Component.translatable(ready ? "gui.scrapwars.ready" : "gui.scrapwars.missing"),
                leftPos + 146,
                topPos + 70 + line * 18 + 4,
                ready ? 0x00AA00 : 0xAA0000,
                false
        );
    }

    private void renderScrollbar(GuiGraphics guiGraphics) {
        int barX = leftPos + 132;
        int barY = topPos + 36;
        int barHeight = ENTRY_HEIGHT * MAX_VISIBLE_RECIPES;

        guiGraphics.fill(barX, barY, barX + 4, barY + barHeight, 0xFF555555);

        int maxScroll = Math.max(0, visibleRecipes.size() - MAX_VISIBLE_RECIPES);
        if (maxScroll <= 0) {
            guiGraphics.fill(barX, barY, barX + 4, barY + barHeight, 0xFFAAAAAA);
            return;
        }

        int thumbHeight = Math.max(10, barHeight / Math.max(1, visibleRecipes.size()) * MAX_VISIBLE_RECIPES);
        int thumbY = barY + (barHeight - thumbHeight) * scrollOffset / maxScroll;

        guiGraphics.fill(barX, thumbY, barX + 4, thumbY + thumbHeight, 0xFFCCCCCC);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        guiGraphics.drawString(font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < MAX_VISIBLE_RECIPES; i++) {
            int entryX = leftPos + LIST_X;
            int entryY = topPos + LIST_Y + i * ENTRY_HEIGHT;
            int recipeIndex = scrollOffset + i;

            if (recipeIndex >= visibleRecipes.size()) {
                break;
            }

            if (mouseX >= entryX && mouseX <= entryX + LIST_WIDTH
                    && mouseY >= entryY && mouseY <= entryY + ENTRY_HEIGHT - 2) {
                selectedRecipeIndex = i;
                updateCraftButtonState();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maxScroll = Math.max(0, visibleRecipes.size() - MAX_VISIBLE_RECIPES);

        if (delta < 0 && scrollOffset < maxScroll) {
            scrollOffset++;
            if (selectedRecipeIndex >= 0 && scrollOffset + selectedRecipeIndex >= visibleRecipes.size()) {
                selectedRecipeIndex = -1;
            }
            updateCraftButtonState();
            return true;
        }

        if (delta > 0 && scrollOffset > 0) {
            scrollOffset--;
            updateCraftButtonState();
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        updateCraftButtonState();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        renderRecipeTooltips(guiGraphics, mouseX, mouseY);
        renderMaterialTooltips(guiGraphics, mouseX, mouseY);
    }

    private void renderRecipeTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (minecraft == null || minecraft.level == null) {
            return;
        }

        for (int i = 0; i < MAX_VISIBLE_RECIPES; i++) {
            int recipeIndex = scrollOffset + i;
            if (recipeIndex >= visibleRecipes.size()) {
                break;
            }

            int entryX = leftPos + LIST_X;
            int entryY = topPos + LIST_Y + i * ENTRY_HEIGHT;

            if (mouseX >= entryX + 2 && mouseX <= entryX + 18
                    && mouseY >= entryY + 2 && mouseY <= entryY + 18) {
                ItemStack resultStack = visibleRecipes.get(recipeIndex).getResultItem(minecraft.level.registryAccess());
                guiGraphics.renderTooltip(font, resultStack, mouseX, mouseY);
                break;
            }
        }
    }

    private void renderMaterialTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (minecraft == null || minecraft.level == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        int line = 0;
        for (WorkbenchIngredient ingredient : recipe.getIngredientsList()) {
            ItemStack[] stacks = ingredient.ingredient().getItems();
            if (stacks.length == 0) {
                line++;
                continue;
            }

            int x = leftPos + 146;
            int y = topPos + 70 + line * 18;

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                guiGraphics.renderTooltip(font, stacks[0], mouseX, mouseY);
                break;
            }

            line++;
        }
    }
}
