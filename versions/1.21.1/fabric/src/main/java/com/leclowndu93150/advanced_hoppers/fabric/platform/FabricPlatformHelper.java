package com.leclowndu93150.advanced_hoppers.fabric.platform;

import com.leclowndu93150.advanced_hoppers.platform.services.IPlatformHelper;
import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
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

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public ItemHandler getBlockItemHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, state, blockEntity, side);
        return storage != null ? new FabricItemHandler(storage) : null;
    }

    @Override
    public ItemHandler getEntityItemHandler(Entity entity, Direction side) {
        return null;
    }

    @Override
    public ItemHandler wrapContainer(Container container) {
        return new FabricItemHandler(InventoryStorage.of(container, null));
    }

    @Override
    public ItemHandler wrapSidedContainer(WorldlyContainer container, Direction side) {
        return new FabricItemHandler(InventoryStorage.of(container, side));
    }

    @Override
    public FluidHandler getBlockFluidHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction side) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, blockEntity, side);
        return storage != null ? new FabricFluidHandler(storage) : null;
    }

    @Override
    public FluidHandler getEntityFluidHandler(Entity entity, Direction side) {
        return null;
    }

    @Override
    public FluidHandler getHeldItemFluidHandler(Player player, InteractionHand hand) {
        Storage<FluidVariant> storage = ContainerItemContext.forPlayerInteraction(player, hand).find(FluidStorage.ITEM);
        return storage != null ? new FabricFluidHandler(storage) : null;
    }

    @Override
    public FluidVolume getContainedFluid(ItemStack stack) {
        Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
        if (storage != null) {
            for (StorageView<FluidVariant> view : storage) {
                if (!view.isResourceBlank()) {
                    return new FluidVolume(view.getResource().getFluid(), (int) Math.max(1, view.getAmount() / FabricFluidHandler.DROPLETS_PER_MB),
                            view.getResource().getComponents());
                }
            }
        }
        return FluidVolume.EMPTY;
    }

    @Override
    public Component getFluidName(FluidVolume fluid) {
        return FluidVariantAttributes.getName(FabricFluidHandler.toVariant(fluid));
    }
}
