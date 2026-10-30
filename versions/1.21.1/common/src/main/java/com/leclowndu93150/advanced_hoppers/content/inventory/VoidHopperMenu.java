package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.VoidHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VoidHopperMenu extends AbstractContainerMenu {
    private final Container hopper;
    private final ContainerData data;

    public VoidHopperMenu(int id, Inventory playerInventory, Container hopper, ContainerData data) {
        super(AHRegistry.VOID_HOPPER_MENU, id);
        this.hopper = hopper;
        this.data = data;
        checkContainerSize(hopper, 5);
        hopper.startOpen(playerInventory.player);
        for (int i = 0; i < 5; ++i) {
            this.addSlot(new SingleItemSlot(hopper, i, 32 + i * 18, 20));
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, row * 18 + 51));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 109));
        }
        this.addDataSlots(data);
    }

    public VoidHopperMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(5), new SimpleContainerData(1));
    }

    public boolean isBlacklist() {
        return data.get(0) != 0;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != 0) {
            return false;
        }
        if (hopper instanceof VoidHopperBlockEntity voidHopper) {
            voidHopper.toggleBlacklist();
        }
        return true;
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
