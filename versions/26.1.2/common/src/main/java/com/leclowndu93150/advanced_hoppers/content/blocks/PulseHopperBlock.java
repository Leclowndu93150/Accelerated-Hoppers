package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.PulseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public class PulseHopperBlock extends ConfiguredHopperBlock<PulseHopperBlockEntity> {
    public PulseHopperBlock(Properties properties) {
        super(properties, () -> AHRegistry.PULSE_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.pulseHopperTransferCooldown, BaseHopperBlockEntity::baseTick);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide() && level.hasNeighborSignal(pos) && state.getValue(ENABLED)
                && level.getBlockEntity(pos) instanceof PulseHopperBlockEntity pulse) {
            pulse.pulse();
        }
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }
}
