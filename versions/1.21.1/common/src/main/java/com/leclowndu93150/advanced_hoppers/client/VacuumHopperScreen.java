package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.VacuumHopperMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class VacuumHopperScreen extends AbstractContainerScreen<VacuumHopperMenu> {
    private static final ResourceLocation TEXTURE = AHMain.id("textures/gui/container/vacuum_hopper.png");

    private ValueSlider slider;

    public VacuumHopperScreen(VacuumHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 158;
        this.inventoryLabelY = 64;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (slider != null) {
            slider.sync(menu.getRange());
        }
    }

    @Override
    protected void init() {
        super.init();
        slider = addRenderableWidget(new ValueSlider(leftPos + 38, topPos + 40, 100, 1, Config.vacuumHopperMaxRange, menu.getRange(),
                value -> Component.translatable("gui.advanced_hoppers.range", value),
                value -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, value)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
