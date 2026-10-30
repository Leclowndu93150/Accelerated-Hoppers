package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.SimpleFluidTank;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class FlopperBlock extends HopperBlock {
    private final Supplier<? extends BlockEntityType<? extends FlopperBlockEntity>> blockEntityType;
    private final IntSupplier capacity;
    private final IntSupplier ioRate;

    public FlopperBlock(Properties properties) {
        this(properties, () -> AHRegistry.FLOPPER_BLOCK_ENTITY_TYPE, () -> Config.flopperCapacity, () -> Config.flopperIORate);
    }

    public FlopperBlock(Properties properties, Supplier<? extends BlockEntityType<? extends FlopperBlockEntity>> blockEntityType, IntSupplier capacity, IntSupplier ioRate) {
        super(properties);
        this.blockEntityType = blockEntityType;
        this.capacity = capacity;
        this.ioRate = ioRate;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Objects.requireNonNull(blockEntityType.get().create(pos, state));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide || type != blockEntityType.get()) {
            return null;
        }
        return (tickLevel, pos, tickState, blockEntity) -> FlopperBlockEntity.tick(tickLevel, pos, tickState, (FlopperBlockEntity) blockEntity);
    }

    private void bucketFillSound(Level worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void bucketEmptySound(Level worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            InteractionHand hand = player.getUsedItemHand();
            ItemStack stack = player.getItemInHand(hand);
            if (blockEntity instanceof FlopperBlockEntity flopper) {
                player.awardStat(Stats.INSPECT_HOPPER);
                SimpleFluidTank tank = flopper.getTank();

                if (tank.getCapacity() >= 1000) {
                    if (stack.getItem() instanceof BucketItem bucket && bucket.content != Fluids.EMPTY
                            && flopper.accepts(new FluidVolume(bucket.content, 1000))) {
                        if (tank.getFluid().isEmpty()) {
                            tank.fill(new FluidVolume(bucket.content, 1000), false);
                            stack.shrink(1);
                            bucketEmptySound(worldIn, pos);
                            if (stack.isEmpty()) {
                                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                            } else {
                                player.addItem(new ItemStack(Items.BUCKET));
                            }
                        } else if (tank.getFluid().getFluid().equals(bucket.content) && tank.getCapacity() - tank.getFluidAmount() >= 1000) {
                            tank.fill(new FluidVolume(bucket.content, 1000), false);
                            stack.shrink(1);
                            bucketEmptySound(worldIn, pos);
                            if (stack.isEmpty()) {
                                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                            } else {
                                player.addItem(new ItemStack(Items.BUCKET));
                            }
                        }
                        return InteractionResult.CONSUME;
                    }
                    if (stack.getItem() instanceof BucketItem bucket && bucket.content == Fluids.EMPTY) {
                        if (tank.getFluid().getAmount() >= 1000) {
                            Fluid tankFluid = tank.getFluid().getFluid();

                            tank.drain(1000, false);

                            stack.shrink(1);

                            if (stack.isEmpty()) {
                                player.setItemInHand(hand, new ItemStack(tankFluid.getBucket()));
                                bucketFillSound(worldIn, pos);
                            } else {
                                player.addItem(new ItemStack(tankFluid.getBucket()));
                            }
                        }
                        return InteractionResult.CONSUME;
                    }
                }

                FluidHandler handler = Services.PLATFORM.getHeldItemFluidHandler(player, hand);
                if (handler != null) {
                    for (int tankIndex = 0; tankIndex < handler.getTanks(); tankIndex++) {
                        FluidVolume fluidStack = handler.getFluidInTank(tankIndex);
                        if (fluidStack.isEmpty() || !flopper.accepts(fluidStack)) {
                            continue;
                        }
                        int accepted = tank.fill(fluidStack, true);
                        if (accepted > 0) {
                            FluidVolume drained = handler.drain(fluidStack.copyWithAmount(accepted), false);
                            tank.fill(drained, false);
                        }
                    }
                    return InteractionResult.CONSUME;
                }
                if (flopper instanceof MenuProvider provider) {
                    player.openMenu(provider);
                    return InteractionResult.CONSUME;
                }
                int amount = tank.getFluidAmount();
                String fluidName = Services.PLATFORM.getFluidName(tank.getFluid()).getString();
                player.displayClientMessage(
                        Component.literal(amount + "mb ")
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
                        .append(Component.literal(ioRate.getAsInt() + "mb/t").withStyle(ChatFormatting.YELLOW))
        );
        tooltipComponents.add(
                Component.literal("Capacity: ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(capacity.getAsInt() + "mb").withStyle(ChatFormatting.YELLOW))
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
