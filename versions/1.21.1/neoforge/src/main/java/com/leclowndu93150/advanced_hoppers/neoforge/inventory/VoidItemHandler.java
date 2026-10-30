package com.leclowndu93150.advanced_hoppers.neoforge.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.VoidHopperBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class VoidItemHandler implements IItemHandler {
    private final VoidHopperBlockEntity hopper;

    public VoidItemHandler(VoidHopperBlockEntity hopper) {
        this.hopper = hopper;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return hopper.accepts(stack) ? ItemStack.EMPTY : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return hopper.accepts(stack);
    }
}
