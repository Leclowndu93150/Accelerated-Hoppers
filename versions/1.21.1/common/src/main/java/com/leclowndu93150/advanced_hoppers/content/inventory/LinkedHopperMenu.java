package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.Nullable;

public class LinkedHopperMenu extends DataHopperMenu {
    public LinkedHopperMenu(int id, Inventory playerInventory, Container hopper, ContainerData data) {
        super(AHRegistry.LINKED_HOPPER_MENU, id, playerInventory, hopper, data, 75);
    }

    public LinkedHopperMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(5), new SimpleContainerData(13));
    }

    private int coordinate(int index) {
        return (data.get(1 + index * 2) & 0xFFFF) | ((data.get(2 + index * 2) & 0xFFFF) << 16);
    }

    private @Nullable BlockPos position(int flag, int first) {
        return (data.get(0) & flag) == 0 ? null : new BlockPos(coordinate(first), coordinate(first + 1), coordinate(first + 2));
    }

    public @Nullable BlockPos getTarget() {
        return position(1, 0);
    }

    public @Nullable BlockPos getSource() {
        return position(2, 3);
    }
}
