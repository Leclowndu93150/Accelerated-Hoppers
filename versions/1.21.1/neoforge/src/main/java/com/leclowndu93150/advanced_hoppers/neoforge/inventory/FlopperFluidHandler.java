package com.leclowndu93150.advanced_hoppers.neoforge.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.neoforge.platform.NeoForgeFluidHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FlopperFluidHandler implements IFluidHandler {
    private final FlopperBlockEntity flopper;

    public FlopperFluidHandler(FlopperBlockEntity flopper) {
        this.flopper = flopper;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return NeoForgeFluidHandler.toNeoForge(flopper.getTank().getFluid());
    }

    @Override
    public int getTankCapacity(int i) {
        return flopper.getTank().getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return NeoForgeFluidHandler.toNeoForge(flopper.getTank().drain(NeoForgeFluidHandler.toCommon(fluidStack), fluidAction.simulate()));
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return NeoForgeFluidHandler.toNeoForge(flopper.getTank().drain(i, fluidAction.simulate()));
    }
}
