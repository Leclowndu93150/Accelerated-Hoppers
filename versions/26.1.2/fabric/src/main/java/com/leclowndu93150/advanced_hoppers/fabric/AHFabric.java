package com.leclowndu93150.advanced_hoppers.fabric;

import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.fabric.transfer.FilteredHopperItemStorage;
import com.leclowndu93150.advanced_hoppers.fabric.transfer.FlopperFluidStorage;
import com.leclowndu93150.advanced_hoppers.fabric.transfer.HopperItemStorage;
import com.leclowndu93150.advanced_hoppers.fabric.transfer.VoidItemStorage;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AHFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricConfig.load(FabricLoader.getInstance().getConfigDir());
        AHRegistry.init(FabricCreativeModeTab::builder);

        AHRegistry.registerBlocks((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
        AHRegistry.registerItems((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        AHRegistry.registerBlockEntityTypes((id, type) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type));
        AHRegistry.registerMenuTypes((id, type) -> Registry.register(BuiltInRegistries.MENU, id, type));
        AHRegistry.registerCreativeTabs((id, tab) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab));

        for (int stage = 0; stage < AHRegistry.COPPER_HOPPERS.length - 1; stage++) {
            OxidizableBlocksRegistry.registerNextStage(AHRegistry.COPPER_HOPPERS[stage], AHRegistry.COPPER_HOPPERS[stage + 1]);
        }
        for (int stage = 0; stage < AHRegistry.COPPER_HOPPERS.length; stage++) {
            OxidizableBlocksRegistry.registerWaxable(AHRegistry.COPPER_HOPPERS[stage], AHRegistry.WAXED_COPPER_HOPPERS[stage]);
        }
        for (BlockEntityType<? extends BaseHopperBlockEntity> type : AHRegistry.baseHopperTypes()) {
            registerHopper(type);
        }
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, side) -> new FilteredHopperItemStorage(blockEntity), AHRegistry.FILTERED_HOPPER_BLOCK_ENTITY_TYPE);
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, side) -> new VoidItemStorage(blockEntity), AHRegistry.VOID_HOPPER_BLOCK_ENTITY_TYPE);
        for (BlockEntityType<? extends FlopperBlockEntity> type : AHRegistry.flopperTypes()) {
            registerFlopper(type);
        }
    }

    private static <T extends BaseHopperBlockEntity> void registerHopper(BlockEntityType<T> type) {
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, side) -> new HopperItemStorage(blockEntity, blockEntity.getBaseCooldown()), type);
    }

    private static <T extends FlopperBlockEntity> void registerFlopper(BlockEntityType<T> type) {
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, side) -> new FlopperFluidStorage(blockEntity), type);
    }
}
