package com.leclowndu93150.advanced_hoppers.fabric.platform;

import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FabricItemHandler implements ItemHandler {
    private final Storage<ItemVariant> storage;
    private final List<StorageView<ItemVariant>> views = new ArrayList<>();
    private final boolean slotted;

    public FabricItemHandler(Storage<ItemVariant> storage) {
        this.storage = storage;
        this.slotted = storage instanceof SlottedStorage<ItemVariant>;
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            views.addAll(slottedStorage.getSlots());
        } else {
            for (StorageView<ItemVariant> view : storage) {
                views.add(view);
            }
        }
    }

    private StorageView<ItemVariant> view(int slot) {
        return slot < views.size() ? views.get(slot) : null;
    }

    @Override
    public int getSlots() {
        return slotted ? views.size() : views.size() + 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        StorageView<ItemVariant> view = view(slot);
        if (view == null || view.isResourceBlank()) {
            return ItemStack.EMPTY;
        }
        return view.getResource().toStack((int) Math.min(view.getAmount(), Integer.MAX_VALUE));
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        StorageView<ItemVariant> view = view(slot);
        Storage<ItemVariant> target = view == null ? storage : view instanceof SingleSlotStorage<ItemVariant> slotStorage ? slotStorage : null;
        if (target == null) {
            return stack;
        }
        long inserted;
        try (Transaction transaction = Transaction.openOuter()) {
            inserted = target.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
        }
        if (inserted == 0) {
            return stack;
        }
        if (inserted >= stack.getCount()) {
            return ItemStack.EMPTY;
        }
        return stack.copyWithCount(stack.getCount() - (int) inserted);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        StorageView<ItemVariant> view = view(slot);
        if (amount <= 0 || view == null || view.isResourceBlank()) {
            return ItemStack.EMPTY;
        }
        ItemVariant resource = view.getResource();
        long extracted;
        try (Transaction transaction = Transaction.openOuter()) {
            extracted = view.extract(resource, amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
        }
        return extracted == 0 ? ItemStack.EMPTY : resource.toStack((int) extracted);
    }

    @Override
    public int getSlotLimit(int slot) {
        StorageView<ItemVariant> view = view(slot);
        return view == null ? Item.ABSOLUTE_MAX_STACK_SIZE : (int) Math.min(view.getCapacity(), Integer.MAX_VALUE);
    }
}
