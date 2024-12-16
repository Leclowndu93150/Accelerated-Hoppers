package com.leclowndu93150.accelerated_hoppers.content.inventory;

import com.leclowndu93150.accelerated_hoppers.Config;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.FlopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
        return flopper.getTank().getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return Config.flopperCapacity;
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
        return flopper.getTank().drain(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return flopper.getTank().drain(i, fluidAction);
    }
}
