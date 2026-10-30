package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.VacuumHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class VacuumHopperMenu extends DataHopperMenu {
    public VacuumHopperMenu(int id, Inventory playerInventory, Container hopper, ContainerData data) {
        super(AHRegistry.VACUUM_HOPPER_MENU, id, playerInventory, hopper, data, 75);
    }

    public VacuumHopperMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(5), new SimpleContainerData(1));
    }

    public int getRange() {
        return Math.max(1, data.get(0));
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < 1 || id > Config.vacuumHopperMaxRange) {
            return false;
        }
        if (hopper instanceof VacuumHopperBlockEntity vacuum) {
            vacuum.setRange(id);
        }
        return true;
    }
}
