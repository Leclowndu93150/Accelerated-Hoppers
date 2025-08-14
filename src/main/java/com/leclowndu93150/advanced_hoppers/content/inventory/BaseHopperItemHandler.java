package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHopperItemHandler<T extends BaseHopperBlockEntity> extends InvWrapper {
    protected final T hopper;
    protected final int cooldown;

    public BaseHopperItemHandler(T hopper, int cooldown) {
        super(hopper);
        this.hopper = hopper;
        this.cooldown = cooldown;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (simulate) {
            return super.insertItem(slot, stack, true);
        } else {
            boolean wasEmpty = getInv().isEmpty();
            int originalStackSize = stack.getCount();
            stack = super.insertItem(slot, stack, false);
            if (wasEmpty && originalStackSize > stack.getCount()) {
                if (hopper.mayNotTransfer()) {
                    hopper.setTransferCooldown(cooldown);
                }
            }
            return stack;
        }
    }
}