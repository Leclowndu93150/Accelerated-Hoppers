package com.leclowndu93150.advanced_hoppers.transfer;

public interface FluidHandler {
    int size();

    FluidVolume getFluid(int index);

    int getCapacity(int index, FluidVolume resource);

    int insert(FluidVolume resource, int amount, TransferTransaction transaction);

    int extract(FluidVolume resource, int amount, TransferTransaction transaction);
}
