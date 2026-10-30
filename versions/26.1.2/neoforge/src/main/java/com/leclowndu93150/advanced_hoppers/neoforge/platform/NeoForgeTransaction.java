package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public record NeoForgeTransaction(Transaction transaction) implements TransferTransaction {
    public static TransactionContext unwrap(TransferTransaction transaction) {
        return ((NeoForgeTransaction) transaction).transaction();
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
