package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.inventory.VoidHopperMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class VoidHopperScreen extends AbstractContainerScreen<VoidHopperMenu> {
    private static final ResourceLocation TEXTURE = AHMain.id("textures/gui/container/void_hopper.png");

    public VoidHopperScreen(VoidHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new IconToggleButton(leftPos + 127, topPos + 19, TEXTURE, () -> menu.isBlacklist() ? 192 : 176, 18, () -> false,
                () -> Component.translatable(menu.isBlacklist() ? "gui.advanced_hoppers.void_blacklist" : "gui.advanced_hoppers.void_whitelist"),
                () -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0)));
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
