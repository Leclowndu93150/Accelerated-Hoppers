package com.leclowndu93150.accelerated_hoppers.content.blocks;

import com.leclowndu93150.accelerated_hoppers.Config;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.content.blockentities.IronHopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.registries.Registry;
import com.leclowndu93150.accelerated_hoppers.utils.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
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

    private void bucketFillSound(Level worldIn, BlockPos pos) {
        worldIn.playLocalSound(pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    private void bucketEmptySound(Level worldIn, BlockPos pos) {
        worldIn.playLocalSound(pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            ItemStack stack = player.getItemInHand(player.getUsedItemHand());
            if (blockEntity instanceof FlopperBlockEntity) {
                player.awardStat(Stats.INSPECT_HOPPER);
                IFluidHandler handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

                // Buckets
                if (((FlopperBlockEntity) blockEntity).getTank().getCapacity() >= 1000) {
                    if (stack.getItem() instanceof BucketItem && ((BucketItem) stack.getItem()).content != Fluids.EMPTY) {
                        FluidTank tank = ((FlopperBlockEntity) blockEntity).getTank();
                        if (tank.getFluid().isEmpty()) {
                            FluidStack fluidStack = new FluidStack(((BucketItem) stack.getItem()).content, 1000);
                            tank.setFluid(fluidStack);
                            stack.shrink(1);
                            System.out.println("set fluid in tank - stack amount: " + stack.getCount());
                            bucketEmptySound(worldIn, pos);
                            if (stack.isEmpty()) {
                                System.out.println("empty stack");
                                player.setItemInHand(player.getUsedItemHand(), new ItemStack(Items.BUCKET));
                            } else {
                                player.addItem(new ItemStack(Items.BUCKET));
                            }
                        } else if (tank.getFluid().getFluid().equals(((BucketItem) stack.getItem()).content) && tank.getCapacity() - tank.getFluidAmount() >= 1000) {
                            tank.fill(new FluidStack(((BucketItem) stack.getItem()).content, 1000), IFluidHandler.FluidAction.EXECUTE);
                            stack.shrink(1);
                            System.out.println("fillin tank - stack amount: " + stack.getCount());
                            bucketEmptySound(worldIn, pos);
                            if (stack.isEmpty()) {
                                System.out.println("empty stack");
                                player.setItemInHand(player.getUsedItemHand(), new ItemStack(Items.BUCKET));
                            } else {
                                player.addItem(new ItemStack(Items.BUCKET));
                            }
                        }
                        return InteractionResult.CONSUME;
                    }
                    if (stack.getItem() instanceof BucketItem && ((BucketItem) stack.getItem()).content == Fluids.EMPTY) {
                        FluidTank tank = ((FlopperBlockEntity) blockEntity).getTank();
                        if (tank.getFluid().getAmount() >= 1000) {
                            FluidStack fluidStack = tank.getFluid();
                            Fluid tankFluid = fluidStack.copy().getFluid(); // JIC it's literally 1000mb and it gets emptied and there won't be a getBucket()

                            fluidStack.shrink(1000);

                            stack.shrink(1);

                            if (stack.isEmpty()) {
                                player.setItemInHand(player.getUsedItemHand(), new ItemStack(tankFluid.getBucket()));
                                worldIn.playLocalSound(blockEntity.getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                            } else {
                                player.addItem(new ItemStack(fluidStack.getFluid().getBucket()));
                            }
                        }
                        return InteractionResult.CONSUME;
                    }
                }

                // Anything else
                if (handler != null) {
                    for (int tankIndex = 0; tankIndex < handler.getTanks(); tankIndex++) {
                        FluidStack fluidStack = handler.getFluidInTank(tankIndex);
                        if (fluidStack.isEmpty()) {
                            continue;
                        }
                        FluidTank tank = ((FlopperBlockEntity) blockEntity).getTank();
                        if (tank.getFluid().isEmpty()) {
                            FluidStack toFill = new FluidStack(fluidStack.getFluid(), Math.min(Config.flopperCapacity, fluidStack.getAmount()));
                            tank.setFluid(toFill);
                            System.out.println("shrinking tank contents: " + toFill.getAmount());

                            handler.getFluidInTank(tankIndex).shrink(toFill.getAmount());
                        } else if (FluidStack.isSameFluidSameComponents(tank.getFluid(), fluidStack)) {
                            int amount = tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                            System.out.println("shrinking tank contents: " + amount);

                            handler.getFluidInTank(tankIndex).shrink(amount);
                        }
                    }
                    return InteractionResult.CONSUME;
                }
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
