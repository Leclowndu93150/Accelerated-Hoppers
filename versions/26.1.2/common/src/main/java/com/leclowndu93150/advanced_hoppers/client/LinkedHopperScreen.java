package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.inventory.LinkedHopperMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class LinkedHopperScreen extends AbstractContainerScreen<LinkedHopperMenu> {
    private static final Identifier TEXTURE = AHMain.id("textures/gui/container/linked_hopper.png");
    private static final int TEXT = 0xFF404040;
    private static final int HINT = 0xFF6E6E6E;

    public LinkedHopperScreen(LinkedHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 158);
        this.inventoryLabelY = 64;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        boolean linked = menu.getTarget() != null || menu.getSource() != null;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 26, topPos + 42, linked ? 176 : 192, 0, 16, 16, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        BlockPos target = menu.getTarget();
        BlockPos source = menu.getSource();
        if (target != null && source != null) {
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.to", target.getX(), target.getY(), target.getZ()), 48, 42, TEXT, false);
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.from", source.getX(), source.getY(), source.getZ()), 48, 52, TEXT, false);
        } else if (target != null) {
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.sending_to"), 48, 42, TEXT, false);
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.coordinates", target.getX(), target.getY(), target.getZ()), 48, 52, HINT, false);
        } else if (source != null) {
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.receiving_from"), 48, 42, TEXT, false);
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.coordinates", source.getX(), source.getY(), source.getZ()), 48, 52, HINT, false);
        } else {
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.not_linked"), 48, 42, TEXT, false);
            graphics.text(this.font, Component.translatable("gui.advanced_hoppers.use_linker"), 48, 52, HINT, false);
        }
    }
}
