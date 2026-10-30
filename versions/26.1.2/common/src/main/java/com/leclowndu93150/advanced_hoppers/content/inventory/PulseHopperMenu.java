package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.PulseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class PulseHopperMenu extends DataHopperMenu {
    public PulseHopperMenu(int id, Inventory playerInventory, Container hopper, ContainerData data) {
        super(AHRegistry.PULSE_HOPPER_MENU, id, playerInventory, hopper, data, 75);
    }

    public PulseHopperMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(5), new SimpleContainerData(2));
    }

    public int getPerPulse() {
        return Math.max(1, data.get(0));
    }

    public boolean isPowered() {
        return data.get(1) != 0;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < 1 || id > 64) {
            return false;
        }
        if (hopper instanceof PulseHopperBlockEntity pulse) {
            pulse.setPerPulse(id);
        }
        return true;
    }
}
