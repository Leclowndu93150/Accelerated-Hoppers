package com.leclowndu93150.advanced_hoppers.transfer;

import net.minecraft.world.item.ItemStack;

public interface ItemHandler {
    int size();

    ItemStack getStack(int index);

    int getCapacity(int index, ItemStack resource);

    int insert(ItemStack resource, int amount, TransferTransaction transaction);

    int extract(int index, ItemStack resource, int amount, TransferTransaction transaction);
}
