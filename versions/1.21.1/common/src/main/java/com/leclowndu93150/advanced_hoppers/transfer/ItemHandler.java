package com.leclowndu93150.advanced_hoppers.transfer;

import net.minecraft.world.item.ItemStack;

public interface ItemHandler {
    int getSlots();

    ItemStack getStackInSlot(int slot);

    ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

    ItemStack extractItem(int slot, int amount, boolean simulate);

    int getSlotLimit(int slot);
}
