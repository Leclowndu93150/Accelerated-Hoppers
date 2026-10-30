package com.leclowndu93150.advanced_hoppers.content.blocks;

import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public interface TooltipBlock {
    void appendTooltip(Consumer<Component> tooltip);
}
