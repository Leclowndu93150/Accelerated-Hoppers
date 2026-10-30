package com.leclowndu93150.advanced_hoppers.neoforge;

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
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class AHNeoForgeClient {
    public static void init(IEventBus modBus) {
        modBus.addListener(RegisterMenuScreensEvent.class, event -> {
            event.register(AHRegistry.WOODEN_HOPPER_CONTAINER, WoodenHopperScreen::new);
            event.register(AHRegistry.OTHER_HOPPER_CONTAINER, OtherHopperScreen::new);
            event.register(AHRegistry.FILTER_HOPPER_CONTAINER, FilterHopperScreen::new);
            event.register(AHRegistry.VOID_HOPPER_MENU, VoidHopperScreen::new);
            event.register(AHRegistry.VACUUM_HOPPER_MENU, VacuumHopperScreen::new);
            event.register(AHRegistry.PULSE_HOPPER_MENU, PulseHopperScreen::new);
            event.register(AHRegistry.LINKED_HOPPER_MENU, LinkedHopperScreen::new);
            event.register(AHRegistry.XP_HOPPER_MENU, XpHopperScreen::new);
            event.register(AHRegistry.FILTERED_FLOPPER_MENU, FilteredFlopperScreen::new);
        });

        NeoForge.EVENT_BUS.addListener(ItemTooltipEvent.class, event -> {
            if (event.getItemStack().is(Items.HOPPER)) {
                TooltipUtil.addIndexTooltip(1, event.getToolTip(), 8);
            }
        });
    }
}
