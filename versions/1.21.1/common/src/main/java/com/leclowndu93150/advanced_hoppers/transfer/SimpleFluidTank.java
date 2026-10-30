package com.leclowndu93150.advanced_hoppers.transfer;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class SimpleFluidTank implements FluidHandler {
    private final int capacity;
    private final Runnable changeListener;
    private FluidVolume fluid = FluidVolume.EMPTY;

    public SimpleFluidTank(int capacity, Runnable changeListener) {
        this.capacity = capacity;
        this.changeListener = changeListener;
    }

    public int getCapacity() {
        return capacity;
    }

    public FluidVolume getFluid() {
        return fluid;
    }

    public void setFluid(FluidVolume stack) {
        this.fluid = stack;
    }

    public int getFluidAmount() {
        return fluid.getAmount();
    }

    public boolean isEmpty() {
        return fluid.isEmpty();
    }

    public void onContentsChanged() {
        changeListener.run();
    }

    public SimpleFluidTank readFromNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        fluid = FluidVolume.parseOptional(provider, nbt.getCompound("Fluid"));
        return this;
    }

    public CompoundTag writeToNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (!fluid.isEmpty()) {
            nbt.put("Fluid", fluid.save(provider));
        }
        return nbt;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidVolume getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return getCapacity();
    }

    @Override
    public int fill(FluidVolume resource, boolean simulate) {
        if (resource.isEmpty()) {
            return 0;
        }
        if (simulate) {
            if (fluid.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }
            if (!FluidVolume.isSameFluidSameComponents(fluid, resource)) {
                return 0;
            }
            return Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            fluid = resource.copyWithAmount(Math.min(capacity, resource.getAmount()));
            onContentsChanged();
            return fluid.getAmount();
        }
        if (!FluidVolume.isSameFluidSameComponents(fluid, resource)) {
            return 0;
        }
        int filled = capacity - fluid.getAmount();
        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        if (filled > 0) {
            onContentsChanged();
        }
        return filled;
    }

    @Override
    public FluidVolume drain(FluidVolume resource, boolean simulate) {
        if (resource.isEmpty() || !FluidVolume.isSameFluidSameComponents(resource, fluid)) {
            return FluidVolume.EMPTY;
        }
        return drain(resource.getAmount(), simulate);
    }

    @Override
    public FluidVolume drain(int maxDrain, boolean simulate) {
        int drained = Math.min(maxDrain, fluid.getAmount());
        FluidVolume stack = fluid.copyWithAmount(drained);
        if (!simulate && drained > 0) {
            fluid.shrink(drained);
            onContentsChanged();
        }
        return stack;
    }
}
