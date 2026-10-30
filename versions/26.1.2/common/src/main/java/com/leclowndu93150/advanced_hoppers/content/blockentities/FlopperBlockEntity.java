package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.FluidHandler;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import com.leclowndu93150.advanced_hoppers.transfer.SimpleFluidTank;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FlopperBlockEntity extends BlockEntity {
    protected final SimpleFluidTank tank;
    protected final IntSupplier ioRate;
    private int totalDrainedFromWorld;

    public SimpleFluidTank getTank() {
        return tank;
    }

    public FlopperBlockEntity(BlockPos pos, BlockState state) {
        this(AHRegistry.FLOPPER_BLOCK_ENTITY_TYPE, pos, state, Config.flopperCapacity, () -> Config.flopperIORate);
    }

    public FlopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, IntSupplier ioRate) {
        super(type, pos, state);
        this.ioRate = ioRate;
        totalDrainedFromWorld = 0;
        tank = new SimpleFluidTank(capacity, () -> {
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            setChanged();
        });
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.tank.load(input);
        this.totalDrainedFromWorld = input.getIntOr("totalDrainedFromWorld", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.tank.save(output);
        output.putInt("totalDrainedFromWorld", totalDrainedFromWorld);
    }

    public boolean accepts(FluidVolume fluid) {
        return true;
    }

    public int getIoRate() {
        return ioRate.getAsInt();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FlopperBlockEntity entity) {
        if (level != null && !level.isClientSide()) {
            entity.updateHopper(entity::pullFluids);
        }
    }

    public static List<Entity> getAllAliveEntitiesAt(Level level, double x, double y, double z, Predicate<? super Entity> filtered) {
        return level.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                entity -> entity.isAlive() && filtered.test(entity));
    }

    protected Optional<Pair<FluidHandler, Object>> getFluidHandler(Level level, double x, double y, double z, final Direction side) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(blockpos);
        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity != null) {
                FluidHandler handler = Services.PLATFORM.getBlockFluidHandler(level, blockpos, state, blockEntity, side);
                if (handler != null) {
                    return Optional.of(ImmutablePair.of(handler, blockEntity));
                }
            }
        }
        List<Entity> list = getAllAliveEntitiesAt(level, x, y, z,
                entity -> !(entity instanceof LivingEntity) && Services.PLATFORM.getEntityFluidHandler(entity, side) != null);
        if (!list.isEmpty()) {
            Entity entity = list.get(level.getRandom().nextInt(list.size()));
            FluidHandler cap = Services.PLATFORM.getEntityFluidHandler(entity, side);
            if (cap != null) {
                return Optional.of(ImmutablePair.of(cap, entity));
            }
        }
        return Optional.empty();
    }

    protected boolean isNotFull(FluidHandler handler) {
        for (int index = 0; index < handler.size(); index++) {
            FluidVolume fluidInTank = handler.getFluid(index);
            if (fluidInTank.isEmpty() || fluidInTank.getAmount() < handler.getCapacity(index, fluidInTank)) {
                return true;
            }
        }
        return false;
    }

    protected boolean pullFluidFromFluidHandler(FluidHandler handler) {
        int toDrain = Math.min(tank.getCapacity() - tank.getFluidAmount(), ioRate.getAsInt());
        if (toDrain <= 0) {
            return false;
        }
        for (int i = 0; i < handler.size(); i++) {
            FluidVolume resource = handler.getFluid(i);
            if (resource.isEmpty() || !accepts(resource)) {
                continue;
            }
            FluidVolume destFluid = this.tank.getFluid();
            if (!destFluid.isEmpty() && !FluidVolume.isSameFluidSameComponents(resource, destFluid)) {
                continue;
            }
            int extracted;
            try (TransferTransaction transaction = Services.PLATFORM.openTransaction()) {
                extracted = handler.extract(resource, toDrain, transaction);
                transaction.commit();
            }
            if (extracted > 0) {
                this.tank.fill(resource.copyWithAmount(extracted), false);
                this.setChanged();
                return true;
            }
        }
        return false;
    }

    protected void updateHopper(Supplier<Boolean> supplier) {
        if (this.level != null && !this.level.isClientSide()) {
            if (this.getBlockState().getValue(HopperBlock.ENABLED)) {
                boolean flag = supplier.get();

                if (!this.tank.isEmpty()) {
                    flag |= this.transferFluidOut();
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

    protected Optional<Pair<FluidHandler, Object>> getFluidHandler(FlopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getFluidHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    protected boolean transferFluidOut() {
        Direction flopperFacing = this.getBlockState().getValue(HopperBlock.FACING);
        return getFluidHandler(this, flopperFacing)
                .map(destinationResult -> {
                    FluidHandler fluidHandler = destinationResult.getKey();
                    if (isNotFull(fluidHandler) && !this.tank.isEmpty()) {
                        FluidVolume resource = this.tank.getFluid().copy();
                        int insertAmount = Math.min(this.tank.getFluidAmount(), ioRate.getAsInt());
                        int inserted;
                        try (TransferTransaction transaction = Services.PLATFORM.openTransaction()) {
                            inserted = fluidHandler.insert(resource, insertAmount, transaction);
                            transaction.commit();
                        }
                        if (inserted > 0) {
                            this.tank.drain(inserted, false);
                        }
                        return inserted == insertAmount;
                    }
                    return false;
                })
                .orElse(false);
    }

    protected boolean pullFluids() {
        return getFluidHandler(this, Direction.UP)
                .map(fluidHandlerResult -> pullFluidFromFluidHandler(fluidHandlerResult.getKey()))
                .orElseGet(() -> {
                    BlockPos pos = BlockPos.containing(this.getLevelX(), this.getLevelY() + 1D, this.getLevelZ());
                    BlockState aboveBlockState = this.level.getBlockState(pos);
                    if (aboveBlockState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS) || !aboveBlockState.isCollisionShapeFullBlock(this.level, pos)) {
                        if (aboveBlockState.getBlock() instanceof LiquidBlock) {
                            FluidState fluidState = aboveBlockState.getFluidState();
                            if (fluidState.isSource() && fluidState.getAmount() == 8) {
                                FluidVolume fluid = new FluidVolume(fluidState.getType(), Math.min(ioRate.getAsInt(), 1000));
                                if (accepts(fluid) && (tank.isEmpty() || FluidVolume.isSameFluidSameComponents(tank.getFluid(), fluid))) {
                                    int filled = tank.fill(fluid, false);
                                    this.totalDrainedFromWorld += filled;
                                    if (this.totalDrainedFromWorld >= 1000) {
                                        this.totalDrainedFromWorld -= 1000;
                                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                                        level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                });
    }
}
