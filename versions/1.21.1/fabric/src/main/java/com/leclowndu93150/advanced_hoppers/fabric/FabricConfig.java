package com.leclowndu93150.advanced_hoppers.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.Config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FabricConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int woodenHopperTransferCooldown = Config.DEFAULT_WOODEN_HOPPER_TRANSFER_COOLDOWN;
    public int ironHopperTransferCooldown = Config.DEFAULT_IRON_HOPPER_TRANSFER_COOLDOWN;
    public int goldenHopperTransferCooldown = Config.DEFAULT_GOLDEN_HOPPER_TRANSFER_COOLDOWN;
    public int diamondHopperTransferCooldown = Config.DEFAULT_DIAMOND_HOPPER_TRANSFER_COOLDOWN;
    public int filteredHopperTransferCooldown = Config.DEFAULT_FILTERED_HOPPER_TRANSFER_COOLDOWN;
    public int netheriteHopperTransferCooldown = Config.DEFAULT_NETHERITE_HOPPER_TRANSFER_COOLDOWN;
    public int flopperCapacity = Config.DEFAULT_FLOPPER_CAPACITY;
    public int flopperIORate = Config.DEFAULT_FLOPPER_IO_RATE;
    public int copperHopperTransferCooldown = Config.DEFAULT_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public int exposedCopperHopperTransferCooldown = Config.DEFAULT_EXPOSED_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public int weatheredCopperHopperTransferCooldown = Config.DEFAULT_WEATHERED_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public int oxidizedCopperHopperTransferCooldown = Config.DEFAULT_OXIDIZED_COPPER_HOPPER_TRANSFER_COOLDOWN;
    public int stackHopperTransferCooldown = Config.DEFAULT_STACK_HOPPER_TRANSFER_COOLDOWN;
    public int voidHopperTransferCooldown = Config.DEFAULT_VOID_HOPPER_TRANSFER_COOLDOWN;
    public int splitterHopperTransferCooldown = Config.DEFAULT_SPLITTER_HOPPER_TRANSFER_COOLDOWN;
    public int vacuumHopperTransferCooldown = Config.DEFAULT_VACUUM_HOPPER_TRANSFER_COOLDOWN;
    public int vacuumHopperMaxRange = Config.DEFAULT_VACUUM_HOPPER_MAX_RANGE;
    public int pulseHopperTransferCooldown = Config.DEFAULT_PULSE_HOPPER_TRANSFER_COOLDOWN;
    public int upwardHopperTransferCooldown = Config.DEFAULT_UPWARD_HOPPER_TRANSFER_COOLDOWN;
    public int linkedHopperTransferCooldown = Config.DEFAULT_LINKED_HOPPER_TRANSFER_COOLDOWN;
    public int linkedHopperMaxDistance = Config.DEFAULT_LINKED_HOPPER_MAX_DISTANCE;
    public int xpHopperCollectInterval = Config.DEFAULT_XP_HOPPER_COLLECT_INTERVAL;
    public int ironFlopperCapacity = Config.DEFAULT_IRON_FLOPPER_CAPACITY;
    public int ironFlopperIORate = Config.DEFAULT_IRON_FLOPPER_IO_RATE;
    public int goldenFlopperCapacity = Config.DEFAULT_GOLDEN_FLOPPER_CAPACITY;
    public int goldenFlopperIORate = Config.DEFAULT_GOLDEN_FLOPPER_IO_RATE;
    public int diamondFlopperCapacity = Config.DEFAULT_DIAMOND_FLOPPER_CAPACITY;
    public int diamondFlopperIORate = Config.DEFAULT_DIAMOND_FLOPPER_IO_RATE;

    public static void load(Path configDir) {
        Path file = configDir.resolve(AHMain.MODID + ".json");
        FabricConfig config = new FabricConfig();
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                FabricConfig loaded = GSON.fromJson(reader, FabricConfig.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (IOException | JsonParseException ignored) {
            }
        }
        try {
            Files.createDirectories(configDir);
            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException ignored) {
        }
        Config.woodenHopperTransferCooldown = Math.max(1, config.woodenHopperTransferCooldown);
        Config.ironHopperTransferCooldown = Math.max(1, config.ironHopperTransferCooldown);
        Config.goldenHopperTransferCooldown = Math.max(1, config.goldenHopperTransferCooldown);
        Config.diamondHopperTransferCooldown = Math.max(1, config.diamondHopperTransferCooldown);
        Config.filteredHopperTransferCooldown = Math.max(1, config.filteredHopperTransferCooldown);
        Config.netheriteHopperTransferCooldown = Math.max(1, config.netheriteHopperTransferCooldown);
        Config.flopperCapacity = Math.max(1, config.flopperCapacity);
        Config.flopperIORate = Math.max(1, config.flopperIORate);
        Config.copperHopperTransferCooldown = Math.max(1, config.copperHopperTransferCooldown);
        Config.exposedCopperHopperTransferCooldown = Math.max(1, config.exposedCopperHopperTransferCooldown);
        Config.weatheredCopperHopperTransferCooldown = Math.max(1, config.weatheredCopperHopperTransferCooldown);
        Config.oxidizedCopperHopperTransferCooldown = Math.max(1, config.oxidizedCopperHopperTransferCooldown);
        Config.stackHopperTransferCooldown = Math.max(1, config.stackHopperTransferCooldown);
        Config.voidHopperTransferCooldown = Math.max(1, config.voidHopperTransferCooldown);
        Config.splitterHopperTransferCooldown = Math.max(1, config.splitterHopperTransferCooldown);
        Config.vacuumHopperTransferCooldown = Math.max(1, config.vacuumHopperTransferCooldown);
        Config.vacuumHopperMaxRange = Math.max(1, config.vacuumHopperMaxRange);
        Config.pulseHopperTransferCooldown = Math.max(1, config.pulseHopperTransferCooldown);
        Config.upwardHopperTransferCooldown = Math.max(1, config.upwardHopperTransferCooldown);
        Config.linkedHopperTransferCooldown = Math.max(1, config.linkedHopperTransferCooldown);
        Config.linkedHopperMaxDistance = Math.max(1, config.linkedHopperMaxDistance);
        Config.xpHopperCollectInterval = Math.max(1, config.xpHopperCollectInterval);
        Config.ironFlopperCapacity = Math.max(1, config.ironFlopperCapacity);
        Config.ironFlopperIORate = Math.max(1, config.ironFlopperIORate);
        Config.goldenFlopperCapacity = Math.max(1, config.goldenFlopperCapacity);
        Config.goldenFlopperIORate = Math.max(1, config.goldenFlopperIORate);
        Config.diamondFlopperCapacity = Math.max(1, config.diamondFlopperCapacity);
        Config.diamondFlopperIORate = Math.max(1, config.diamondFlopperIORate);
        Config.clampRates();
    }
}
