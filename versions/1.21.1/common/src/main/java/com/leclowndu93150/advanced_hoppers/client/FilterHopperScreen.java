package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.filter.ItemFilter;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilterHopperContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FilterHopperScreen extends AbstractContainerScreen<FilterHopperContainer> {
    private static final ResourceLocation HOPPER_GUI_TEXTURE = AHMain.id("textures/gui/container/filtered_hopper.png");

    public FilterHopperScreen(FilterHopperContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 133;
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(HOPPER_GUI_TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
