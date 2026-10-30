package com.leclowndu93150.advanced_hoppers.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class TooltipUtil {
    public static void addTooltip(Consumer<Component> tooltipComponents, int ticks) {
        if (ticks <= 0) {
            tooltipComponents.accept(Component.literal("⚡ Speed: ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal("INSTANT")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
            return;
        }

        double itemsPerSecond = 20.0 / ticks;

        Component speedComponent = createSpeedComponent(itemsPerSecond, ticks);

        tooltipComponents.accept(Component.literal("⚡ Speed: ")
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
            formattedSpeed = String.format("%.0f", itemsPerSecond);
            speedColor = ChatFormatting.RED;
        } else if (itemsPerSecond >= 10) {
            formattedSpeed = String.format("%.1f", itemsPerSecond);
            speedColor = ChatFormatting.GOLD;
        } else if (itemsPerSecond >= 5) {
            formattedSpeed = String.format("%.1f", itemsPerSecond);
            speedColor = ChatFormatting.YELLOW;
        } else if (itemsPerSecond >= 1) {
            if (itemsPerSecond == Math.floor(itemsPerSecond)) {
                formattedSpeed = String.format("%.0f", itemsPerSecond);
            } else {
                formattedSpeed = String.format("%.1f", itemsPerSecond);
            }
            speedColor = ChatFormatting.GREEN;
        } else {
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
