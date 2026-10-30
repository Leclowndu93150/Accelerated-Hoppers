package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.XpHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public class XpHopperMenu extends AbstractContainerMenu {
    private final @Nullable XpHopperBlockEntity hopper;
    private final ContainerData data;

    public XpHopperMenu(int id, Inventory playerInventory, @Nullable XpHopperBlockEntity hopper, ContainerData data) {
        super(AHRegistry.XP_HOPPER_MENU, id);
        this.hopper = hopper;
        this.data = data;
        this.addDataSlots(data);
    }

    public XpHopperMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, null, new SimpleContainerData(2));
    }

    public int getStoredXp() {
        return (data.get(0) & 0xFFFF) | ((data.get(1) & 0xFFFF) << 16);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < XpHopperBlockEntity.TAKE_ONE || id > XpHopperBlockEntity.TAKE_ALL) {
            return false;
        }
        if (hopper != null) {
            hopper.take(player, id);
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return hopper == null || isValid(hopper, player);
    }

    private static boolean isValid(BlockEntity blockEntity, Player player) {
        return !blockEntity.isRemoved() && Container.stillValidBlockEntity(blockEntity, player);
    }
}
