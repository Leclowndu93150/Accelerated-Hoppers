package com.leclowndu93150.advanced_hoppers.fabric.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.fabric.platform.FabricFluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.SimpleFluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class FlopperFluidStorage extends SnapshotParticipant<FluidVolume> implements SingleSlotStorage<FluidVariant> {
    private final SimpleFluidTank tank;

    public FlopperFluidStorage(FlopperBlockEntity flopper) {
        this.tank = flopper.getTank();
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        FluidVolume fluid = tank.getFluid();
        if (fluid.isEmpty() || !resource.isOf(fluid.getFluid()) || !resource.componentsMatch(fluid.getComponents())) {
            return 0;
        }
        int amount = (int) Math.min(fluid.getAmount(), maxAmount / FabricFluidHandler.DROPLETS_PER_MB);
        if (amount <= 0) {
            return 0;
        }
        updateSnapshots(transaction);
        tank.setFluid(fluid.copyWithAmount(fluid.getAmount() - amount));
        return amount * FabricFluidHandler.DROPLETS_PER_MB;
    }

    @Override
    public boolean isResourceBlank() {
        return tank.isEmpty();
    }

    @Override
    public FluidVariant getResource() {
        return FabricFluidHandler.toVariant(tank.getFluid());
    }

    @Override
    public long getAmount() {
        return tank.getFluidAmount() * FabricFluidHandler.DROPLETS_PER_MB;
    }

    @Override
    public long getCapacity() {
        return tank.getCapacity() * FabricFluidHandler.DROPLETS_PER_MB;
    }

    @Override
    protected FluidVolume createSnapshot() {
        return tank.getFluid().copy();
    }

    @Override
    protected void readSnapshot(FluidVolume snapshot) {
        tank.setFluid(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        tank.onContentsChanged();
    }
}
