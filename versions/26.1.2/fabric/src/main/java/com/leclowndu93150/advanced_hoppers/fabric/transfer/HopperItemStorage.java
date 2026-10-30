package com.leclowndu93150.advanced_hoppers.fabric.transfer;

import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class HopperItemStorage extends SnapshotParticipant<Boolean> implements SlottedStorage<ItemVariant> {
    private final BaseHopperBlockEntity hopper;
    private final int cooldown;
    private final ContainerStorage backing;
    private final List<SingleSlotStorage<ItemVariant>> slots;
    private boolean triggerCooldown;

    public HopperItemStorage(BaseHopperBlockEntity hopper, int cooldown) {
        this.hopper = hopper;
        this.cooldown = cooldown;
        this.backing = ContainerStorage.of(hopper, null);
        this.slots = backing.getSlots().stream().<SingleSlotStorage<ItemVariant>>map(Slot::new).toList();
    }

    private void onInserted(boolean wasEmpty, long inserted, TransactionContext transaction) {
        if (wasEmpty && inserted > 0) {
            updateSnapshots(transaction);
            triggerCooldown = true;
        }
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        boolean wasEmpty = hopper.isEmpty();
        long inserted = backing.insert(resource, maxAmount, transaction);
        onInserted(wasEmpty, inserted, transaction);
        return inserted;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        return backing.extract(resource, maxAmount, transaction);
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        return Collections.<StorageView<ItemVariant>>unmodifiableList(slots).iterator();
    }

    @Override
    public int getSlotCount() {
        return slots.size();
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    protected Boolean createSnapshot() {
        return triggerCooldown;
    }

    @Override
    protected void readSnapshot(Boolean snapshot) {
        triggerCooldown = snapshot;
    }

    @Override
    protected void onFinalCommit() {
        if (triggerCooldown) {
            triggerCooldown = false;
            if (hopper.mayNotTransfer()) {
                hopper.setTransferCooldown(cooldown);
            }
        }
    }

    private class Slot implements SingleSlotStorage<ItemVariant> {
        private final SingleSlotStorage<ItemVariant> delegate;

        private Slot(SingleSlotStorage<ItemVariant> delegate) {
            this.delegate = delegate;
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            boolean wasEmpty = hopper.isEmpty();
            long inserted = delegate.insert(resource, maxAmount, transaction);
            onInserted(wasEmpty, inserted, transaction);
            return inserted;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return delegate.extract(resource, maxAmount, transaction);
        }

        @Override
        public boolean isResourceBlank() {
            return delegate.isResourceBlank();
        }

        @Override
        public ItemVariant getResource() {
            return delegate.getResource();
        }

        @Override
        public long getAmount() {
            return delegate.getAmount();
        }

        @Override
        public long getCapacity() {
            return delegate.getCapacity();
        }
    }
}
