package com.leclowndu93150.advanced_hoppers.neoforge.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.VoidHopperBlockEntity;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class VoidResourceHandler implements ResourceHandler<ItemResource> {
    private final VoidHopperBlockEntity hopper;

    public VoidResourceHandler(VoidHopperBlockEntity hopper) {
        this.hopper = hopper;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ItemResource getResource(int index) {
        return ItemResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int index) {
        return 0;
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return resource.isEmpty() || hopper.accepts(resource.toStack()) ? Integer.MAX_VALUE : 0;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return hopper.accepts(resource.toStack());
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        return hopper.accepts(resource.toStack()) ? amount : 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }
}
