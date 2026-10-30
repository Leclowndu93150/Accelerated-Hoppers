package com.leclowndu93150.advanced_hoppers.neoforge;

import com.leclowndu93150.advanced_hoppers.Config;
import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue WOODEN_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for wooden hoppers.")
            .defineInRange("woodenHopperTransferCooldown", Config.DEFAULT_WOODEN_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue IRON_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for iron hoppers.")
            .defineInRange("ironHopperTransferCooldown", Config.DEFAULT_IRON_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue GOLDEN_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for golden hoppers.")
            .defineInRange("goldenHopperTransferCooldown", Config.DEFAULT_GOLDEN_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue DIAMOND_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for diamond hoppers.")
            .defineInRange("diamondHopperTransferCooldown", Config.DEFAULT_DIAMOND_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue FILTERED_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for filtered hoppers.")
            .defineInRange("filteredHopperTransferCooldown", Config.DEFAULT_FILTERED_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue NETHERITE_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for netherite hoppers.")
            .defineInRange("netheriteHopperTransferCooldown", Config.DEFAULT_NETHERITE_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue FLOPPER_CAPACITY = BUILDER.comment("The fluid capacity (in mb) that can be stored in a flopper.")
            .defineInRange("flopperCapacity", Config.DEFAULT_FLOPPER_CAPACITY, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue FLOPPER_IO_RATE = BUILDER.comment("The fluid transfer rate (in mb) of a flopper. This value defaults to the flopper capacity if it exceeds it.")
            .defineInRange("flopperIORate", Config.DEFAULT_FLOPPER_IO_RATE, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue COPPER_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for copper hoppers.")
            .defineInRange("copperHopperTransferCooldown", Config.DEFAULT_COPPER_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue EXPOSED_COPPER_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for exposed copper hoppers.")
            .defineInRange("exposedCopperHopperTransferCooldown", Config.DEFAULT_EXPOSED_COPPER_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue WEATHERED_COPPER_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for weathered copper hoppers.")
            .defineInRange("weatheredCopperHopperTransferCooldown", Config.DEFAULT_WEATHERED_COPPER_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue OXIDIZED_COPPER_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for oxidized copper hoppers.")
            .defineInRange("oxidizedCopperHopperTransferCooldown", Config.DEFAULT_OXIDIZED_COPPER_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue STACK_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for stack hoppers. Each operation moves a whole stack.")
            .defineInRange("stackHopperTransferCooldown", Config.DEFAULT_STACK_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue VOID_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each void operation for void hoppers.")
            .defineInRange("voidHopperTransferCooldown", Config.DEFAULT_VOID_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue SPLITTER_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for splitter hoppers.")
            .defineInRange("splitterHopperTransferCooldown", Config.DEFAULT_SPLITTER_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue VACUUM_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for vacuum hoppers.")
            .defineInRange("vacuumHopperTransferCooldown", Config.DEFAULT_VACUUM_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue VACUUM_HOPPER_MAX_RANGE = BUILDER.comment("The largest pickup range (in blocks) a vacuum hopper can be set to.")
            .defineInRange("vacuumHopperMaxRange", Config.DEFAULT_VACUUM_HOPPER_MAX_RANGE, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PULSE_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each pull operation for pulse hoppers.")
            .defineInRange("pulseHopperTransferCooldown", Config.DEFAULT_PULSE_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue UPWARD_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for upward hoppers.")
            .defineInRange("upwardHopperTransferCooldown", Config.DEFAULT_UPWARD_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue LINKED_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for linked hoppers.")
            .defineInRange("linkedHopperTransferCooldown", Config.DEFAULT_LINKED_HOPPER_TRANSFER_COOLDOWN, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue LINKED_HOPPER_MAX_DISTANCE = BUILDER.comment("The largest distance (in blocks) between two linked hoppers.")
            .defineInRange("linkedHopperMaxDistance", Config.DEFAULT_LINKED_HOPPER_MAX_DISTANCE, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue XP_HOPPER_COLLECT_INTERVAL = BUILDER.comment("The number of ticks between each experience collection for XP hoppers.")
            .defineInRange("xpHopperCollectInterval", Config.DEFAULT_XP_HOPPER_COLLECT_INTERVAL, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue IRON_FLOPPER_CAPACITY = BUILDER.comment("The fluid capacity (in mb) of an iron flopper.")
            .defineInRange("ironFlopperCapacity", Config.DEFAULT_IRON_FLOPPER_CAPACITY, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue IRON_FLOPPER_IO_RATE = BUILDER.comment("The fluid transfer rate (in mb) of an iron flopper.")
            .defineInRange("ironFlopperIORate", Config.DEFAULT_IRON_FLOPPER_IO_RATE, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue GOLDEN_FLOPPER_CAPACITY = BUILDER.comment("The fluid capacity (in mb) of a golden flopper.")
            .defineInRange("goldenFlopperCapacity", Config.DEFAULT_GOLDEN_FLOPPER_CAPACITY, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue GOLDEN_FLOPPER_IO_RATE = BUILDER.comment("The fluid transfer rate (in mb) of a golden flopper.")
            .defineInRange("goldenFlopperIORate", Config.DEFAULT_GOLDEN_FLOPPER_IO_RATE, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue DIAMOND_FLOPPER_CAPACITY = BUILDER.comment("The fluid capacity (in mb) of a diamond flopper.")
            .defineInRange("diamondFlopperCapacity", Config.DEFAULT_DIAMOND_FLOPPER_CAPACITY, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue DIAMOND_FLOPPER_IO_RATE = BUILDER.comment("The fluid transfer rate (in mb) of a diamond flopper.")
            .defineInRange("diamondFlopperIORate", Config.DEFAULT_DIAMOND_FLOPPER_IO_RATE, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    static void onLoad() {
        Config.woodenHopperTransferCooldown = WOODEN_HOPPER_TRANSFER_COOLDOWN.get();
        Config.ironHopperTransferCooldown = IRON_HOPPER_TRANSFER_COOLDOWN.get();
        Config.goldenHopperTransferCooldown = GOLDEN_HOPPER_TRANSFER_COOLDOWN.get();
        Config.diamondHopperTransferCooldown = DIAMOND_HOPPER_TRANSFER_COOLDOWN.get();
        Config.filteredHopperTransferCooldown = FILTERED_HOPPER_TRANSFER_COOLDOWN.get();
        Config.netheriteHopperTransferCooldown = NETHERITE_HOPPER_TRANSFER_COOLDOWN.get();
        Config.flopperCapacity = FLOPPER_CAPACITY.get();
        Config.flopperIORate = FLOPPER_IO_RATE.get();
        Config.copperHopperTransferCooldown = COPPER_HOPPER_TRANSFER_COOLDOWN.get();
        Config.exposedCopperHopperTransferCooldown = EXPOSED_COPPER_HOPPER_TRANSFER_COOLDOWN.get();
        Config.weatheredCopperHopperTransferCooldown = WEATHERED_COPPER_HOPPER_TRANSFER_COOLDOWN.get();
        Config.oxidizedCopperHopperTransferCooldown = OXIDIZED_COPPER_HOPPER_TRANSFER_COOLDOWN.get();
        Config.stackHopperTransferCooldown = STACK_HOPPER_TRANSFER_COOLDOWN.get();
        Config.voidHopperTransferCooldown = VOID_HOPPER_TRANSFER_COOLDOWN.get();
        Config.splitterHopperTransferCooldown = SPLITTER_HOPPER_TRANSFER_COOLDOWN.get();
        Config.vacuumHopperTransferCooldown = VACUUM_HOPPER_TRANSFER_COOLDOWN.get();
        Config.vacuumHopperMaxRange = VACUUM_HOPPER_MAX_RANGE.get();
        Config.pulseHopperTransferCooldown = PULSE_HOPPER_TRANSFER_COOLDOWN.get();
        Config.upwardHopperTransferCooldown = UPWARD_HOPPER_TRANSFER_COOLDOWN.get();
        Config.linkedHopperTransferCooldown = LINKED_HOPPER_TRANSFER_COOLDOWN.get();
        Config.linkedHopperMaxDistance = LINKED_HOPPER_MAX_DISTANCE.get();
        Config.xpHopperCollectInterval = XP_HOPPER_COLLECT_INTERVAL.get();
        Config.ironFlopperCapacity = IRON_FLOPPER_CAPACITY.get();
        Config.ironFlopperIORate = IRON_FLOPPER_IO_RATE.get();
        Config.goldenFlopperCapacity = GOLDEN_FLOPPER_CAPACITY.get();
        Config.goldenFlopperIORate = GOLDEN_FLOPPER_IO_RATE.get();
        Config.diamondFlopperCapacity = DIAMOND_FLOPPER_CAPACITY.get();
        Config.diamondFlopperIORate = DIAMOND_FLOPPER_IO_RATE.get();
        Config.clampRates();
    }
}
