package com.leclowndu93150.advanced_hoppers.fabric;

import com.leclowndu93150.advanced_hoppers.client.FilterHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.FilteredFlopperScreen;
import com.leclowndu93150.advanced_hoppers.client.LinkedHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.PulseHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.VacuumHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.VoidHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.XpHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.OtherHopperScreen;
import com.leclowndu93150.advanced_hoppers.client.WoodenHopperScreen;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.utils.TooltipUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.Items;

public class AHFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(AHRegistry.WOODEN_HOPPER_CONTAINER, WoodenHopperScreen::new);
        MenuScreens.register(AHRegistry.OTHER_HOPPER_CONTAINER, OtherHopperScreen::new);
        MenuScreens.register(AHRegistry.FILTER_HOPPER_CONTAINER, FilterHopperScreen::new);
        MenuScreens.register(AHRegistry.VOID_HOPPER_MENU, VoidHopperScreen::new);
        MenuScreens.register(AHRegistry.VACUUM_HOPPER_MENU, VacuumHopperScreen::new);
        MenuScreens.register(AHRegistry.PULSE_HOPPER_MENU, PulseHopperScreen::new);
        MenuScreens.register(AHRegistry.LINKED_HOPPER_MENU, LinkedHopperScreen::new);
        MenuScreens.register(AHRegistry.XP_HOPPER_MENU, XpHopperScreen::new);
        MenuScreens.register(AHRegistry.FILTERED_FLOPPER_MENU, FilteredFlopperScreen::new);

        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            if (stack.is(Items.HOPPER)) {
                TooltipUtil.addIndexTooltip(1, lines, 8);
            }
        });
    }
}
