package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import com.leclowndu93150.advanced_hoppers.platform.services.IPlatformHelper;
import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
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
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public TransferTransaction openTransaction() {
        return new NeoForgeTransaction(Transaction.openRoot());
    }

    @Override
    public ItemHandler getBlockItemHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side) {
        ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, pos, state, blockEntity, side);
        return handler != null ? new NeoForgeItemHandler(handler) : null;
    }

    @Override
    public ItemHandler getEntityItemHandler(Entity entity, Direction side) {
        ResourceHandler<ItemResource> handler = entity.getCapability(Capabilities.Item.ENTITY_AUTOMATION, side);
        return handler != null ? new NeoForgeItemHandler(handler) : null;
    }

    @Override
    public ItemHandler wrapContainer(Container container) {
        return new NeoForgeItemHandler(VanillaContainerWrapper.of(container));
    }

    @Override
    public ItemHandler wrapSidedContainer(WorldlyContainer container, Direction side) {
        return new NeoForgeItemHandler(new WorldlyContainerWrapper(container, side));
    }

    @Override
    public FluidHandler getBlockFluidHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side) {
        ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, pos, state, blockEntity, side);
        return handler != null ? new NeoForgeFluidHandler(handler) : null;
    }

    @Override
    public FluidHandler getEntityFluidHandler(Entity entity, Direction side) {
        ResourceHandler<FluidResource> handler = entity.getCapability(Capabilities.Fluid.ENTITY, side);
        return handler != null ? new NeoForgeFluidHandler(handler) : null;
    }

    @Override
    public FluidHandler getHeldItemFluidHandler(Player player, InteractionHand hand) {
        ResourceHandler<FluidResource> handler = ItemAccess.forPlayerInteraction(player, hand).getCapability(Capabilities.Fluid.ITEM);
        return handler != null ? new NeoForgeFluidHandler(handler) : null;
    }

    @Override
    public FluidVolume getContainedFluid(ItemStack stack) {
        ResourceHandler<FluidResource> handler = ItemAccess.forStack(stack.copy()).getCapability(Capabilities.Fluid.ITEM);
        if (handler != null) {
            for (int index = 0; index < handler.size(); index++) {
                FluidResource resource = handler.getResource(index);
                if (!resource.isEmpty()) {
                    return NeoForgeFluidHandler.toVolume(resource, Math.max(1, handler.getAmountAsInt(index)));
                }
            }
        }
        return FluidVolume.EMPTY;
    }

    @Override
    public Component getFluidName(FluidVolume fluid) {
        return NeoForgeFluidHandler.toResource(fluid).getHoverName();
    }
}
