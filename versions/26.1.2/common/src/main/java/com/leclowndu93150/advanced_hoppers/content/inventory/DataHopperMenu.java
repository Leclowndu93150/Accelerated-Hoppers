package com.leclowndu93150.advanced_hoppers.content.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class DataHopperMenu extends AbstractContainerMenu {
    protected final Container hopper;
    protected final ContainerData data;

    protected DataHopperMenu(MenuType<?> type, int id, Inventory playerInventory, Container hopper, ContainerData data, int inventoryTop) {
        super(type, id);
        this.hopper = hopper;
        this.data = data;
        checkContainerSize(hopper, 5);
        checkContainerDataCount(data, data.getCount());
        hopper.startOpen(playerInventory.player);
        for (int i = 0; i < 5; ++i) {
            this.addSlot(new Slot(hopper, i, 44 + i * 18, 20));
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, inventoryTop + 1 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, inventoryTop + 59));
        }
        this.addDataSlots(data);
    }

    public int getData(int index) {
        return data.get(index);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 5) {
                if (!this.moveItemStackTo(stack, 5, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 5, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.hopper.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.hopper.stopOpen(player);
    }
}
