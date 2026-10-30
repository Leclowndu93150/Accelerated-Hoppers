package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.OtherHopperContainer;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class UpwardHopperBlockEntity extends BaseHopperBlockEntity {
    public UpwardHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.UPWARD_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, Config.upwardHopperTransferCooldown, "Upward Hopper");
    }

    public static Direction outputOf(BlockState state) {
        Direction facing = state.getValue(HopperBlock.FACING);
        return facing == Direction.DOWN ? Direction.UP : facing;
    }

    @Override
    protected Direction pullDirection() {
        return Direction.DOWN;
    }

    @Override
    protected List<Direction> outputDirections() {
        return List.of(outputOf(getBlockState()));
    }

    @Override
    public void onItemEntityIsCaptured(ItemEntity itemEntity) {
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new OtherHopperContainer(id, player, this);
    }
}
