package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.GoldenHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GoldenHopperBlock extends BaseHopperBlock<GoldenHopperBlockEntity> {
    public GoldenHopperBlock(Properties properties) {
        super(properties, Registry.GOLDEN_HOPPER_BLOCK_ENTITY_TYPE);
    }

    @Override
    protected int getTransferCooldown() {
        return Config.goldenHopperTransferCooldown;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, Registry.GOLDEN_HOPPER_BLOCK_ENTITY_TYPE.get(), GoldenHopperBlockEntity::tick);
    }
}
