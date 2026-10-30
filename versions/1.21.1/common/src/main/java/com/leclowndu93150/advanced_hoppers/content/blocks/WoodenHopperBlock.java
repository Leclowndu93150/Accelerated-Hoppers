package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.WoodenHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WoodenHopperBlock extends BaseHopperBlock<WoodenHopperBlockEntity> {
    public WoodenHopperBlock(Properties properties) {
        super(properties, () -> AHRegistry.WOODEN_HOPPER_BLOCK_ENTITY_TYPE);
    }

    @Override
    public int getTransferCooldown() {
        return Config.woodenHopperTransferCooldown;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, AHRegistry.WOODEN_HOPPER_BLOCK_ENTITY_TYPE, WoodenHopperBlockEntity::tick);
    }
}
