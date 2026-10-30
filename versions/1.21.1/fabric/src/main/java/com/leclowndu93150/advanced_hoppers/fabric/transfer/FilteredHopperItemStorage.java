package com.leclowndu93150.advanced_hoppers.fabric.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredHopperBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage;

public class FilteredHopperItemStorage extends FilteringStorage<ItemVariant> {
    public FilteredHopperItemStorage(FilteredHopperBlockEntity hopper) {
        super(InventoryStorage.of(hopper, null));
    }

    @Override
    protected boolean canInsert(ItemVariant resource) {
        return false;
    }

    @Override
    protected boolean canExtract(ItemVariant resource) {
        return false;
    }
}
