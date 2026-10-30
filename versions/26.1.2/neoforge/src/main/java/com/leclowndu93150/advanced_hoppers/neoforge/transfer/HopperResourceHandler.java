package com.leclowndu93150.advanced_hoppers.neoforge.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class HopperResourceHandler extends SnapshotJournal<Boolean> implements ResourceHandler<ItemResource> {
    private final BaseHopperBlockEntity hopper;
    private final int cooldown;
    private final ResourceHandler<ItemResource> backing;
    private boolean triggerCooldown;

    public HopperResourceHandler(BaseHopperBlockEntity hopper, int cooldown) {
        this.hopper = hopper;
        this.cooldown = cooldown;
        this.backing = VanillaContainerWrapper.of(hopper);
    }

    private void onInserted(boolean wasEmpty, int inserted, TransactionContext transaction) {
        if (wasEmpty && inserted > 0) {
            updateSnapshots(transaction);
            triggerCooldown = true;
        }
    }

    @Override
    public int size() {
        return backing.size();
    }

    @Override
    public ItemResource getResource(int index) {
        return backing.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return backing.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return backing.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return backing.isValid(index, resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        boolean wasEmpty = hopper.isEmpty();
        int inserted = backing.insert(index, resource, amount, transaction);
        onInserted(wasEmpty, inserted, transaction);
        return inserted;
    }

    @Override
    public int insert(ItemResource resource, int amount, TransactionContext transaction) {
        boolean wasEmpty = hopper.isEmpty();
        int inserted = backing.insert(resource, amount, transaction);
        onInserted(wasEmpty, inserted, transaction);
        return inserted;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return backing.extract(index, resource, amount, transaction);
    }

    @Override
    protected Boolean createSnapshot() {
        return triggerCooldown;
    }

    @Override
    protected void revertToSnapshot(Boolean snapshot) {
        triggerCooldown = snapshot;
    }

    @Override
    protected void onRootCommit(Boolean originalState) {
        if (triggerCooldown) {
            triggerCooldown = false;
            if (hopper.mayNotTransfer()) {
                hopper.setTransferCooldown(cooldown);
            }
        }
    }
}
