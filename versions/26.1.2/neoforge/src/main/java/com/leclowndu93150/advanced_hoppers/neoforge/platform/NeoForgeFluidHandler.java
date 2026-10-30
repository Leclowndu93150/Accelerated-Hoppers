package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public record NeoForgeFluidHandler(ResourceHandler<FluidResource> handler) implements FluidHandler {
    public static FluidResource toResource(FluidVolume volume) {
        return volume.isEmpty() ? FluidResource.EMPTY : FluidResource.of(volume.getFluid(), volume.getComponents());
    }

    public static FluidVolume toVolume(FluidResource resource, int amount) {
        return resource.isEmpty() ? FluidVolume.EMPTY : new FluidVolume(resource.getFluid(), amount, resource.getComponentsPatch());
    }

    @Override
    public int size() {
        return handler.size();
    }

    @Override
    public FluidVolume getFluid(int index) {
        return toVolume(handler.getResource(index), handler.getAmountAsInt(index));
    }

    @Override
    public int getCapacity(int index, FluidVolume resource) {
        return handler.getCapacityAsInt(index, toResource(resource));
    }

    @Override
    public int insert(FluidVolume resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        return handler.insert(toResource(resource), amount, NeoForgeTransaction.unwrap(transaction));
    }

    @Override
    public int extract(FluidVolume resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        return handler.extract(toResource(resource), amount, NeoForgeTransaction.unwrap(transaction));
    }
}
