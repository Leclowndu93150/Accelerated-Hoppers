package com.leclowndu93150.advanced_hoppers;

public class Config {
    public static final int DEFAULT_WOODEN_HOPPER_TRANSFER_COOLDOWN = 20;
    public static final int DEFAULT_IRON_HOPPER_TRANSFER_COOLDOWN = 6;
    public static final int DEFAULT_GOLDEN_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_DIAMOND_HOPPER_TRANSFER_COOLDOWN = 2;
    public static final int DEFAULT_FILTERED_HOPPER_TRANSFER_COOLDOWN = 2;
    public static final int DEFAULT_NETHERITE_HOPPER_TRANSFER_COOLDOWN = 1;
    public static final int DEFAULT_FLOPPER_CAPACITY = 8000;
    public static final int DEFAULT_FLOPPER_IO_RATE = 50;
    public static final int DEFAULT_COPPER_HOPPER_TRANSFER_COOLDOWN = 10;
    public static final int DEFAULT_EXPOSED_COPPER_HOPPER_TRANSFER_COOLDOWN = 12;
    public static final int DEFAULT_WEATHERED_COPPER_HOPPER_TRANSFER_COOLDOWN = 14;
    public static final int DEFAULT_OXIDIZED_COPPER_HOPPER_TRANSFER_COOLDOWN = 16;
    public static final int DEFAULT_STACK_HOPPER_TRANSFER_COOLDOWN = 8;
    public static final int DEFAULT_VOID_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_SPLITTER_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_VACUUM_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_VACUUM_HOPPER_MAX_RANGE = 8;
    public static final int DEFAULT_PULSE_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_UPWARD_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_LINKED_HOPPER_TRANSFER_COOLDOWN = 4;
    public static final int DEFAULT_LINKED_HOPPER_MAX_DISTANCE = 64;
    public static final int DEFAULT_XP_HOPPER_COLLECT_INTERVAL = 4;
    public static final int DEFAULT_IRON_FLOPPER_CAPACITY = 16000;
    public static final int DEFAULT_IRON_FLOPPER_IO_RATE = 100;
    public static final int DEFAULT_GOLDEN_FLOPPER_CAPACITY = 32000;
    public static final int DEFAULT_GOLDEN_FLOPPER_IO_RATE = 250;
    public static final int DEFAULT_DIAMOND_FLOPPER_CAPACITY = 64000;
    public static final int DEFAULT_DIAMOND_FLOPPER_IO_RATE = 1000;

    public static int woodenHopperTransferCooldown = DEFAULT_WOODEN_HOPPER_TRANSFER_COOLDOWN;
    public static int ironHopperTransferCooldown = DEFAULT_IRON_HOPPER_TRANSFER_COOLDOWN;
    public static int goldenHopperTransferCooldown = DEFAULT_GOLDEN_HOPPER_TRANSFER_COOLDOWN;
    public static int diamondHopperTransferCooldown = DEFAULT_DIAMOND_HOPPER_TRANSFER_COOLDOWN;
    public static int filteredHopperTransferCooldown = DEFAULT_FILTERED_HOPPER_TRANSFER_COOLDOWN;
    public static int netheriteHopperTransferCooldown = DEFAULT_NETHERITE_HOPPER_TRANSFER_COOLDOWN;
    public static int flopperCapacity = DEFAULT_FLOPPER_CAPACITY;
    public static int flopperIORate = DEFAULT_FLOPPER_IO_RATE;
    public static int copperHopperTransferCooldown = DEFAULT_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public static int exposedCopperHopperTransferCooldown = DEFAULT_EXPOSED_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public static int weatheredCopperHopperTransferCooldown = DEFAULT_WEATHERED_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public static int oxidizedCopperHopperTransferCooldown = DEFAULT_OXIDIZED_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public static int stackHopperTransferCooldown = DEFAULT_STACK_HOPPER_TRANSFER_COOLDOWN;
    public static int voidHopperTransferCooldown = DEFAULT_VOID_HOPPER_TRANSFER_COOLDOWN;
    public static int splitterHopperTransferCooldown = DEFAULT_SPLITTER_HOPPER_TRANSFER_COOLDOWN;
    public static int vacuumHopperTransferCooldown = DEFAULT_VACUUM_HOPPER_TRANSFER_COOLDOWN;
    public static int vacuumHopperMaxRange = DEFAULT_VACUUM_HOPPER_MAX_RANGE;
    public static int pulseHopperTransferCooldown = DEFAULT_PULSE_HOPPER_TRANSFER_COOLDOWN;
    public static int upwardHopperTransferCooldown = DEFAULT_UPWARD_HOPPER_TRANSFER_COOLDOWN;
    public static int linkedHopperTransferCooldown = DEFAULT_LINKED_HOPPER_TRANSFER_COOLDOWN;
    public static int linkedHopperMaxDistance = DEFAULT_LINKED_HOPPER_MAX_DISTANCE;
    public static int xpHopperCollectInterval = DEFAULT_XP_HOPPER_COLLECT_INTERVAL;
    public static int ironFlopperCapacity = DEFAULT_IRON_FLOPPER_CAPACITY;
    public static int ironFlopperIORate = DEFAULT_IRON_FLOPPER_IO_RATE;
    public static int goldenFlopperCapacity = DEFAULT_GOLDEN_FLOPPER_CAPACITY;
    public static int goldenFlopperIORate = DEFAULT_GOLDEN_FLOPPER_IO_RATE;
    public static int diamondFlopperCapacity = DEFAULT_DIAMOND_FLOPPER_CAPACITY;
    public static int diamondFlopperIORate = DEFAULT_DIAMOND_FLOPPER_IO_RATE;

    public static void clampRates() {
        flopperIORate = Math.min(flopperIORate, flopperCapacity);
        ironFlopperIORate = Math.min(ironFlopperIORate, ironFlopperCapacity);
        goldenFlopperIORate = Math.min(goldenFlopperIORate, goldenFlopperCapacity);
        diamondFlopperIORate = Math.min(diamondFlopperIORate, diamondFlopperCapacity);
    }
}
