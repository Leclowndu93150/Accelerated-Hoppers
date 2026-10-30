package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class NeoForgeFluidHandler implements FluidHandler {
    private final IFluidHandler handler;

    public NeoForgeFluidHandler(IFluidHandler handler) {
        this.handler = handler;
    }

    public static FluidVolume toCommon(FluidStack stack) {
        return stack.isEmpty() ? FluidVolume.EMPTY : new FluidVolume(stack.getFluid(), stack.getAmount(), stack.getComponentsPatch());
    }

    public static FluidStack toNeoForge(FluidVolume volume) {
        return volume.isEmpty() ? FluidStack.EMPTY : new FluidStack(volume.getFluid().builtInRegistryHolder(), volume.getAmount(), volume.getComponents());
    }

    private static IFluidHandler.FluidAction action(boolean simulate) {
        return simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
    }

    protected void onExecuted() {
    }

    @Override
    public int getTanks() {
        return handler.getTanks();
    }

    @Override
    public FluidVolume getFluidInTank(int tank) {
        return toCommon(handler.getFluidInTank(tank));
    }

    @Override
    public int getTankCapacity(int tank) {
        return handler.getTankCapacity(tank);
    }

    @Override
    public int fill(FluidVolume resource, boolean simulate) {
        int filled = handler.fill(toNeoForge(resource), action(simulate));
        if (!simulate && filled > 0) {
            onExecuted();
        }
        return filled;
    }

    @Override
    public FluidVolume drain(FluidVolume resource, boolean simulate) {
        FluidStack drained = handler.drain(toNeoForge(resource), action(simulate));
        if (!simulate && !drained.isEmpty()) {
            onExecuted();
        }
        return toCommon(drained);
    }

    @Override
    public FluidVolume drain(int maxDrain, boolean simulate) {
        FluidStack drained = handler.drain(maxDrain, action(simulate));
        if (!simulate && !drained.isEmpty()) {
            onExecuted();
        }
        return toCommon(drained);
    }
}
