package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.inventory.LinkedHopperMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LinkedHopperScreen extends AbstractContainerScreen<LinkedHopperMenu> {
    private static final ResourceLocation TEXTURE = AHMain.id("textures/gui/container/linked_hopper.png");
    private static final int TEXT = 0xFF404040;
    private static final int HINT = 0xFF6E6E6E;

    public LinkedHopperScreen(LinkedHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 158;
        this.inventoryLabelY = 64;
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
        boolean linked = menu.getTarget() != null || menu.getSource() != null;
        graphics.blit(TEXTURE, leftPos + 26, topPos + 42, linked ? 176 : 192, 0, 16, 16, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        BlockPos target = menu.getTarget();
        BlockPos source = menu.getSource();
        if (target != null && source != null) {
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.to", target.getX(), target.getY(), target.getZ()), 48, 42, TEXT, false);
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.from", source.getX(), source.getY(), source.getZ()), 48, 52, TEXT, false);
        } else if (target != null) {
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.sending_to"), 48, 42, TEXT, false);
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.coordinates", target.getX(), target.getY(), target.getZ()), 48, 52, HINT, false);
        } else if (source != null) {
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.receiving_from"), 48, 42, TEXT, false);
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.coordinates", source.getX(), source.getY(), source.getZ()), 48, 52, HINT, false);
        } else {
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.not_linked"), 48, 42, TEXT, false);
            graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.use_linker"), 48, 52, HINT, false);
        }
    }
}
