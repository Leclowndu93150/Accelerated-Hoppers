package com.leclowndu93150.advanced_hoppers.platform.services;

import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IPlatformHelper {
    @Nullable
    ItemHandler getBlockItemHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side);

    @Nullable
    ItemHandler getEntityItemHandler(Entity entity, Direction side);

    ItemHandler wrapContainer(Container container);

    ItemHandler wrapSidedContainer(WorldlyContainer container, Direction side);

    @Nullable
    FluidHandler getBlockFluidHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side);

    @Nullable
    FluidHandler getEntityFluidHandler(Entity entity, Direction side);

    @Nullable
    FluidHandler getHeldItemFluidHandler(Player player, InteractionHand hand);

    Component getFluidName(FluidVolume fluid);

    FluidVolume getContainedFluid(ItemStack stack);
}
