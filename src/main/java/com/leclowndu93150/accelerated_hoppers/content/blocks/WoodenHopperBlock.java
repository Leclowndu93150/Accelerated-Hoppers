package com.leclowndu93150.accelerated_hoppers.content.blocks;

import com.leclowndu93150.accelerated_hoppers.content.blockentities.WoodenHopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.registries.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WoodenHopperBlock extends HopperBlock {
    public WoodenHopperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Registry.WOODEN_HOPPER_BLOCK_ENTITY_TYPE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, Registry.WOODEN_HOPPER_BLOCK_ENTITY_TYPE.get(), WoodenHopperBlockEntity::tick);
    }
}
