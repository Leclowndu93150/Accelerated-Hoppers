package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import com.leclowndu93150.advanced_hoppers.transfer.TransferTransaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BaseHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
    protected int transferCooldown;
    protected final int baseCooldown;
    protected long tickedGameTime;
    protected NonNullList<ItemStack> items;
    protected final String hopperName;

    public BaseHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots, int baseCooldown, String hopperName) {
        super(type, pos, state);
        this.items = NonNullList.withSize(slots, ItemStack.EMPTY);
        this.baseCooldown = baseCooldown;
        this.transferCooldown = baseCooldown;
        this.hopperName = hopperName;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.transferCooldown = input.getIntOr("TransferCooldown", 0);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(input)) {
            ContainerHelper.loadAllItems(input, this.items);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("TransferCooldown", this.transferCooldown);
        if (!this.trySaveLootTable(output)) {
            ContainerHelper.saveAllItems(output, this.items);
        }
    }

    @Override
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
        return this.items.size();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        this.unpackLootTable(null);
        ItemStack stack = ContainerHelper.removeItem(this.getItems(), index, count);
        this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        this.unpackLootTable(null);
        ItemStack stack = ContainerHelper.takeItem(this.getItems(), index);
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.unpackLootTable(null);
        this.getItems().set(index, stack);
        stack.limitSize(this.getMaxStackSize(stack));
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

    protected long getLastUpdateTime() {
        return this.tickedGameTime;
    }

    public static List<Entity> getAllAliveEntitiesAt(Level level, double x, double y, double z, Predicate<? super Entity> filtered) {
        return level.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                entity -> entity.isAlive() && filtered.test(entity));
    }

    public static void baseTick(Level level, BlockPos pos, BlockState state, BaseHopperBlockEntity entity) {
        if (level != null && !level.isClientSide()) {
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

    protected ItemStack insertIntoSelf(ItemStack stack) {
        boolean inventoryWasEmpty = this.isEmpty();
        int originalCount = stack.getCount();
        for (int slot = 0; slot < this.getContainerSize() && !stack.isEmpty(); slot++) {
            ItemStack current = this.getItem(slot);
            int limit = Math.min(this.getMaxStackSize(), stack.getMaxStackSize());
            if (current.isEmpty()) {
                this.setItem(slot, stack.split(Math.min(limit, stack.getCount())));
            } else if (current.getCount() < limit && ItemStack.isSameItemSameComponents(current, stack)) {
                int moved = Math.min(limit - current.getCount(), stack.getCount());
                current.grow(moved);
                stack.shrink(moved);
                this.setChanged();
            }
        }
        if (stack.getCount() < originalCount) {
            updateCooldown(inventoryWasEmpty, null, this);
        }
        return stack;
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
            Entity entity = list.get(level.getRandom().nextInt(list.size()));
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
        for (int slot = 0; slot < itemHandler.size(); slot++) {
            ItemStack stackInSlot = itemHandler.getStack(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getCapacity(slot, stackInSlot)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(ItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.size(); slot++) {
            if (!itemHandler.getStack(slot).isEmpty()) {
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
        for (int i = 0; i < handler.size(); i++) {
            ItemStack resource = handler.getStack(i);
            if (resource.isEmpty()) {
                continue;
            }
            int slot = findSlotFor(resource.copyWithCount(1));
            if (slot < 0) {
                continue;
            }
            ItemStack destStack = this.getItem(slot);
            int limit = Math.min(this.getMaxStackSize(), resource.getMaxStackSize());
            int wanted = Math.min(Math.min(transferAmount(), resource.getCount()), limit - destStack.getCount());
            int moved;
            try (TransferTransaction transaction = Services.PLATFORM.openTransaction()) {
                moved = handler.extract(i, resource, Math.max(wanted, 1), transaction);
                if (moved > 0) {
                    transaction.commit();
                }
            }
            if (moved > 0) {
                if (destStack.isEmpty()) {
                    this.setItem(slot, resource.copyWithCount(moved));
                } else {
                    destStack.grow(moved);
                    this.setItem(slot, destStack);
                }
                this.setChanged();
                return true;
            }
        }
        return false;
    }

    protected void updateCooldown(boolean inventoryWasEmpty, @Nullable BlockEntity source, Object destination) {
        if (inventoryWasEmpty && destination instanceof BaseHopperBlockEntity destinationHopper && destinationHopper.mayNotTransfer()) {
            int k = 0;
            if (source instanceof BaseHopperBlockEntity sourceHopper && destinationHopper.getLastUpdateTime() >= sourceHopper.getLastUpdateTime()) {
                k = 1;
            }
            destinationHopper.setTransferCooldown(destinationHopper.getBaseCooldown() - k);
        }
    }

    protected boolean canTransfer() {
        return this.getBlockState().getValue(HopperBlock.ENABLED);
    }

    protected void updateHopper(Supplier<Boolean> pullAction) {
        if (this.level != null && !this.level.isClientSide()) {
            if (this.isNotOnTransferCooldown() && canTransfer()) {
                boolean flag = false;
                if (!this.isEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (isNotFull()) {
                    flag |= pullAction.get();
                }
                if (flag) {
                    this.setTransferCooldown(getBaseCooldown());
                    this.setChanged();
                }
            }
        }
    }

    protected boolean isNotFull() {
        for (ItemStack stack : this.items) {
            if (stack.isEmpty() || stack.getCount() < Math.min(this.getMaxStackSize(), stack.getMaxStackSize())) {
                return true;
            }
        }
        return false;
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
        if (isNotFull(itemHandler)) {
            for (int i = 0; i < this.getContainerSize(); ++i) {
                ItemStack stack = this.getItem(i);
                if (stack.isEmpty()) {
                    continue;
                }
                boolean inventoryWasEmpty = isEmpty(itemHandler);
                int moved;
                try (TransferTransaction transaction = Services.PLATFORM.openTransaction()) {
                    moved = itemHandler.insert(stack, Math.min(stack.getCount(), maxAmount), transaction);
                    if (moved > 0) {
                        transaction.commit();
                    }
                }
                if (moved > 0) {
                    this.removeItem(i, moved);
                    updateCooldown(inventoryWasEmpty, this, destination);
                    return moved;
                }
            }
        }
        return 0;
    }

    protected boolean captureItem(ItemEntity itemEntity) {
        ItemStack remainder = insertIntoSelf(itemEntity.getItem().copy());
        if (remainder.isEmpty()) {
            itemEntity.remove(Entity.RemovalReason.DISCARDED);
            return true;
        }
        itemEntity.setItem(remainder);
        return false;
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
