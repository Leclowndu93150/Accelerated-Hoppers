package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.inventory.PulseHopperMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PulseHopperScreen extends AbstractContainerScreen<PulseHopperMenu> {
    private static final ResourceLocation TEXTURE = AHMain.id("textures/gui/container/pulse_hopper.png");

    private ValueSlider slider;

    public PulseHopperScreen(PulseHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 158;
        this.inventoryLabelY = 64;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (slider != null) {
            slider.sync(menu.getPerPulse());
        }
    }

    @Override
    protected void init() {
        super.init();
        slider = addRenderableWidget(new ValueSlider(leftPos + 30, topPos + 40, 100, 1, 64, menu.getPerPulse(),
                value -> Component.translatable("gui.advanced_hoppers.per_pulse", value),
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
        graphics.blit(TEXTURE, leftPos + 136, topPos + 42, menu.isPowered() ? 176 : 192, 0, 16, 16, 256, 256);
    }
}
