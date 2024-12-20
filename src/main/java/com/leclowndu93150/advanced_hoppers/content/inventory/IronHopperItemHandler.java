package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.IronHopperBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class IronHopperItemHandler extends InvWrapper
{
    private final IronHopperBlockEntity hopper;

    public IronHopperItemHandler(IronHopperBlockEntity hopper) {
        super(hopper);
        this.hopper = hopper;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (simulate) {
            return super.insertItem(slot, stack, true);
        } else {
            boolean wasEmpty = getInv().isEmpty();
            int originalStackSize = stack.getCount();
            stack = super.insertItem(slot, stack, false);
            if (wasEmpty && originalStackSize > stack.getCount())
            {
                if (hopper.mayNotTransfer())
                {
                    hopper.setTransferCooldown(Config.ironHopperTransferCooldown);
                }
            }
            return stack;
        }
    }
}