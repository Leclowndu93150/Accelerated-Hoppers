package com.leclowndu93150.advanced_hoppers.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class IconToggleButton extends AbstractButton {
    private final Identifier texture;
    private final IntSupplier iconU;
    private final int iconV;
    private final BooleanSupplier selected;
    private final Supplier<Component> tooltip;
    private final Runnable action;
    private Component shownTooltip;

    public IconToggleButton(int x, int y, Identifier texture, IntSupplier iconU, int iconV, BooleanSupplier selected, Supplier<Component> tooltip, Runnable action) {
        super(x, y, 18, 18, tooltip.get());
        this.texture = texture;
        this.iconU = iconU;
        this.iconV = iconV;
        this.selected = selected;
        this.tooltip = tooltip;
        this.action = action;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        action.run();
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Component current = tooltip.get();
        if (!current.equals(shownTooltip)) {
            shownTooltip = current;
            setMessage(current);
            setTooltip(Tooltip.create(current));
        }
        int state = selected.getAsBoolean() ? 2 : isHoveredOrFocused() ? 1 : 0;
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), 176 + state * 18, 0, 18, 18, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, getX() + 1, getY() + 1, iconU.getAsInt(), iconV, 16, 16, 256, 256);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
