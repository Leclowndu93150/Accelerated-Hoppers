package com.leclowndu93150.advanced_hoppers.neoforge.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredHopperBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class FilteredHopperItemHandler extends InvWrapper {
    public FilteredHopperItemHandler(FilteredHopperBlockEntity hopper) {
        super(hopper);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
