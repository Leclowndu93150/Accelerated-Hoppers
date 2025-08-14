package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.WoodenHopperContainer;
import com.leclowndu93150.advanced_hoppers.registries.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WoodenHopperBlockEntity extends BaseHopperBlockEntity {
    public WoodenHopperBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.WOODEN_HOPPER_BLOCK_ENTITY_TYPE.get(), pos, state, 1, Config.woodenHopperTransferCooldown, "Wooden Hopper");
    }

    @Override
    @NotNull
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new WoodenHopperContainer(id, player, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WoodenHopperBlockEntity entity) {
        BaseHopperBlockEntity.baseTick(level, pos, state, entity);
    }
}