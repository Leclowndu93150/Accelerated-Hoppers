package com.leclowndu93150.advanced_hoppers.fabric.platform;

import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public record FabricTransaction(Transaction transaction) implements TransferTransaction {
    public static TransactionContext unwrap(TransferTransaction transaction) {
        return ((FabricTransaction) transaction).transaction();
    }

    @Override
    public void commit() {
        transaction.commit();
    }

    @Override
    public void close() {
        transaction.close();
    }
}
