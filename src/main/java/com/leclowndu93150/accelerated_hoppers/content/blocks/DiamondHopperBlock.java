package com.leclowndu93150.accelerated_hoppers.content.blocks;

import com.leclowndu93150.accelerated_hoppers.Config;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.DiamondHopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.WoodenHopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.registries.Registry;
import com.leclowndu93150.accelerated_hoppers.utils.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiamondHopperBlock extends HopperBlock {
    public DiamondHopperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Registry.DIAMOND_HOPPER_BLOCK_ENTITY_TYPE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, Registry.DIAMOND_HOPPER_BLOCK_ENTITY_TYPE.get(), DiamondHopperBlockEntity::tick);
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof DiamondHopperBlockEntity) {
                player.openMenu((DiamondHopperBlockEntity)blockEntity);
                player.awardStat(Stats.INSPECT_HOPPER);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof DiamondHopperBlockEntity) {
                Containers.dropContents(worldIn, pos, (DiamondHopperBlockEntity)blockEntity);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DiamondHopperBlockEntity diamondHopperBlockEntity
                    && !itemEntity.getItem().isEmpty() && itemEntity.getBoundingBox().move((-pos.getX()), (-pos.getY()), (-pos.getZ())).intersects(diamondHopperBlockEntity.getSuckAabb())
            ) {
                diamondHopperBlockEntity.onItemEntityIsCaptured(itemEntity);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        TooltipUtil.addTooltip(tooltipComponents, Config.diamondHopperTransferCooldown);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
