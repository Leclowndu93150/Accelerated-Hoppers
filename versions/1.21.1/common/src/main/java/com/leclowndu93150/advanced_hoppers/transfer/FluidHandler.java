package com.leclowndu93150.advanced_hoppers.transfer;

public interface FluidHandler {
    int getTanks();

    FluidVolume getFluidInTank(int tank);

    int getTankCapacity(int tank);

    int fill(FluidVolume resource, boolean simulate);

    FluidVolume drain(FluidVolume resource, boolean simulate);

    FluidVolume drain(int maxDrain, boolean simulate);
}
