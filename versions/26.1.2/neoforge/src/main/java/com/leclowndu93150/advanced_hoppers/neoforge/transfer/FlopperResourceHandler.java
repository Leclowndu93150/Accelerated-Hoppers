package com.leclowndu93150.advanced_hoppers.neoforge.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.neoforge.platform.NeoForgeFluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.SimpleFluidTank;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class FlopperResourceHandler extends SnapshotJournal<FluidVolume> implements ResourceHandler<FluidResource> {
    private final SimpleFluidTank tank;

    public FlopperResourceHandler(FlopperBlockEntity flopper) {
        this.tank = flopper.getTank();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        return NeoForgeFluidHandler.toResource(tank.getFluid());
    }

    @Override
    public long getAmountAsLong(int index) {
        return tank.getFluidAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return tank.getCapacity();
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return true;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        FluidVolume fluid = tank.getFluid();
        if (!FluidVolume.isSameFluidSameComponents(fluid, NeoForgeFluidHandler.toVolume(resource, amount))) {
            return 0;
        }
        int extracted = Math.min(amount, fluid.getAmount());
        if (extracted <= 0) {
            return 0;
        }
        updateSnapshots(transaction);
        tank.setFluid(fluid.copyWithAmount(fluid.getAmount() - extracted));
        return extracted;
    }

    @Override
    protected FluidVolume createSnapshot() {
        return tank.getFluid().copy();
    }

    @Override
    protected void revertToSnapshot(FluidVolume snapshot) {
        tank.setFluid(snapshot);
    }

    @Override
    protected void onRootCommit(FluidVolume originalState) {
        tank.onContentsChanged();
    }
}
