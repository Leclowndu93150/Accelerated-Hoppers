package com.leclowndu93150.accelerated_hoppers;

import com.leclowndu93150.accelerated_hoppers.client.FilterHopperScreen;
import com.leclowndu93150.accelerated_hoppers.client.OtherHopperScreen;
import com.leclowndu93150.accelerated_hoppers.client.WoodenHopperScreen;
import com.leclowndu93150.accelerated_hoppers.content.inventory.*;
import com.leclowndu93150.accelerated_hoppers.registries.Registry;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;


@Mod(AHMain.MODID)
@EventBusSubscriber(modid = AHMain.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AHMain {
    public static final String MODID = "accelerated_hoppers";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AHMain(IEventBus modEventBus, ModContainer modContainer) {
        Registry.BLOCKS.register(modEventBus);
        Registry.ITEMS.register(modEventBus);
        Registry.CREATIVE_MODE_TABS.register(modEventBus);
        Registry.BLOCK_ENTITY_TYPES.register(modEventBus);
        Registry.MENU_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMenuScreenRegister(RegisterMenuScreensEvent event) {
        event.register(Registry.WOODEN_HOPPER_CONTAINER.get(), WoodenHopperScreen::new);
        event.register(Registry.OTHER_HOPPER_CONTAINER.get(), OtherHopperScreen::new);
        event.register(Registry.FILTER_HOPPER_CONTAINER.get(), FilterHopperScreen::new);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registry.WOODEN_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new WoodenHopperItemHandler(blockEntity));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registry.IRON_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new IronHopperItemHandler(blockEntity));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registry.GOLDEN_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new GoldenHopperItemHandler(blockEntity));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registry.DIAMOND_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new DiamondHopperItemHandler(blockEntity));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registry.FILTERED_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new FilteredHopperItemHandler(blockEntity));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registry.NETHERITE_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new NetheriteHopperItemHandler(blockEntity));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, Registry.FLOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new FlopperFluidHandler(blockEntity));
    }
}
