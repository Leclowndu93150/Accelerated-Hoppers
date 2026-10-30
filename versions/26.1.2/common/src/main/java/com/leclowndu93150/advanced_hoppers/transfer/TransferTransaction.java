package com.leclowndu93150.advanced_hoppers.transfer;

public interface TransferTransaction extends AutoCloseable {
    void commit();

    @Override
    void close();
}
