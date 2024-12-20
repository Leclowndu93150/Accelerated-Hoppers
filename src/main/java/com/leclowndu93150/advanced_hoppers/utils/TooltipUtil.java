package com.leclowndu93150.advanced_hoppers.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TooltipUtil {

    public static void addTooltip(List<Component> tooltipComponents, int ticks) {
        float itemsPerSecond = 20.0f / ticks;
        String formattedSpeed;

        if (itemsPerSecond == (int) itemsPerSecond) {
            formattedSpeed = String.valueOf((int) itemsPerSecond);
        } else {
            formattedSpeed = String.format("%.1f", itemsPerSecond).replaceAll("\\.?0+$", "");
        }

        String itemOrItems = itemsPerSecond == 1 ? "item" : "items";

        tooltipComponents.add(Component.literal("Speed: ")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(formattedSpeed + " " + itemOrItems + "/second")
                        .withStyle(ChatFormatting.YELLOW)));
    }

    public static void addIndexTooltip(int index,List<Component> tooltipComponents, int ticks) {
        float itemsPerSecond = 20.0f / ticks;
        String formattedSpeed;

        if (itemsPerSecond == (int) itemsPerSecond) {
            formattedSpeed = String.valueOf((int) itemsPerSecond);
        } else {
            formattedSpeed = String.format("%.1f", itemsPerSecond).replaceAll("\\.?0+$", "");
        }

        String itemOrItems = itemsPerSecond == 1 ? "item" : "items";

        tooltipComponents.add(index,Component.literal("Speed: ")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(formattedSpeed + " " + itemOrItems + "/second")
                        .withStyle(ChatFormatting.YELLOW)));
    }

}
