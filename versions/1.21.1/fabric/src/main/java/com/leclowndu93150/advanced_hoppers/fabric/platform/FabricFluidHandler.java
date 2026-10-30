package com.leclowndu93150.advanced_hoppers.fabric.platform;

import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FabricFluidHandler implements FluidHandler {
    public static final long DROPLETS_PER_MB = FluidConstants.BUCKET / 1000;

    private final Storage<FluidVariant> storage;
    private final List<StorageView<FluidVariant>> views = new ArrayList<>();

    public FabricFluidHandler(Storage<FluidVariant> storage) {
        this.storage = storage;
        for (StorageView<FluidVariant> view : storage) {
            views.add(view);
        }
    }

    public static FluidVariant toVariant(FluidVolume volume) {
        return FluidVariant.of(volume.getFluid(), volume.getComponents());
    }

    private static FluidVolume toVolume(FluidVariant variant, long droplets) {
        return new FluidVolume(variant.getFluid(), (int) Math.min(droplets / DROPLETS_PER_MB, Integer.MAX_VALUE), variant.getComponents());
    }

    @Override
    public int getTanks() {
        return views.size();
    }

    @Override
    public FluidVolume getFluidInTank(int tank) {
        StorageView<FluidVariant> view = views.get(tank);
        return view.isResourceBlank() ? FluidVolume.EMPTY : toVolume(view.getResource(), view.getAmount());
    }

    @Override
    public int getTankCapacity(int tank) {
        return (int) Math.min(views.get(tank).getCapacity() / DROPLETS_PER_MB, Integer.MAX_VALUE);
    }

    @Override
    public int fill(FluidVolume resource, boolean simulate) {
        if (resource.isEmpty()) {
            return 0;
        }
        FluidVariant variant = toVariant(resource);
        try (Transaction transaction = Transaction.openOuter()) {
            long accepted;
            try (Transaction nested = transaction.openNested()) {
                accepted = storage.insert(variant, resource.getAmount() * DROPLETS_PER_MB, nested);
            }
            long amount = accepted / DROPLETS_PER_MB;
            if (amount == 0 || simulate) {
                return (int) amount;
            }
            long inserted = storage.insert(variant, amount * DROPLETS_PER_MB, transaction);
            transaction.commit();
            return (int) (inserted / DROPLETS_PER_MB);
        }
    }

    @Override
    public FluidVolume drain(FluidVolume resource, boolean simulate) {
        if (resource.isEmpty()) {
            return FluidVolume.EMPTY;
        }
        FluidVariant variant = toVariant(resource);
        try (Transaction transaction = Transaction.openOuter()) {
            long available;
            try (Transaction nested = transaction.openNested()) {
                available = storage.extract(variant, resource.getAmount() * DROPLETS_PER_MB, nested);
            }
            long amount = available / DROPLETS_PER_MB;
            if (amount == 0) {
                return FluidVolume.EMPTY;
            }
            if (simulate) {
                return toVolume(variant, amount * DROPLETS_PER_MB);
            }
            long extracted = storage.extract(variant, amount * DROPLETS_PER_MB, transaction);
            transaction.commit();
            return toVolume(variant, extracted);
        }
    }

    @Override
    public FluidVolume drain(int maxDrain, boolean simulate) {
        for (StorageView<FluidVariant> view : views) {
            if (!view.isResourceBlank()) {
                return drain(toVolume(view.getResource(), maxDrain * DROPLETS_PER_MB), simulate);
            }
        }
        return FluidVolume.EMPTY;
    }
}
