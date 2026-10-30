package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class PlacingFlopperBlockEntity extends FlopperBlockEntity {
    private static final int PLACE_INTERVAL = 8;

    private int placeCooldown;

    public PlacingFlopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.PLACING_FLOPPER_BLOCK_ENTITY_TYPE, pos, state, Config.flopperCapacity, () -> Config.flopperIORate);
    }

    @Override
    protected boolean transferFluidOut() {
        Direction facing = this.getBlockState().getValue(HopperBlock.FACING);
        if (getFluidHandler(this, facing).isPresent()) {
            return super.transferFluidOut();
        }
        if (--this.placeCooldown > 0) {
            return false;
        }
        this.placeCooldown = PLACE_INTERVAL;
        return placeInWorld(this.worldPosition.relative(facing));
    }

    private boolean placeInWorld(BlockPos target) {
        FluidVolume stored = this.tank.getFluid();
        if (stored.getAmount() < 1000 || !(stored.getFluid() instanceof FlowingFluid flowing)) {
            return false;
        }
        Fluid source = flowing.getSource();
        BlockState placed = source.defaultFluidState().createLegacyBlock();
        if (placed.isAir()) {
            return false;
        }
        BlockState existing = this.level.getBlockState(target);
        if (!existing.isAir() && !(existing.canBeReplaced(source) && !existing.getFluidState().isSource())) {
            return false;
        }
        if (source.is(FluidTags.WATER) && this.level.dimensionType().ultraWarm()) {
            return false;
        }
        if (!existing.isAir() && !existing.liquid()) {
            this.level.destroyBlock(target, true);
        }
        this.level.setBlockAndUpdate(target, placed);
        this.level.playSound(null, target, source.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.tank.drain(1000, false);
        return true;
    }
}
