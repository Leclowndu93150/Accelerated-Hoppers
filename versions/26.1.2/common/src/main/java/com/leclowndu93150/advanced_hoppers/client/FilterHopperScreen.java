package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.filter.ItemFilter;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilterHopperContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FilterHopperScreen extends AbstractContainerScreen<FilterHopperContainer> {
    private static final Identifier HOPPER_GUI_TEXTURE = AHMain.id("textures/gui/container/filtered_hopper.png");

    public FilterHopperScreen(FilterHopperContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 176, 133);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new IconToggleButton(leftPos + 107, topPos + 19, HOPPER_GUI_TEXTURE,
                () -> menu.isModeEnabled(ItemFilter.BLACKLIST) ? 192 : 176, 18, () -> false,
                () -> Component.translatable(menu.isModeEnabled(ItemFilter.BLACKLIST) ? "gui.advanced_hoppers.blacklist" : "gui.advanced_hoppers.whitelist"),
                () -> click(ItemFilter.BLACKLIST)));
        addRenderableWidget(new IconToggleButton(leftPos + 127, topPos + 19, HOPPER_GUI_TEXTURE, () -> 208, 18,
                () -> menu.isModeEnabled(ItemFilter.TAGS), () -> Component.translatable("gui.advanced_hoppers.match_tags"),
                () -> click(ItemFilter.TAGS)));
        addRenderableWidget(new IconToggleButton(leftPos + 147, topPos + 19, HOPPER_GUI_TEXTURE, () -> 224, 18,
                () -> menu.isModeEnabled(ItemFilter.EXACT), () -> Component.translatable("gui.advanced_hoppers.match_components"),
                () -> click(ItemFilter.EXACT)));
    }

    private void click(int id) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, HOPPER_GUI_TEXTURE, leftPos, topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
