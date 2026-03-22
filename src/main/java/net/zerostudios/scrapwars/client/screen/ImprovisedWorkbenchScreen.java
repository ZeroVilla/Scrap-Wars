package net.zerostudios.scrapwars.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zerostudios.scrapwars.common.menu.ImprovisedWorkbenchMenu;
import net.zerostudios.scrapwars.common.util.ModLoc;

public class ImprovisedWorkbenchScreen extends AbstractContainerScreen<ImprovisedWorkbenchMenu> {
    private static final ResourceLocation TEXTURE = ModLoc.of("textures/gui/improvised_workbench.png");

    public ImprovisedWorkbenchScreen(ImprovisedWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
