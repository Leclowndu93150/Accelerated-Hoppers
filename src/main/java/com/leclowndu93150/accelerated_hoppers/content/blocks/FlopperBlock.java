package com.leclowndu93150.accelerated_hoppers.content.blocks;

import com.leclowndu93150.accelerated_hoppers.Config;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.IronHopperBlockEntity;
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
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlopperBlock extends HopperBlock {
    public FlopperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Registry.FLOPPER_BLOCK_ENTITY_TYPE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, Registry.FLOPPER_BLOCK_ENTITY_TYPE.get(), FlopperBlockEntity::tick);
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof FlopperBlockEntity) {
                player.awardStat(Stats.INSPECT_HOPPER);
                FluidTank tank = ((FlopperBlockEntity) blockEntity).getTank();
                int ammount = tank.getFluidAmount();
                String fluidName = tank.getFluid().getHoverName().getString();
                player.displayClientMessage(
                        Component.literal(ammount + "mb ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(
                                Component.literal("of ")
                                        .withStyle(ChatFormatting.YELLOW)
                        ).append(
                                Component.literal(fluidName)
                                        .withStyle(ChatFormatting.GRAY)
                        ), true);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof FlopperBlockEntity) {
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.literal("Speed: ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(Config.flopperIORate + "mb/t").withStyle(ChatFormatting.YELLOW))
        );
        tooltipComponents.add(
                Component.literal("Capacity: ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(Config.flopperCapacity + "mb").withStyle(ChatFormatting.YELLOW))
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
