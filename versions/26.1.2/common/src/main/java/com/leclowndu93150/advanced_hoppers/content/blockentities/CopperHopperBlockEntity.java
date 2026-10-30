package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blocks.CopperHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.inventory.OtherHopperContainer;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CopperHopperBlockEntity extends BaseHopperBlockEntity {
    public CopperHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.COPPER_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, cooldownOf(state), "Copper Hopper");
    }

    private static int cooldownOf(BlockState state) {
        return state.getBlock() instanceof CopperHopperBlock copper ? copper.getTransferCooldown() : Config.copperHopperTransferCooldown;
    }

    @Override
    public int getBaseCooldown() {
        return cooldownOf(getBlockState());
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new OtherHopperContainer(id, player, this);
    }
}
