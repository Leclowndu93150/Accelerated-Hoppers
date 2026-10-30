package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.filter.ItemFilter;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilterHopperContainer;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class FilteredHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
    protected int transferCooldown = Config.filteredHopperTransferCooldown;
    protected long tickedGameTime;
    private final ItemFilter filter = new ItemFilter();
    protected boolean useSecondaryNext = false;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return filter.mode(index) ? 1 : 0;
        }

        @Override
        public void set(int index, int value) {
            filter.setMode(index, value != 0);
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public FilteredHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.FILTERED_HOPPER_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.transferCooldown = nbt.getInt("TransferCooldown");
        this.useSecondaryNext = nbt.getBoolean("UseSecondaryNext");
        this.filter.load(nbt, provider);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putInt("TransferCooldown", this.transferCooldown);
        compound.putBoolean("UseSecondaryNext", this.useSecondaryNext);
        this.filter.save(compound, provider);
    }

    @Override
    @NotNull
    protected Component getDefaultName() {
        return Component.literal("Filtered Hopper");
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
    @NotNull
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new FilterHopperContainer(id, player, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return 5;
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
        for (int i = 0; i < 5; i++) {
            items.set(i, this.filter.slots().getStackInSlot(i));
        }
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemsIn) {
        for (int i = 0; i < 5 && i < itemsIn.size(); i++) {
            ItemStack stack = itemsIn.get(i).copy();
            if (!stack.isEmpty()) {
                stack.setCount(1);
            }
            this.filter.slots().setStackInSlot(i, stack);
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return removeItemNoUpdate(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack current = getItem(index);
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack taken = current.copy();
        setItem(index, ItemStack.EMPTY);
        return taken;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        if (index < 5) {
            ItemStack filterStack = stack.copy();
            if (!filterStack.isEmpty()) {
                filterStack.setCount(1);
            }
            this.filter.slots().setStackInSlot(index, filterStack);
            this.setChanged();
        }
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        if (index < 5) {
            return this.filter.slots().getStackInSlot(index);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return false;
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    protected boolean isNotOnTransferCooldown() {
        return this.transferCooldown <= 0;
    }

    public boolean mayNotTransfer() {
        return this.transferCooldown <= Config.filteredHopperTransferCooldown;
    }

    protected long getLastUpdateTime() {
        return this.tickedGameTime;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FilteredHopperBlockEntity entity) {
        if (level != null && !level.isClientSide) {
            entity.transferCooldown--;
            entity.tickedGameTime = level.getGameTime();
            if (entity.isNotOnTransferCooldown()) {
                entity.setTransferCooldown(0);
                entity.updateHopper();
            }
        }
    }

    public void onItemEntityIsCaptured(ItemEntity itemEntity) {
        if (itemEntity.isRemoved() || itemEntity.getItem().isEmpty()) {
            return;
        }
        if (isItemAllowedByFilter(itemEntity.getItem())) {
            if (transferItemEntityToDestination(itemEntity)) {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private boolean transferItemEntityToDestination(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem().copy();
        Direction facing = this.getBlockState().getValue(HopperBlock.FACING);
        Optional<Pair<ItemHandler, Object>> facingHandler = getItemHandler(this, facing);

        if (facingHandler.isPresent()) {
            Object facingDest = facingHandler.get().getValue();
            if (facing == Direction.DOWN) {
                ItemHandler destHandler = facingHandler.get().getKey();
                ItemStack remainder = insertIntoHandlerWithCooldown(destHandler, facingDest, stack);
                if (remainder.isEmpty()) {
                    return true;
                }
                itemEntity.setItem(remainder);
                return false;
            } else {
                Optional<Pair<ItemHandler, Object>> bottomHandler = getItemHandler(this, Direction.DOWN);
                boolean hasHopperBelow = bottomHandler.isPresent() && isHopper(bottomHandler.get().getValue());

                if (hasHopperBelow) {
                    Object bottomDest = bottomHandler.get().getValue();
                    ItemHandler primaryDestHandler = useSecondaryNext ? bottomHandler.get().getKey() : facingHandler.get().getKey();
                    Object primaryDest = useSecondaryNext ? bottomDest : facingDest;
                    ItemHandler secondaryDestHandler = useSecondaryNext ? facingHandler.get().getKey() : bottomHandler.get().getKey();
                    Object secondaryDest = useSecondaryNext ? facingDest : bottomDest;

                    ItemStack remainder = insertIntoHandlerWithCooldown(primaryDestHandler, primaryDest, stack);
                    if (remainder.isEmpty()) {
                        useSecondaryNext = !useSecondaryNext;
                        return true;
                    }
                    remainder = insertIntoHandlerWithCooldown(secondaryDestHandler, secondaryDest, remainder);
                    if (remainder.isEmpty()) {
                        return true;
                    }

                    itemEntity.setItem(remainder);
                    return false;
                } else {
                    ItemHandler destHandler = facingHandler.get().getKey();
                    ItemStack remainder = insertIntoHandlerWithCooldown(destHandler, facingDest, stack);
                    if (remainder.isEmpty()) {
                        return true;
                    }
                    itemEntity.setItem(remainder);
                    return false;
                }
            }
        }

        return false;
    }

    private ItemStack insertIntoHandler(ItemHandler handler, ItemStack stack) {
        ItemStack remaining = stack.copy();
        for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
            ItemStack simResult = handler.insertItem(slot, remaining, true);
            if (!ItemStack.matches(simResult, remaining)) {
                remaining = handler.insertItem(slot, remaining, false);
            }
        }
        return remaining;
    }

    private ItemStack insertIntoHandlerWithCooldown(ItemHandler handler, Object destBlockEntity, ItemStack stack) {
        boolean wasEmpty = isHandlerEmpty(handler);
        ItemStack remaining = insertIntoHandler(handler, stack);

        if (remaining.getCount() < stack.getCount()) {
            if (destBlockEntity instanceof BaseHopperBlockEntity destHopper) {
                if (wasEmpty && destHopper.mayNotTransfer()) {
                    destHopper.setTransferCooldown(destHopper.getBaseCooldown());
                }
            } else if (destBlockEntity instanceof FilteredHopperBlockEntity destFiltered) {
                if (wasEmpty && destFiltered.mayNotTransfer()) {
                    destFiltered.setTransferCooldown(Config.filteredHopperTransferCooldown);
                }
            }
        }

        return remaining;
    }

    private boolean isHandlerEmpty(ItemHandler handler) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            if (!handler.getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    protected void updateHopper() {
        if (this.level != null && !this.level.isClientSide) {
            if (this.isNotOnTransferCooldown() && this.getBlockState().getValue(HopperBlock.ENABLED)) {
                boolean transferred = pullAndTransferItems();
                if (transferred) {
                    this.setTransferCooldown(Config.filteredHopperTransferCooldown);
                    this.setChanged();
                }
            }
        }
    }

    protected boolean pullAndTransferItems() {
        Optional<Pair<ItemHandler, Object>> aboveHandler = getItemHandler(this, Direction.UP);
        if (aboveHandler.isPresent()) {
            return pullFromHandlerAndTransfer(aboveHandler.get().getKey());
        }

        BlockPos pos = BlockPos.containing(this.getLevelX(), this.getLevelY() + 1D, this.getLevelZ());
        BlockState aboveBlockState = this.level.getBlockState(pos);
        if (aboveBlockState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS) || !aboveBlockState.isCollisionShapeFullBlock(this.level, pos)) {
            for (ItemEntity itemEntity : HopperBlockEntity.getItemsAtAndAbove(this.level, this)) {
                if (isItemAllowedByFilter(itemEntity.getItem())) {
                    if (transferItemEntityToDestination(itemEntity)) {
                        itemEntity.remove(Entity.RemovalReason.DISCARDED);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean pullFromHandlerAndTransfer(ItemHandler sourceHandler) {
        Direction facing = this.getBlockState().getValue(HopperBlock.FACING);
        Optional<Pair<ItemHandler, Object>> facingHandler = getItemHandler(this, facing);

        for (int sourceSlot = 0; sourceSlot < sourceHandler.getSlots(); sourceSlot++) {
            ItemStack stack = sourceHandler.extractItem(sourceSlot, 1, true);

            if (!stack.isEmpty() && isItemAllowedByFilter(stack)) {
                if (facing == Direction.DOWN && facingHandler.isPresent()) {
                    ItemHandler destHandler = facingHandler.get().getKey();
                    Object facingDest = facingHandler.get().getValue();
                    if (insertIntoHandlerWithCooldown(destHandler, facingDest, stack.copy()).isEmpty()) {
                        sourceHandler.extractItem(sourceSlot, 1, false);
                        return true;
                    }
                } else if (facing != Direction.DOWN && facingHandler.isPresent()) {
                    Optional<Pair<ItemHandler, Object>> bottomHandler = getItemHandler(this, Direction.DOWN);
                    boolean hasHopperBelow = bottomHandler.isPresent() && isHopper(bottomHandler.get().getValue());

                    if (hasHopperBelow) {
                        Object facingDest = facingHandler.get().getValue();
                        Object bottomDest = bottomHandler.get().getValue();
                        ItemHandler primaryDestHandler = useSecondaryNext ? bottomHandler.get().getKey() : facingHandler.get().getKey();
                        Object primaryDest = useSecondaryNext ? bottomDest : facingDest;
                        ItemHandler secondaryDestHandler = useSecondaryNext ? facingHandler.get().getKey() : bottomHandler.get().getKey();
                        Object secondaryDest = useSecondaryNext ? facingDest : bottomDest;

                        if (insertIntoHandlerWithCooldown(primaryDestHandler, primaryDest, stack.copy()).isEmpty()) {
                            sourceHandler.extractItem(sourceSlot, 1, false);
                            useSecondaryNext = !useSecondaryNext;
                            return true;
                        }
                        if (insertIntoHandlerWithCooldown(secondaryDestHandler, secondaryDest, stack.copy()).isEmpty()) {
                            sourceHandler.extractItem(sourceSlot, 1, false);
                            return true;
                        }
                    } else {
                        ItemHandler destHandler = facingHandler.get().getKey();
                        Object facingDest = facingHandler.get().getValue();
                        if (insertIntoHandlerWithCooldown(destHandler, facingDest, stack.copy()).isEmpty()) {
                            sourceHandler.extractItem(sourceSlot, 1, false);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isHopper(Object destination) {
        return destination instanceof BlockEntity blockEntity &&
                (blockEntity instanceof HopperBlockEntity ||
                        blockEntity instanceof BaseHopperBlockEntity ||
                        blockEntity instanceof FilteredHopperBlockEntity ||
                        blockEntity instanceof FlopperBlockEntity);
    }

    private boolean isItemAllowedByFilter(ItemStack itemToCheck) {
        return this.filter.matches(itemToCheck);
    }

    public void toggleMode(int mode) {
        this.filter.toggle(mode);
        this.setChanged();
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

        List<Entity> list = level.getEntities((Entity) null,
                new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                entity -> entity.isAlive() && (entity instanceof Container ||
                        (!(entity instanceof LivingEntity) && Services.PLATFORM.getEntityItemHandler(entity, side) != null)));

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

    private Optional<Pair<ItemHandler, Object>> getItemHandler(FilteredHopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    public SimpleItemHandler getFilterSlots() {
        return this.filter.slots();
    }

    public ItemStack removeFilterItem(int index) {
        if (index >= 0 && index < 5) {
            ItemStack current = this.filter.slots().getStackInSlot(index);
            if (!current.isEmpty()) {
                this.filter.slots().setStackInSlot(index, ItemStack.EMPTY);
                this.setChanged();
                return current.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public void setFilterItem(int index, ItemStack stack) {
        if (index >= 0 && index < 5) {
            ItemStack filterStack = stack.copy();
            if (!filterStack.isEmpty()) {
                filterStack.setCount(1);
            }
            this.filter.slots().setStackInSlot(index, filterStack);
            this.setChanged();
        }
    }

    public ItemStack getFilterItem(int index) {
        if (index >= 0 && index < 5) {
            return this.filter.slots().getStackInSlot(index);
        }
        return ItemStack.EMPTY;
    }
}
