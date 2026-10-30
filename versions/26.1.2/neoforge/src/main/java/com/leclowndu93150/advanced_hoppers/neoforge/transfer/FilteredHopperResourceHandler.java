package com.leclowndu93150.advanced_hoppers.neoforge.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredHopperBlockEntity;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class FilteredHopperResourceHandler implements ResourceHandler<ItemResource> {
    private final FilteredHopperBlockEntity hopper;

    public FilteredHopperResourceHandler(FilteredHopperBlockEntity hopper) {
        this.hopper = hopper;
    }

    @Override
    public int size() {
        return hopper.getContainerSize();
    }

    @Override
    public ItemResource getResource(int index) {
        return ItemResource.of(hopper.getItem(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return hopper.getItem(index).getCount();
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return 1;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return false;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }
}
