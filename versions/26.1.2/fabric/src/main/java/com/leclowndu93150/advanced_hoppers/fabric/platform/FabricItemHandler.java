package com.leclowndu93150.advanced_hoppers.fabric.platform;

import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FabricItemHandler implements ItemHandler {
    private final Storage<ItemVariant> storage;
    private final List<StorageView<ItemVariant>> views = new ArrayList<>();

    public FabricItemHandler(Storage<ItemVariant> storage) {
        this.storage = storage;
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            views.addAll(slottedStorage.getSlots());
        } else {
            for (StorageView<ItemVariant> view : storage) {
                views.add(view);
            }
        }
    }

    @Override
    public int size() {
        return views.size();
    }

    @Override
    public ItemStack getStack(int index) {
        StorageView<ItemVariant> view = views.get(index);
        if (view.isResourceBlank()) {
            return ItemStack.EMPTY;
        }
        return view.getResource().toStack((int) Math.min(view.getAmount(), Integer.MAX_VALUE));
    }

    @Override
    public int getCapacity(int index, ItemStack resource) {
        return (int) Math.min(views.get(index).getCapacity(), Integer.MAX_VALUE);
    }

    @Override
    public int insert(ItemStack resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        return (int) storage.insert(ItemVariant.of(resource), amount, FabricTransaction.unwrap(transaction));
    }

    @Override
    public int extract(int index, ItemStack resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        return (int) views.get(index).extract(ItemVariant.of(resource), amount, FabricTransaction.unwrap(transaction));
    }
}
