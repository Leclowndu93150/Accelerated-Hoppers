package com.leclowndu93150.advanced_hoppers.neoforge;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.neoforge.transfer.FilteredHopperResourceHandler;
import com.leclowndu93150.advanced_hoppers.neoforge.transfer.FlopperResourceHandler;
import com.leclowndu93150.advanced_hoppers.neoforge.transfer.HopperResourceHandler;
import com.leclowndu93150.advanced_hoppers.neoforge.transfer.VoidResourceHandler;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(AHMain.MODID)
public class AHNeoForge {
    public AHNeoForge(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, NeoForgeConfig.SPEC);
        modBus.addListener(ModConfigEvent.Loading.class, event -> NeoForgeConfig.onLoad());
        modBus.addListener(ModConfigEvent.Reloading.class, event -> NeoForgeConfig.onLoad());

        modBus.addListener(RegisterEvent.class, event -> {
            if (event.getRegistryKey().equals(Registries.BLOCK)) {
                AHRegistry.init(CreativeModeTab::builder);
            }
            event.register(Registries.BLOCK, helper -> AHRegistry.registerBlocks(helper::register));
            event.register(Registries.ITEM, helper -> AHRegistry.registerItems(helper::register));
            event.register(Registries.BLOCK_ENTITY_TYPE, helper -> AHRegistry.registerBlockEntityTypes(helper::register));
            event.register(Registries.MENU, helper -> AHRegistry.registerMenuTypes(helper::register));
            event.register(Registries.CREATIVE_MODE_TAB, helper -> AHRegistry.registerCreativeTabs(helper::register));
        });

        modBus.addListener(RegisterCapabilitiesEvent.class, AHNeoForge::registerCapabilities);

        if (FMLEnvironment.getDist().isClient()) {
            AHNeoForgeClient.init(modBus);
        }
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BlockEntityType<? extends BaseHopperBlockEntity> type : AHRegistry.baseHopperTypes()) {
            registerHopper(event, type);
        }
        event.registerBlockEntity(Capabilities.Item.BLOCK, AHRegistry.FILTERED_HOPPER_BLOCK_ENTITY_TYPE, (blockEntity, side) -> new FilteredHopperResourceHandler(blockEntity));
        event.registerBlockEntity(Capabilities.Item.BLOCK, AHRegistry.VOID_HOPPER_BLOCK_ENTITY_TYPE, (blockEntity, side) -> new VoidResourceHandler(blockEntity));
        for (BlockEntityType<? extends FlopperBlockEntity> type : AHRegistry.flopperTypes()) {
            registerFlopper(event, type);
        }
    }

    private static <T extends BaseHopperBlockEntity> void registerHopper(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, type, (blockEntity, side) -> new HopperResourceHandler(blockEntity, blockEntity.getBaseCooldown()));
    }

    private static <T extends FlopperBlockEntity> void registerFlopper(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, type, (blockEntity, side) -> new FlopperResourceHandler(blockEntity));
    }
}
