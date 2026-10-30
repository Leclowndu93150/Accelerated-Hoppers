package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilteredFlopperMenu;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FilteredFlopperScreen extends AbstractContainerScreen<FilteredFlopperMenu> {
    private static final Identifier TEXTURE = AHMain.id("textures/gui/container/filtered_flopper.png");
    private static final int GAUGE_X = 104;
    private static final int GAUGE_Y = 18;
    private static final int GAUGE_HEIGHT = 52;
    private static final int WATER_TINT = 0xFF3F76E4;

    public FilteredFlopperScreen(FilteredFlopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        Fluid fluid = menu.getFluid();
        int filled = Math.min(GAUGE_HEIGHT, (int) Math.ceil((double) GAUGE_HEIGHT * menu.getAmount() / menu.getCapacity()));
        if (fluid != Fluids.EMPTY && filled > 0) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getBlockStateModelSet()
                    .getParticleMaterial(fluid.defaultFluidState().createLegacyBlock()).sprite();
            int tint = fluid.is(FluidTags.WATER) ? WATER_TINT : 0xFFFFFFFF;
            int bottom = topPos + GAUGE_Y + GAUGE_HEIGHT;
            graphics.enableScissor(leftPos + GAUGE_X, bottom - filled, leftPos + GAUGE_X + 16, bottom);
            for (int drawn = 0; drawn < filled; drawn += 16) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, leftPos + GAUGE_X, bottom - drawn - 16, 16, 16, tint);
            }
            graphics.disableScissor();
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + GAUGE_X, topPos + GAUGE_Y, 176, 0, 4, GAUGE_HEIGHT, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        Fluid fluid = menu.getFluid();
        Component name = fluid == Fluids.EMPTY ? Component.translatable("gui.advanced_hoppers.empty")
                : Services.PLATFORM.getFluidName(new FluidVolume(fluid, Math.max(1, menu.getAmount())));
        graphics.text(this.font, name, 126, 30, 0xFF404040, false);
        graphics.text(this.font, Component.translatable("gui.advanced_hoppers.millibuckets", menu.getAmount()), 126, 42, 0xFF404040, false);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        int x = mouseX - leftPos;
        int y = mouseY - topPos;
        if (x >= GAUGE_X && x < GAUGE_X + 16 && y >= GAUGE_Y && y < GAUGE_Y + GAUGE_HEIGHT) {
            graphics.setTooltipForNextFrame(Component.translatable("gui.advanced_hoppers.tank", menu.getAmount(), menu.getCapacity()), mouseX, mouseY);
        }
    }
}
