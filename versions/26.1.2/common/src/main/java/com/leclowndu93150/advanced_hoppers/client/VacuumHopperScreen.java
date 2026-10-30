package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.VacuumHopperMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class VacuumHopperScreen extends AbstractContainerScreen<VacuumHopperMenu> {
    private static final Identifier TEXTURE = AHMain.id("textures/gui/container/vacuum_hopper.png");

    private ValueSlider slider;

    public VacuumHopperScreen(VacuumHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 158);
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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
