package com.leclowndu93150.advanced_hoppers.fabric.platform;

import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

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
        return new FluidVolume(variant.getFluid(), (int) Math.min(droplets / DROPLETS_PER_MB, Integer.MAX_VALUE), variant.getComponentsPatch());
    }

    @Override
    public int size() {
        return views.size();
    }

    @Override
    public FluidVolume getFluid(int index) {
        StorageView<FluidVariant> view = views.get(index);
        return view.isResourceBlank() ? FluidVolume.EMPTY : toVolume(view.getResource(), view.getAmount());
    }

    @Override
    public int getCapacity(int index, FluidVolume resource) {
        return (int) Math.min(views.get(index).getCapacity() / DROPLETS_PER_MB, Integer.MAX_VALUE);
    }

    @Override
    public int insert(FluidVolume resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        FluidVariant variant = toVariant(resource);
        TransactionContext context = FabricTransaction.unwrap(transaction);
        long accepted;
        try (Transaction nested = context.openNested()) {
            accepted = storage.insert(variant, amount * DROPLETS_PER_MB, nested);
        }
        long wholeMb = accepted / DROPLETS_PER_MB;
        if (wholeMb == 0) {
            return 0;
        }
        return (int) (storage.insert(variant, wholeMb * DROPLETS_PER_MB, context) / DROPLETS_PER_MB);
    }

    @Override
    public int extract(FluidVolume resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        FluidVariant variant = toVariant(resource);
        TransactionContext context = FabricTransaction.unwrap(transaction);
        long available;
        try (Transaction nested = context.openNested()) {
            available = storage.extract(variant, amount * DROPLETS_PER_MB, nested);
        }
        long wholeMb = available / DROPLETS_PER_MB;
        if (wholeMb == 0) {
            return 0;
        }
        return (int) (storage.extract(variant, wholeMb * DROPLETS_PER_MB, context) / DROPLETS_PER_MB);
    }
}
