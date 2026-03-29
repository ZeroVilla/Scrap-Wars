package net.zerostudios.scrapwars.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
    private static final ResourceLocation TEXTURE = ModLoc.of("textures/gui/improvised_workbench.png");

    private static final int GUI_U = 0;
    private static final int GUI_V = 0;
    private static final int GUI_W = 256;
    private static final int GUI_H = 222;

    private static final int TAB_U = 0;
    private static final int TAB_V = 223;
    private static final int TAB_W = 28;
    private static final int TAB_H = 32;

    private static final int TAB_SELECTED_U = 29;
    private static final int TAB_SELECTED_V = 223;

    private static final int MATERIAL_OK_U = 58;
    private static final int MATERIAL_OK_V = 223;
    private static final int MATERIAL_ROW_W = 75;
    private static final int MATERIAL_ROW_H = 24;

    private static final int MATERIAL_MISSING_U = 133;
    private static final int MATERIAL_MISSING_V = 223;
    
    private static final int HEADER_TITLE_X = 8;
    private static final int HEADER_TITLE_Y = 8;

    private static final int RECIPE_NAME_X = 82;
    private static final int RECIPE_NAME_Y = 22;
    private static final int RECIPE_NAME_MAX_W = 120;

    private static final int PREV_BUTTON_X = 8;
    private static final int PREV_BUTTON_Y = 18;

    private static final int NEXT_BUTTON_X = 140;
    private static final int NEXT_BUTTON_Y = 18;

    private static final int CRAFT_BUTTON_X = 172;
    private static final int CRAFT_BUTTON_Y = 18;
    private static final int CRAFT_BUTTON_W = 76;
    private static final int CRAFT_BUTTON_H = 20;

    private static final int PREVIEW_CENTER_X = 82;
    private static final int PREVIEW_CENTER_Y = 74;
    private static final float PREVIEW_SCALE = 3.0F;

    private static final int MATERIALS_TITLE_X = 173;
    private static final int MATERIALS_TITLE_Y = 40;

    private static final int MATERIAL_ROW_X = 172;
    private static final int MATERIALS_START_Y = 52;
    private static final int MATERIAL_ROW_SPACING = 24;

    private static final int STATUS_X = 172;
    private static final int STATUS_Y = 116;

    private static final int INVENTORY_LABEL_X = 8;
    private static final int INVENTORY_LABEL_Y = 112;

    private WorkbenchCategory selectedCategory = WorkbenchCategory.ALL;
    private final List<ImprovisedWorkbenchRecipe> visibleRecipes = new ArrayList<>();
    private int selectedRecipeIndex = 0;

    private Button craftButton;
    private Button prevButton;
    private Button nextButton;

    public ImprovisedWorkbenchScreen(ImprovisedWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 222;
        this.titleLabelX = HEADER_TITLE_X;
        this.titleLabelY = HEADER_TITLE_Y;
        this.inventoryLabelX = INVENTORY_LABEL_X;
        this.inventoryLabelY = INVENTORY_LABEL_Y;
    }

    @Override
    protected void init() {
        super.init();

        rebuildRecipeList();
        clampSelectedRecipeIndex();

        prevButton = Button.builder(Component.literal("<"), button -> prevRecipe())
                .bounds(leftPos + PREV_BUTTON_X, topPos + PREV_BUTTON_Y, 20, 20)
                .build();

        nextButton = Button.builder(Component.literal(">"), button -> nextRecipe())
                .bounds(leftPos + NEXT_BUTTON_X, topPos + NEXT_BUTTON_Y, 20, 20)
                .build();

        craftButton = Button.builder(Component.translatable("gui.scrapwars.assemble"), button -> craftSelected())
                .bounds(leftPos + CRAFT_BUTTON_X, topPos + CRAFT_BUTTON_Y, CRAFT_BUTTON_W, CRAFT_BUTTON_H)
                .build();

        addRenderableWidget(prevButton);
        addRenderableWidget(nextButton);
        addRenderableWidget(craftButton);

        updateCraftButtonState();
    }

    private void rebuildRecipeList() {
        visibleRecipes.clear();

        if (minecraft == null || minecraft.level == null) {
            return;
        }

        for (ImprovisedWorkbenchRecipe recipe : minecraft.level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.IMPROVISED_WORKBENCH_RECIPE.get())) {
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

    private void clampSelectedRecipeIndex() {
        if (visibleRecipes.isEmpty()) {
            selectedRecipeIndex = -1;
        } else {
            selectedRecipeIndex = Mth.clamp(selectedRecipeIndex, 0, visibleRecipes.size() - 1);
        }
    }

    private void setCategory(WorkbenchCategory category) {
        this.selectedCategory = category;
        this.selectedRecipeIndex = 0;
        rebuildRecipeList();
        clampSelectedRecipeIndex();
        updateCraftButtonState();
    }

    private void prevRecipe() {
        if (visibleRecipes.isEmpty()) {
            return;
        }

        selectedRecipeIndex--;
        if (selectedRecipeIndex < 0) {
            selectedRecipeIndex = visibleRecipes.size() - 1;
        }
        updateCraftButtonState();
    }

    private void nextRecipe() {
        if (visibleRecipes.isEmpty()) {
            return;
        }

        selectedRecipeIndex++;
        if (selectedRecipeIndex >= visibleRecipes.size()) {
            selectedRecipeIndex = 0;
        }
        updateCraftButtonState();
    }

    private ImprovisedWorkbenchRecipe getSelectedRecipe() {
        if (visibleRecipes.isEmpty() || selectedRecipeIndex < 0 || selectedRecipeIndex >= visibleRecipes.size()) {
            return null;
        }
        return visibleRecipes.get(selectedRecipeIndex);
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

    private void updateCraftButtonState() {
        if (craftButton == null || minecraft == null || minecraft.player == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        craftButton.active = recipe != null && WorkbenchRecipeHelper.hasMaterials(minecraft.player, recipe);

        boolean hasMultiple = visibleRecipes.size() > 1;
        if (prevButton != null) prevButton.active = hasMultiple;
        if (nextButton != null) nextButton.active = hasMultiple;
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
        guiGraphics.blit(TEXTURE, leftPos, topPos, GUI_U, GUI_V, GUI_W, GUI_H);

        renderTabs(guiGraphics, mouseX, mouseY);
        renderPreviewPanel(guiGraphics);
        renderMaterialsPanel(guiGraphics);
    }

    private void renderTabs(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        WorkbenchCategory[] categories = {
                WorkbenchCategory.WEAPONS,
                WorkbenchCategory.TOOLS,
                WorkbenchCategory.ELECTRONICS,
                WorkbenchCategory.COMPONENTS,
                WorkbenchCategory.MATERIALS,
                WorkbenchCategory.ALL
        };

        for (int i = 0; i < categories.length; i++) {
            WorkbenchCategory category = categories[i];
            int x = leftPos + 4 + (TAB_W * i);
            int y = topPos - 32;

            boolean selected = category == selectedCategory;
            int u = selected ? TAB_SELECTED_U : TAB_U;
            int v = selected ? TAB_SELECTED_V : TAB_V;

            guiGraphics.blit(TEXTURE, x, y, u, v, TAB_W, TAB_H);

            ItemStack icon = getCategoryIcon(category);
            if (!icon.isEmpty()) {
                guiGraphics.renderItem(icon, x + 6, y + 8);
            }

            if (mouseX >= x && mouseX <= x + TAB_W && mouseY >= y && mouseY <= y + TAB_H) {
                guiGraphics.renderTooltip(font, category.getTranslatedName(), mouseX, mouseY);
            }
        }
    }

    private ItemStack getCategoryIcon(WorkbenchCategory category) {
        if (minecraft == null || minecraft.level == null) {
            return ItemStack.EMPTY;
        }

        for (ImprovisedWorkbenchRecipe recipe : minecraft.level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.IMPROVISED_WORKBENCH_RECIPE.get())) {
            if (category == WorkbenchCategory.ALL || recipe.getCategory() == category) {
                return recipe.getResultItem(minecraft.level.registryAccess());
            }
        }

        return ItemStack.EMPTY;
    }

    private void renderPreviewPanel(GuiGraphics guiGraphics) {
        if (minecraft == null || minecraft.level == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        ItemStack resultStack = recipe.getResultItem(minecraft.level.registryAccess());

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftPos + PREVIEW_CENTER_X, topPos + PREVIEW_CENTER_Y, 0);
        guiGraphics.pose().scale(PREVIEW_SCALE, PREVIEW_SCALE, 1.0F);
        guiGraphics.renderItem(resultStack, -8, -8);
        guiGraphics.pose().popPose();
    }

    private void renderMaterialsPanel(GuiGraphics guiGraphics) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        int line = 0;
        for (WorkbenchIngredient ingredient : recipe.getIngredientsList()) {
            if (line >= 5) {
                break;
            }

            ItemStack displayStack = ItemStack.EMPTY;
            ItemStack[] stacks = ingredient.ingredient().getItems();
            if (stacks.length > 0) {
                displayStack = stacks[0];
            }

            int x = leftPos + MATERIAL_ROW_X;
            int y = topPos + MATERIALS_START_Y + line * MATERIAL_ROW_SPACING;

            int available = countMatchingItems(ingredient);
            boolean hasEnough = available >= ingredient.count();

            int u = hasEnough ? MATERIAL_OK_U : MATERIAL_MISSING_U;
            int v = hasEnough ? MATERIAL_OK_V : MATERIAL_MISSING_V;

            guiGraphics.blit(TEXTURE, x, y, u, v, MATERIAL_ROW_W, MATERIAL_ROW_H);

            if (!displayStack.isEmpty()) {
                guiGraphics.renderItem(displayStack, x + 2, y + 4);
                guiGraphics.drawString(
                        font,
                        available + "/" + ingredient.count(),
                        x + 22,
                        y + 8,
                        hasEnough ? 0xE0E0E0 : 0xFFD0D0,
                        false
                );
            } else {
                guiGraphics.drawString(
                        font,
                        Component.translatable("gui.scrapwars.unknown"),
                        x + 4,
                        y + 8,
                        0xE0E0E0,
                        false
                );
            }

            line++;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();

        guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);

        if (recipe != null && minecraft != null && minecraft.level != null) {
            ItemStack resultStack = recipe.getResultItem(minecraft.level.registryAccess());
            Component recipeName = resultStack.getHoverName();
            String recipeNameText = font.substrByWidth(recipeName, RECIPE_NAME_MAX_W).getString();

            guiGraphics.drawCenteredString(
                    font,
                    recipeNameText,
                    RECIPE_NAME_X,
                    RECIPE_NAME_Y,
                    0x404040
            );
        }

        guiGraphics.drawString(
                font,
                this.playerInventoryTitle,
                this.inventoryLabelX,
                this.inventoryLabelY,
                0x404040,
                false
        );

        guiGraphics.drawString(
                font,
                Component.translatable("gui.scrapwars.materials"),
                MATERIALS_TITLE_X,
                MATERIALS_TITLE_Y,
                0xE0E0E0,
                false
        );

        if (recipe != null && minecraft != null && minecraft.player != null) {
            boolean ready = WorkbenchRecipeHelper.hasMaterials(minecraft.player, recipe);

            guiGraphics.drawString(
                    font,
                    Component.translatable(ready ? "gui.scrapwars.ready" : "gui.scrapwars.missing"),
                    STATUS_X,
                    STATUS_Y,
                    ready ? 0x55FF55 : 0xFF5555,
                    false
            );
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        WorkbenchCategory[] categories = {
                WorkbenchCategory.WEAPONS,
                WorkbenchCategory.TOOLS,
                WorkbenchCategory.ELECTRONICS,
                WorkbenchCategory.COMPONENTS,
                WorkbenchCategory.MATERIALS,
                WorkbenchCategory.ALL
        };

        for (int i = 0; i < categories.length; i++) {
            int x = leftPos + 4 + (TAB_W * i);
            int y = topPos - 32;

            if (mouseX >= x && mouseX <= x + TAB_W && mouseY >= y && mouseY <= y + TAB_H) {
                setCategory(categories[i]);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
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

        renderPreviewTooltip(guiGraphics, mouseX, mouseY);
        renderMaterialTooltips(guiGraphics, mouseX, mouseY);
    }

    private void renderPreviewTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (minecraft == null || minecraft.level == null) {
            return;
        }

        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        int x1 = leftPos + PREVIEW_CENTER_X - 24;
        int y1 = topPos + PREVIEW_CENTER_Y - 24;
        int x2 = leftPos + PREVIEW_CENTER_X + 24;
        int y2 = topPos + PREVIEW_CENTER_Y + 24;

        if (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2) {
            guiGraphics.renderTooltip(font, recipe.getResultItem(minecraft.level.registryAccess()), mouseX, mouseY);
        }
    }

    private void renderMaterialTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ImprovisedWorkbenchRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        int line = 0;
        for (WorkbenchIngredient ingredient : recipe.getIngredientsList()) {
            if (line >= 5) {
                break;
            }

            ItemStack[] stacks = ingredient.ingredient().getItems();
            if (stacks.length == 0) {
                line++;
                continue;
            }

            int x = leftPos + MATERIAL_ROW_X + 2;
            int y = topPos + MATERIALS_START_Y + line * MATERIAL_ROW_SPACING + 4;

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                guiGraphics.renderTooltip(font, stacks[0], mouseX, mouseY);
                break;
            }

            line++;
        }
    }
}