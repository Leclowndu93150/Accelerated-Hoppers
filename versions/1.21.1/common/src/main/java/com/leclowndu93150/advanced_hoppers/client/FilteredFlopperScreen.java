package com.leclowndu93150.advanced_hoppers.client;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilteredFlopperMenu;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FilteredFlopperScreen extends AbstractContainerScreen<FilteredFlopperMenu> {
    private static final ResourceLocation TEXTURE = AHMain.id("textures/gui/container/filtered_flopper.png");
    private static final int GAUGE_X = 104;
    private static final int GAUGE_Y = 18;
    private static final int GAUGE_HEIGHT = 52;

    public FilteredFlopperScreen(FilteredFlopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
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
        Fluid fluid = menu.getFluid();
        int filled = Math.min(GAUGE_HEIGHT, (int) Math.ceil((double) GAUGE_HEIGHT * menu.getAmount() / menu.getCapacity()));
        if (fluid != Fluids.EMPTY && filled > 0) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper()
                    .getParticleIcon(fluid.defaultFluidState().createLegacyBlock());
            boolean water = fluid.is(FluidTags.WATER);
            float red = water ? 63 / 255.0F : 1.0F;
            float green = water ? 118 / 255.0F : 1.0F;
            float blue = water ? 228 / 255.0F : 1.0F;
            int bottom = topPos + GAUGE_Y + GAUGE_HEIGHT;
            graphics.enableScissor(leftPos + GAUGE_X, bottom - filled, leftPos + GAUGE_X + 16, bottom);
            for (int drawn = 0; drawn < filled; drawn += 16) {
                graphics.blit(leftPos + GAUGE_X, bottom - drawn - 16, 0, 16, 16, sprite, red, green, blue, 1.0F);
            }
            graphics.disableScissor();
        }
        graphics.blit(TEXTURE, leftPos + GAUGE_X, topPos + GAUGE_Y, 176, 0, 4, GAUGE_HEIGHT, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        Fluid fluid = menu.getFluid();
        Component name = fluid == Fluids.EMPTY ? Component.translatable("gui.advanced_hoppers.empty")
                : Services.PLATFORM.getFluidName(new FluidVolume(fluid, Math.max(1, menu.getAmount())));
        graphics.drawString(this.font, name, 126, 30, 0x404040, false);
        graphics.drawString(this.font, Component.translatable("gui.advanced_hoppers.millibuckets", menu.getAmount()), 126, 42, 0x404040, false);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        int x = mouseX - leftPos;
        int y = mouseY - topPos;
        if (x >= GAUGE_X && x < GAUGE_X + 16 && y >= GAUGE_Y && y < GAUGE_Y + GAUGE_HEIGHT) {
            graphics.renderTooltip(this.font, Component.translatable("gui.advanced_hoppers.tank", menu.getAmount(), menu.getCapacity()), mouseX, mouseY);
        }
    }
}
