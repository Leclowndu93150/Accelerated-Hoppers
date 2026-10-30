package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import com.leclowndu93150.advanced_hoppers.transfer.SimpleItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BaseHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
    protected int transferCooldown;
    protected final int baseCooldown;
    protected long tickedGameTime;
    protected SimpleItemHandler inventory;
    protected final String hopperName;

    public BaseHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots, int baseCooldown, String hopperName) {
        super(type, pos, state);
        this.inventory = new SimpleItemHandler(slots);
        this.baseCooldown = baseCooldown;
        this.transferCooldown = baseCooldown;
        this.hopperName = hopperName;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.transferCooldown = nbt.getInt("TransferCooldown");
        this.inventory = new SimpleItemHandler(getContainerSize());
        if (!this.tryLoadLootTable(nbt)) {
            this.inventory.deserializeNBT(provider, nbt);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putInt("TransferCooldown", this.transferCooldown);
        if (!this.trySaveLootTable(compound)) {
            compound.merge(this.inventory.serializeNBT(provider));
        }
    }

    @Override
    @NotNull
    protected Component getDefaultName() {
        return Component.literal(hopperName);
    }

    @Override
    public double getLevelX() {
        return (double) this.worldPosition.getX() + 0.5D;
    }

    @Override
    public double getLevelY() {
        return (double) this.worldPosition.getY() + 0.5D;
    }

    @Override
    public double getLevelZ() {
        return (double) this.worldPosition.getZ() + 0.5D;
    }

    @Override
    public boolean isGridAligned() {
        return true;
    }

    @Override
    protected abstract AbstractContainerMenu createMenu(int id, Inventory player);

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < getContainerSize(); i++) {
            items.set(i, this.inventory.getStackInSlot(i));
        }
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemsIn) {
        for (int i = 0; i < Math.min(itemsIn.size(), getContainerSize()); i++) {
            this.inventory.setStackInSlot(i, itemsIn.get(i));
        }
    }

    @Override
    @NotNull
    public ItemStack removeItem(int index, int count) {
        this.unpackLootTable(null);
        ItemStack stack = this.inventory.extractItem(index, count, false);
        this.setChanged();
        return stack;
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int index) {
        this.unpackLootTable(null);
        ItemStack stack = this.inventory.getStackInSlot(index);
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        this.unpackLootTable(null);
        this.inventory.setStackInSlot(index, stack);
        this.setChanged();
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    protected boolean isNotOnTransferCooldown() {
        return this.transferCooldown <= 0;
    }

    public boolean mayNotTransfer() {
        return this.transferCooldown <= getBaseCooldown();
    }

    public int getBaseCooldown() {
        return baseCooldown;
    }

    protected int transferAmount() {
        return 1;
    }

    protected Direction pullDirection() {
        return Direction.UP;
    }

    protected List<Direction> outputDirections() {
        return List.of(this.getBlockState().getValue(HopperBlock.FACING));
    }

    protected void onPushed(Direction direction) {
    }

    protected boolean canTransfer() {
        return this.getBlockState().getValue(HopperBlock.ENABLED);
    }

    protected long getLastUpdateTime() {
        return this.tickedGameTime;
    }

    public static List<Entity> getAllAliveEntitiesAt(Level level, double x, double y, double z, Predicate<? super Entity> filtered) {
        return level.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                entity -> entity.isAlive() && filtered.test(entity));
    }

    public static void baseTick(Level level, BlockPos pos, BlockState state, BaseHopperBlockEntity entity) {
        if (level != null && !level.isClientSide) {
            entity.transferCooldown--;
            entity.tickedGameTime = level.getGameTime();
            if (entity.isNotOnTransferCooldown()) {
                entity.setTransferCooldown(0);
                entity.updateHopper(entity::pullItems);
            }
        }
    }

    public void onItemEntityIsCaptured(ItemEntity itemEntity) {
        this.updateHopper(() -> captureItem(itemEntity));
    }

    protected ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, ItemHandler destInventory, ItemStack stack) {
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    private ItemStack insertStack(BlockEntity source, Object destination, ItemHandler destInventory, ItemStack stack, int slot) {
        ItemStack result = stack;
        if (!destInventory.insertItem(slot, stack, true).equals(stack)) {
            boolean inventoryWasEmpty = isEmpty(destInventory);
            result = destInventory.insertItem(slot, stack, false);
            if (result.getCount() < stack.getCount()) {
                updateCooldown(inventoryWasEmpty, source, destination);
            }
        }
        return result;
    }

    protected Optional<Pair<ItemHandler, Object>> getItemHandler(Level level, double x, double y, double z, final Direction side) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(blockpos);
        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity != null) {
                ItemHandler handler = Services.PLATFORM.getBlockItemHandler(level, blockpos, state, blockEntity, side);
                if (handler != null) {
                    return Optional.of(ImmutablePair.of(handler, blockEntity));
                }
            }
            if (blockEntity instanceof WorldlyContainer container) {
                return Optional.of(ImmutablePair.of(Services.PLATFORM.wrapSidedContainer(container, side), state));
            }
            if (blockEntity instanceof Container container) {
                return Optional.of(ImmutablePair.of(Services.PLATFORM.wrapContainer(container), state));
            }
        }
        Block block = state.getBlock();
        if (block instanceof WorldlyContainerHolder holder) {
            return Optional.of(ImmutablePair.of(Services.PLATFORM.wrapSidedContainer(holder.getContainer(state, level, blockpos), side), state));
        }
        List<Entity> list = getAllAliveEntitiesAt(level, x, y, z,
                entity -> entity instanceof Container || !(entity instanceof LivingEntity) && Services.PLATFORM.getEntityItemHandler(entity, side) != null);
        if (!list.isEmpty()) {
            Entity entity = list.get(level.random.nextInt(list.size()));
            ItemHandler cap = Services.PLATFORM.getEntityItemHandler(entity, side);
            if (cap != null) {
                return Optional.of(ImmutablePair.of(cap, entity));
            }
            if (entity instanceof WorldlyContainer container) {
                return Optional.of(ImmutablePair.of(Services.PLATFORM.wrapSidedContainer(container, side), entity));
            }
            if (entity instanceof Container containerEntity) {
                return Optional.of(ImmutablePair.of(Services.PLATFORM.wrapContainer(containerEntity), entity));
            }
        }
        return Optional.empty();
    }

    protected boolean isNotFull(ItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(ItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    private int findSlotFor(ItemStack stack) {
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack destStack = this.getItem(slot);
            if (this.canPlaceItem(slot, stack) && (destStack.isEmpty() || destStack.getCount() < destStack.getMaxStackSize()
                    && destStack.getCount() < this.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack, destStack))) {
                return slot;
            }
        }
        return -1;
    }

    protected boolean pullItemsFromItemHandler(ItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack simulated = handler.extractItem(i, transferAmount(), true);
            if (simulated.isEmpty()) {
                continue;
            }
            int slot = findSlotFor(simulated.copyWithCount(1));
            if (slot < 0) {
                continue;
            }
            ItemStack destStack = this.getItem(slot);
            int space = Math.min(this.getMaxStackSize(), simulated.getMaxStackSize()) - destStack.getCount();
            ItemStack extracted = handler.extractItem(i, Math.min(space, simulated.getCount()), false);
            if (extracted.isEmpty()) {
                continue;
            }
            if (destStack.isEmpty()) {
                this.setItem(slot, extracted);
            } else {
                destStack.grow(extracted.getCount());
                this.setItem(slot, destStack);
            }
            this.setChanged();
            return true;
        }
        return false;
    }

    protected void updateCooldown(boolean inventoryWasEmpty, BlockEntity source, Object destination) {
        if (inventoryWasEmpty && destination instanceof BaseHopperBlockEntity destinationHopper && destinationHopper.mayNotTransfer()) {
            int k = 0;
            if (source instanceof BaseHopperBlockEntity sourceHopper && destinationHopper.getLastUpdateTime() >= sourceHopper.getLastUpdateTime()) {
                k = 1;
            }
            destinationHopper.setTransferCooldown(destinationHopper.getBaseCooldown() - k);
        }
    }

    protected void updateHopper(Supplier<Boolean> pullAction) {
        if (this.level != null && !this.level.isClientSide) {
            if (this.isNotOnTransferCooldown() && canTransfer()) {
                boolean flag = false;
                if (!this.isEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (isNotFull(this.inventory)) {
                    flag |= pullAction.get();
                }
                if (flag) {
                    this.setTransferCooldown(getBaseCooldown());
                    this.setChanged();
                }
            }
        }
    }

    protected Optional<Pair<ItemHandler, Object>> getItemHandler(BaseHopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    protected boolean transferItemsOut() {
        for (Direction direction : outputDirections()) {
            if (getItemHandler(this, direction).map(this::pushInto).orElse(false)) {
                onPushed(direction);
                return true;
            }
        }
        return false;
    }

    protected boolean pushInto(Pair<ItemHandler, Object> destinationResult) {
        return pushAmount(destinationResult, transferAmount()) > 0;
    }

    protected int pushAmount(Pair<ItemHandler, Object> destinationResult, int maxAmount) {
        ItemHandler itemHandler = destinationResult.getKey();
        Object destination = destinationResult.getValue();
        if (!isNotFull(itemHandler)) {
            return 0;
        }
        for (int i = 0; i < this.getContainerSize(); ++i) {
            if (this.getItem(i).isEmpty()) {
                continue;
            }
            ItemStack original = this.getItem(i).copy();
            ItemStack taken = this.removeItem(i, Math.min(original.getCount(), maxAmount));
            int takenCount = taken.getCount();
            ItemStack remainder = putStackInInventoryAllSlots(this, destination, itemHandler, taken);
            int moved = takenCount - remainder.getCount();
            if (moved == 0) {
                this.setItem(i, original);
                continue;
            }
            if (!remainder.isEmpty()) {
                original.shrink(moved);
                this.setItem(i, original);
            }
            return moved;
        }
        return 0;
    }

    protected ItemStack insertIntoSelf(ItemStack stack) {
        return putStackInInventoryAllSlots(null, this, this.inventory, stack);
    }

    protected boolean captureItem(ItemEntity itemEntity) {
        boolean flag = false;
        ItemStack remainder = insertIntoSelf(itemEntity.getItem().copy());
        if (remainder.isEmpty()) {
            flag = true;
            itemEntity.remove(Entity.RemovalReason.DISCARDED);
        } else {
            itemEntity.setItem(remainder);
        }
        return flag;
    }

    protected boolean pullItems() {
        return getItemHandler(this, pullDirection())
                .map(itemHandlerResult -> pullItemsFromItemHandler(itemHandlerResult.getKey()))
                .orElseGet(() -> {
                    if (pullDirection() != Direction.UP) {
                        return false;
                    }
                    BlockPos pos = BlockPos.containing(this.getLevelX(), this.getLevelY() + 1D, this.getLevelZ());
                    BlockState aboveBlockState = this.level.getBlockState(pos);
                    if (aboveBlockState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS) || !aboveBlockState.isCollisionShapeFullBlock(this.level, pos)) {
                        for (ItemEntity itementity : HopperBlockEntity.getItemsAtAndAbove(this.level, this)) {
                            if (this.captureItem(itementity)) {
                                return true;
                            }
                        }
                    }
                    return false;
                });
    }
}
