package com.leclowndu93150.advanced_hoppers.transfer;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SimpleFluidTank {
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

    public void load(ValueInput input) {
        fluid = input.read("Fluid", FluidVolume.CODEC).orElse(FluidVolume.EMPTY);
    }

    public void save(ValueOutput output) {
        if (!fluid.isEmpty()) {
            output.store("Fluid", FluidVolume.CODEC, fluid);
        }
    }

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

    public FluidVolume drain(FluidVolume resource, boolean simulate) {
        if (resource.isEmpty() || !FluidVolume.isSameFluidSameComponents(resource, fluid)) {
            return FluidVolume.EMPTY;
        }
        return drain(resource.getAmount(), simulate);
    }

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
