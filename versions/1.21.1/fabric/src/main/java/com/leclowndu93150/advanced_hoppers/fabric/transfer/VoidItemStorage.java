package com.leclowndu93150.advanced_hoppers.fabric.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.VoidHopperBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class VoidItemStorage implements InsertionOnlyStorage<ItemVariant> {
    private final VoidHopperBlockEntity hopper;

    public VoidItemStorage(VoidHopperBlockEntity hopper) {
        this.hopper = hopper;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        return hopper.accepts(resource.toStack()) ? maxAmount : 0;
    }
}
