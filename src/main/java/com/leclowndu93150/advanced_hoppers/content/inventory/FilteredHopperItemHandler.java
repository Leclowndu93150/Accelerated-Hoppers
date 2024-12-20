package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredHopperBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class FilteredHopperItemHandler extends InvWrapper
{
    private final FilteredHopperBlockEntity hopper;

    public FilteredHopperItemHandler(FilteredHopperBlockEntity hopper) {
        super(hopper);
        this.hopper = hopper;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        return stack;
    }
}