package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.OtherHopperContainer;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class IronHopperBlockEntity extends BaseHopperBlockEntity {
    public IronHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.IRON_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, Config.ironHopperTransferCooldown, "Iron Hopper");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new OtherHopperContainer(id, player, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IronHopperBlockEntity entity) {
        BaseHopperBlockEntity.baseTick(level, pos, state, entity);
    }
}
