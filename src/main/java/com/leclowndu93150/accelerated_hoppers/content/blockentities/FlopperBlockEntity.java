package com.leclowndu93150.accelerated_hoppers.content.blockentities;

import com.leclowndu93150.accelerated_hoppers.Config;
import com.leclowndu93150.accelerated_hoppers.registries.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FlopperBlockEntity extends BlockEntity {
    private FluidTank tank;
    private int totalDrainedFromWorld = 0;
    private ItemStackHandler inventory = new ItemStackHandler(0) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    public FluidTank getTank() {
        return tank;
    }

    public FlopperBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.FLOPPER_BLOCK_ENTITY_TYPE.get(), pos, state);

        tank = new FluidTank(Config.flopperCapacity) {
            @Override
            protected void onContentsChanged() {
                if (!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                };
                setChanged();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.tank.readFromNBT(provider, nbt);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.merge(this.tank.writeToNBT(provider, new CompoundTag()));

    }

    public static void tick(Level level, BlockPos pos, BlockState state, FlopperBlockEntity entity) {
        if (level != null && !level.isClientSide) {
            entity.updateHopper(entity::pullFluids);
            System.out.println("FlopperBlockEntity tick");
        }
    }

    public static List<Entity> getAllAliveEntitiesAt(Level level, double x, double y, double z, Predicate<? super Entity> filter) {
        return level.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                entity -> entity.isAlive() && filter.test(entity));
    }

    protected Optional<Pair<? extends IFluidHandler, Object>> getFluidHandler(Level level, double x, double y, double z, final Direction side) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(blockpos);
        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity != null) {
                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, blockpos, state, blockEntity, side);
                if (handler != null) {
                    return Optional.of(ImmutablePair.of(handler, blockEntity));
                }
            }
        }
        // Get entities with item handlers
        List<Entity> list = getAllAliveEntitiesAt(level, x, y, z,
                entity -> entity instanceof Container || !(entity instanceof LivingEntity) && entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side) != null);
        if (!list.isEmpty()) {
            Entity entity = list.get(level.random.nextInt(list.size()));
            IFluidHandler cap = entity.getCapability(Capabilities.FluidHandler.ENTITY, side);
            if (cap != null) {
                return Optional.of(ImmutablePair.of(cap, entity));
            }
        }
        return Optional.empty();
    }

    protected boolean isNotFull(Object fluidHandler) {
        IFluidHandler handler = (IFluidHandler) fluidHandler;
        for (int tank = 0; tank < handler.getTanks(); tank++) {
            FluidStack fluidInTank = handler.getFluidInTank(tank);
            if (fluidInTank.isEmpty() || fluidInTank.getAmount() < handler.getTankCapacity(tank)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(IFluidHandler fluidHandler) {
        IFluidHandler handler = (IFluidHandler) fluidHandler;
        for (int tank = 0; tank < handler.getTanks(); tank++) {
            FluidStack fluidInTank = handler.getFluidInTank(tank);
            if (!(fluidInTank.getAmount() == 0)) {
                return false;
            }
        }
        return false;
    }

    protected boolean pullFluidFromFluidHandler(Object fluidHandler) {
        IFluidHandler handler = (IFluidHandler) fluidHandler;
        for (int i = 0; i < handler.getTanks(); i++) {
            int toDrain = Math.min(tank.getCapacity() - tank.getFluidAmount(), Config.flopperIORate);
            FluidStack drainFluid = handler.drain(toDrain, IFluidHandler.FluidAction.SIMULATE);
            if (!drainFluid.isEmpty()) {
                FluidStack destFluid = this.tank.getFluid();
                if (destFluid.isEmpty() || (destFluid.getAmount() < this.tank.getCapacity() &&
                                FluidStack.isSameFluidSameComponents(drainFluid, destFluid))) {
                    drainFluid = handler.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                    if (destFluid.isEmpty()) {
                        this.tank.setFluid(drainFluid);
                    } else {
                        destFluid.grow(drainFluid.getAmount());
                    }
                    this.setChanged();
                    return true;
                }
            }
        }
        return false;
    }

    protected void updateHopper(Supplier<Boolean> supplier) {
        if (this.level != null && !this.level.isClientSide) {
            if (this.getBlockState().getValue(HopperBlock.ENABLED)) {
                boolean flag = false;

                if (!this.tank.isEmpty()) {
                    flag = this.transferFluidOut();
                    flag |= supplier.get();
                }
                if (flag) {
                    this.setChanged();
                }
            }
        }
    }

    protected double getLevelX() {
        return this.getBlockPos().getX() + 0.5D;
    }

    protected double getLevelY() {
        return this.getBlockPos().getY() + 0.5D;
    }

    protected double getLevelZ() {
        return this.getBlockPos().getZ() + 0.5D;
    }

    private Optional<Pair<? extends IFluidHandler, Object>> getFluidHandler(FlopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getFluidHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    private FluidStack insertStack(BlockEntity source, BlockEntity destination, IFluidHandler destInventory, FluidStack stack, int index) {
        FluidStack destStack = destInventory.getFluidInTank(index);
        if (destStack.isEmpty() || FluidStack.isSameFluidSameComponents(stack, destStack)) {
            int maxAmount = Math.min(destInventory.getTankCapacity(index) - destStack.getAmount(), stack.getAmount());
            if (maxAmount > 0) {
                FluidStack copy = stack.copy();
                copy.setAmount(maxAmount);
                destInventory.fill(copy, IFluidHandler.FluidAction.EXECUTE);
                stack.shrink(maxAmount);
                return stack;
            }
        }
        return stack;
    }

    protected FluidStack putStackInInventoryAllTanks(BlockEntity source, BlockEntity destination, IFluidHandler destInventory, FluidStack stack) {
        for (int tank = 0; tank < destInventory.getTanks() && !stack.isEmpty(); tank++) {
            stack = insertStack(source, destination, destInventory, stack, tank);
        }
        return stack;
    }

    private boolean transferFluidOut() {
        Direction flopperFacing = this.getBlockState().getValue(HopperBlock.FACING);
        return getFluidHandler(this, flopperFacing)
                .map(destinationResult -> {
                    IFluidHandler fluidHandler = destinationResult.getKey();
                    BlockEntity destination = (BlockEntity) destinationResult.getValue();
                    if (isNotFull(fluidHandler)) {
                        if (!this.tank.isEmpty()) {
                            // Copy the tank contents to avoid modifying em
                            FluidStack originalTankContents = this.tank.getFluid().copy();

                            // Find out the quanity of fluid to transfer
                            FluidStack insertStack = originalTankContents.copy();
                            insertStack.setAmount(Math.min(this.tank.getFluidAmount(), Config.flopperIORate));

                            // Insert the fluid into the destination and get the remainder if any
                            FluidStack remainder = putStackInInventoryAllTanks(this, destination, fluidHandler, insertStack);
                            if (remainder.isEmpty()) {
                                return true;
                            }

                            // If there is a remainder, shrink the original tank contents by the amount that was successfully transferred
                            originalTankContents.shrink(remainder.getAmount());
                            this.tank.setFluid(originalTankContents);
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    protected boolean pullFluids() {
        return getFluidHandler(this, Direction.UP)
            .map(fluidHandlerResult -> {
                    return pullFluidFromFluidHandler(fluidHandlerResult.getKey());
            }).orElseGet(() -> {
                BlockPos pos = BlockPos.containing(this.getLevelX(), this.getLevelY() + 1D, this.getLevelZ());
                BlockState aboveBlockState = this.level.getBlockState(pos);
                if (aboveBlockState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS) || !aboveBlockState.isCollisionShapeFullBlock(this.level, pos)) {
                    if (level.getBlockState(pos).getBlock() instanceof LiquidBlock liquidBlock) {
                        FluidStack fluid = new FluidStack(liquidBlock.fluid, Math.min(Config.flopperIORate, 1000));
                        if (tank.isEmpty() || FluidStack.isSameFluidSameComponents(tank.getFluid(), fluid)) {
                            int filled = tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                            totalDrainedFromWorld += filled;
                            if (totalDrainedFromWorld >= 1000) {
                                totalDrainedFromWorld -= 1000;
                                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
                                level.playLocalSound(this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                            }
                            return true;
                        }
                    }
                }
                return false;
            });
    }
}