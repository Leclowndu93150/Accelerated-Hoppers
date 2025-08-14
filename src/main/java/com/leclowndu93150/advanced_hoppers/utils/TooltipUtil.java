package com.leclowndu93150.advanced_hoppers.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TooltipUtil {

    public static void addTooltip(List<Component> tooltipComponents, int ticks) {
        // Prevent division by zero and negative values
        if (ticks <= 0) {
            tooltipComponents.add(Component.literal("⚡ Speed: ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal("INSTANT")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
            return;
        }

        // Calculate items per second (20 ticks = 1 second)
        double itemsPerSecond = 20.0 / ticks;
        
        // Create speed display with better formatting
        Component speedComponent = createSpeedComponent(itemsPerSecond, ticks);
        
        tooltipComponents.add(Component.literal("⚡ Speed: ")
                .withStyle(ChatFormatting.GOLD)
                .append(speedComponent));
    }

    public static void addIndexTooltip(int index, List<Component> tooltipComponents, int ticks) {
        if (ticks <= 0) {
            tooltipComponents.add(index, Component.literal("⚡ Speed: ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal("INSTANT")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
            return;
        }

        double itemsPerSecond = 20.0 / ticks;

        Component speedComponent = createSpeedComponent(itemsPerSecond, ticks);
        
        tooltipComponents.add(index, Component.literal("⚡ Speed: ")
                .withStyle(ChatFormatting.GOLD)
                .append(speedComponent));
    }

    private static Component createSpeedComponent(double itemsPerSecond, int ticks) {
        String formattedSpeed;
        ChatFormatting speedColor;
        
        if (itemsPerSecond >= 20) {
            // Ultra fast (20+ items/s) - Red for extreme speed
            formattedSpeed = String.format("%.0f", itemsPerSecond);
            speedColor = ChatFormatting.RED;
        } else if (itemsPerSecond >= 10) {
            // Very fast (10-19.9 items/s) - Gold for high speed
            formattedSpeed = String.format("%.1f", itemsPerSecond);
            speedColor = ChatFormatting.GOLD;
        } else if (itemsPerSecond >= 5) {
            // Fast (5-9.9 items/s) - Yellow for medium-high speed
            formattedSpeed = String.format("%.1f", itemsPerSecond);
            speedColor = ChatFormatting.YELLOW;
        } else if (itemsPerSecond >= 1) {
            // Normal (1-4.9 items/s) - Green for normal speed
            if (itemsPerSecond == Math.floor(itemsPerSecond)) {
                formattedSpeed = String.format("%.0f", itemsPerSecond);
            } else {
                formattedSpeed = String.format("%.1f", itemsPerSecond);
            }
            speedColor = ChatFormatting.GREEN;
        } else {
            // Slow (< 1 item/s) - Gray for slow speed
            formattedSpeed = String.format("%.2f", itemsPerSecond);
            speedColor = ChatFormatting.GRAY;
        }

        formattedSpeed = formattedSpeed.replaceAll("[.,]?0+$", "");

        String itemText = itemsPerSecond == 1.0 ? "item" : "items";

        String timeInfo = "";
        if (itemsPerSecond < 1) {
            double secondsPerItem = ticks / 20.0;
            if (secondsPerItem >= 2) {
                timeInfo = String.format(" (%.1fs/item)", secondsPerItem);
            }
        }
        
        return Component.literal(formattedSpeed + " " + itemText + "/s" + timeInfo)
                .withStyle(speedColor, ChatFormatting.BOLD);
    }
}