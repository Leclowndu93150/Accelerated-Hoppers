package com.leclowndu93150.advanced_hoppers.content.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SingleItemSlot extends Slot {
    public SingleItemSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty();
    }

    @Override
    public void set(ItemStack stack) {
        if (!stack.isEmpty()) {
            ItemStack singleItem = stack.copy();
            singleItem.setCount(1);
            stack.shrink(1);
            super.set(singleItem);
        } else {
            super.set(ItemStack.EMPTY);
        }
    }
}

