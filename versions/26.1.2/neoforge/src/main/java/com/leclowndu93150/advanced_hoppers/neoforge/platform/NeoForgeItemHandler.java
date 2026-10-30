package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public record NeoForgeItemHandler(ResourceHandler<ItemResource> handler) implements ItemHandler {
    @Override
    public int size() {
        return handler.size();
    }

    @Override
    public ItemStack getStack(int index) {
        return handler.getResource(index).toStack(handler.getAmountAsInt(index));
    }

    @Override
    public int getCapacity(int index, ItemStack resource) {
        return handler.getCapacityAsInt(index, ItemResource.of(resource));
    }

    @Override
    public int insert(ItemStack resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        return handler.insert(ItemResource.of(resource), amount, NeoForgeTransaction.unwrap(transaction));
    }

    @Override
    public int extract(int index, ItemStack resource, int amount, TransferTransaction transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }
        return handler.extract(index, ItemResource.of(resource), amount, NeoForgeTransaction.unwrap(transaction));
    }
}
