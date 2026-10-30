package com.leclowndu93150.advanced_hoppers.content.blocks;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ConfiguredHopperBlock<T extends BlockEntity> extends BaseHopperBlock<T> {
    private final IntSupplier cooldown;
    private final BlockEntityTicker<? super T> ticker;

    public ConfiguredHopperBlock(Properties properties, Supplier<BlockEntityType<T>> blockEntityType, IntSupplier cooldown, BlockEntityTicker<? super T> ticker) {
        super(properties, blockEntityType);
        this.cooldown = cooldown;
        this.ticker = ticker;
    }

    @Override
    public int getTransferCooldown() {
        return cooldown.getAsInt();
    }

    @Override
    public <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(Level level, BlockState state, BlockEntityType<BE> type) {
        return level.isClientSide ? null : createTickerHelper(type, blockEntityType.get(), ticker);
    }
}
