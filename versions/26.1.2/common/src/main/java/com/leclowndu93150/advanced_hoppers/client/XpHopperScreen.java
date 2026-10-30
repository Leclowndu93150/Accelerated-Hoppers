package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.blockentities.XpHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.inventory.XpHopperMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class XpHopperScreen extends AbstractContainerScreen<XpHopperMenu> {
    private static final Identifier TEXTURE = AHMain.id("textures/gui/container/xp_hopper.png");
    private static final int XP_GREEN = 0xFF80FF20;
    private static final int OUTLINE = 0xFF000000;

    public XpHopperScreen(XpHopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 80);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.translatable("gui.advanced_hoppers.take_one"), button -> take(XpHopperBlockEntity.TAKE_ONE))
                .bounds(leftPos + 8, topPos + 50, 50, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.advanced_hoppers.take_ten"), button -> take(XpHopperBlockEntity.TAKE_TEN))
                .bounds(leftPos + 63, topPos + 50, 50, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.advanced_hoppers.take_all"), button -> take(XpHopperBlockEntity.TAKE_ALL))
                .bounds(leftPos + 118, topPos + 50, 50, 20).build());
    }

    private void take(int mode) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, mode);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        int stored = menu.getStoredXp();
        int level = XpHopperBlockEntity.levelsFor(stored);
        int remainder = stored;
        for (int i = 0; i < level; i++) {
            remainder -= XpHopperBlockEntity.xpForNextLevel(i);
        }
        int width = (int) (160.0F * remainder / XpHopperBlockEntity.xpForNextLevel(level));
        if (width > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 8, topPos + 31, 0, 100, width, 7, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);
        String level = String.valueOf(XpHopperBlockEntity.levelsFor(menu.getStoredXp()));
        int x = (this.imageWidth - this.font.width(level)) / 2;
        graphics.text(this.font, level, x + 1, 19, OUTLINE, false);
        graphics.text(this.font, level, x - 1, 19, OUTLINE, false);
        graphics.text(this.font, level, x, 20, OUTLINE, false);
        graphics.text(this.font, level, x, 18, OUTLINE, false);
        graphics.text(this.font, level, x, 19, XP_GREEN, false);
    }
}
