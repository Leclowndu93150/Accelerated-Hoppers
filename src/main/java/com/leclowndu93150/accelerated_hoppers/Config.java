package com.leclowndu93150.accelerated_hoppers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = AHMain.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue WOODEN_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for wooden hoppers.")
            .defineInRange("woodenHopperTransferCooldown", 20, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue IRON_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for iron hoppers.")
            .defineInRange("ironHopperTransferCooldown", 6, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue GOLDEN_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for golden hoppers.")
            .defineInRange("goldenHopperTransferCooldown", 4, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue DIAMOND_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for diamond hoppers.")
            .defineInRange("diamondHopperTransferCooldown", 2, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue NETHERITE_HOPPER_TRANSFER_COOLDOWN = BUILDER.comment("The number of ticks between each transfer operation for netherite hoppers.")
            .defineInRange("netheriteHopperTransferCooldown", 1, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int woodenHopperTransferCooldown;
    public static int ironHopperTransferCooldown;
    public static int goldenHopperTransferCooldown;
    public static int diamondHopperTransferCooldown;
    public static int netheriteHopperTransferCooldown;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        woodenHopperTransferCooldown = WOODEN_HOPPER_TRANSFER_COOLDOWN.get();
        ironHopperTransferCooldown = IRON_HOPPER_TRANSFER_COOLDOWN.get();
        goldenHopperTransferCooldown = GOLDEN_HOPPER_TRANSFER_COOLDOWN.get();
        diamondHopperTransferCooldown = DIAMOND_HOPPER_TRANSFER_COOLDOWN.get();
        netheriteHopperTransferCooldown = NETHERITE_HOPPER_TRANSFER_COOLDOWN.get();

    }
}
