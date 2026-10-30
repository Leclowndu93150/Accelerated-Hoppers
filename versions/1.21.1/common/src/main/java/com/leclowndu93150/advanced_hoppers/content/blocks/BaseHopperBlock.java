package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.VoidHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.XpHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.items.HopperLinkerItem;
import com.leclowndu93150.advanced_hoppers.utils.TooltipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public abstract class BaseHopperBlock<T extends BlockEntity> extends HopperBlock {
    protected final Supplier<BlockEntityType<T>> blockEntityType;

    public BaseHopperBlock(Properties properties, Supplier<BlockEntityType<T>> blockEntityType) {
        super(properties);
        this.blockEntityType = blockEntityType;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityType.get().create(pos, state);
    }

    @Nullable
    @Override
    public abstract <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(Level level, BlockState state, BlockEntityType<BE> blockEntityType);

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof HopperLinkerItem) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider);
                player.awardStat(Stats.INSPECT_HOPPER);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof Container container) {
                Containers.dropContents(worldIn, pos, container);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof ExperienceOrb orb && !level.isClientSide && level.getBlockEntity(pos) instanceof XpHopperBlockEntity xpHopper) {
            xpHopper.absorb(orb);
            return;
        }
        if (entity instanceof ItemEntity itemEntity && !itemEntity.getItem().isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof Hopper) {
                AABB hopperBounds = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                if (itemEntity.getBoundingBox().intersects(hopperBounds)) {
                    if (blockEntity instanceof BaseHopperBlockEntity baseHopper) {
                        baseHopper.onItemEntityIsCaptured(itemEntity);
                    } else if (blockEntity instanceof FilteredHopperBlockEntity filteredHopper) {
                        filteredHopper.onItemEntityIsCaptured(itemEntity);
                    } else if (blockEntity instanceof VoidHopperBlockEntity voidHopper) {
                        voidHopper.onItemEntityIsCaptured(itemEntity);
                    }
                }
            }
        }
    }

    public abstract int getTransferCooldown();

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        TooltipUtil.addTooltip(tooltipComponents, getTransferCooldown());
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
