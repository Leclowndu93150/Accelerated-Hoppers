package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import com.leclowndu93150.advanced_hoppers.platform.services.IPlatformHelper;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public ItemHandler getBlockItemHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side) {
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, blockEntity, side);
        return handler != null ? new NeoForgeItemHandler(handler) : null;
    }

    @Override
    public ItemHandler getEntityItemHandler(Entity entity, Direction side) {
        IItemHandler handler = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side);
        return handler != null ? new NeoForgeItemHandler(handler) : null;
    }

    @Override
    public ItemHandler wrapContainer(Container container) {
        return new NeoForgeItemHandler(new InvWrapper(container));
    }

    @Override
    public ItemHandler wrapSidedContainer(WorldlyContainer container, Direction side) {
        return new NeoForgeItemHandler(new SidedInvWrapper(container, side));
    }

    @Override
    public FluidHandler getBlockFluidHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side) {
        IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, blockEntity, side);
        return handler != null ? new NeoForgeFluidHandler(handler) : null;
    }

    @Override
    public FluidHandler getEntityFluidHandler(Entity entity, Direction side) {
        IFluidHandler handler = entity.getCapability(Capabilities.FluidHandler.ENTITY, side);
        return handler != null ? new NeoForgeFluidHandler(handler) : null;
    }

    @Override
    public FluidHandler getHeldItemFluidHandler(Player player, InteractionHand hand) {
        IFluidHandlerItem handler = player.getItemInHand(hand).getCapability(Capabilities.FluidHandler.ITEM);
        return handler != null ? new NeoForgeItemFluidHandler(handler, player, hand) : null;
    }

    @Override
    public FluidVolume getContainedFluid(ItemStack stack) {
        return FluidUtil.getFluidContained(stack).map(NeoForgeFluidHandler::toCommon).orElse(FluidVolume.EMPTY);
    }

    @Override
    public Component getFluidName(FluidVolume fluid) {
        return NeoForgeFluidHandler.toNeoForge(fluid).getHoverName();
    }
}
