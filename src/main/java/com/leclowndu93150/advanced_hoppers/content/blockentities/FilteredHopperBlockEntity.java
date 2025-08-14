package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilterHopperContainer;
import com.leclowndu93150.advanced_hoppers.registries.Registry;
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
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FilteredHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
    protected int transferCooldown = Config.filteredHopperTransferCooldown;
    protected long tickedGameTime;
    // Only 5 slots for filter items (ghost slots - max stack size of 1)
    private ItemStackHandler filterSlots = new ItemStackHandler(5) {
        @Override
        public int getSlotLimit(int slot) {
            return 1; // Ghost slots only hold 1 item as template
        }
        
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }
    };
    
    // For alternating between outputs when both are available
    protected boolean useSecondaryNext = false;

    public FilteredHopperBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.FILTERED_HOPPER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.transferCooldown = nbt.getInt("TransferCooldown");
        this.useSecondaryNext = nbt.getBoolean("UseSecondaryNext");
        this.filterSlots = new ItemStackHandler(5) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        if (nbt.contains("FilterSlots")) {
            this.filterSlots.deserializeNBT(provider, nbt.getCompound("FilterSlots"));
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putInt("TransferCooldown", this.transferCooldown);
        compound.putBoolean("UseSecondaryNext", this.useSecondaryNext);
        compound.put("FilterSlots", this.filterSlots.serializeNBT(provider));
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
        return new FilterHopperContainer(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return 5; // Only the filter slots
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
        for (int i = 0; i < 5; i++) {
            items.set(i, this.filterSlots.getStackInSlot(i));
        }
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemsIn) {
        for (int i = 0; i < 5 && i < itemsIn.size(); i++) {
            ItemStack stack = itemsIn.get(i).copy();
            if (!stack.isEmpty()) {
                stack.setCount(1); // Force ghost slots to only have 1 item
            }
            this.filterSlots.setStackInSlot(i, stack);
        }
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        // Ghost slots should NEVER be extracted by external systems
        // They are only for filtering, not actual item storage
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        // Ghost slots should NEVER be extracted by external systems
        // They are only for filtering, not actual item storage
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        if (index < 5) {
            ItemStack filterStack = stack.copy();
            if (!filterStack.isEmpty()) {
                filterStack.setCount(1); // Force ghost slots to only have 1 item
            }
            this.filterSlots.setStackInSlot(index, filterStack);
            this.setChanged();
        }
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        if (index < 5) {
            return this.filterSlots.getStackInSlot(index);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty() {
        // We're always "empty" since we don't store items, only filters
        return true;
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
        if (isItemAllowedByFilter(itemEntity.getItem())) {
            if (transferItemEntityToDestination(itemEntity)) {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private boolean transferItemEntityToDestination(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem().copy();
        Direction facing = this.getBlockState().getValue(HopperBlock.FACING);
        Optional<Pair<Object, Object>> facingHandler = getItemHandler(this, facing);
        
        if (facingHandler.isPresent()) {
            // If facing down, only use that direction
            if (facing == Direction.DOWN) {
                IItemHandler destHandler = (IItemHandler) facingHandler.get().getKey();
                ItemStack remainder = insertIntoHandler(destHandler, stack);
                if (remainder.isEmpty()) {
                    return true;
                }
                itemEntity.setItem(remainder);
                return false;
            }
            // If facing sideways, check for hopper below for 50/50 split
            else {
                Optional<Pair<Object, Object>> bottomHandler = getItemHandler(this, Direction.DOWN);
                boolean hasHopperBelow = bottomHandler.isPresent() && isHopper(bottomHandler.get().getValue());
                
                if (hasHopperBelow) {
                    // 50/50 distribution between facing and bottom
                    IItemHandler primaryDest = useSecondaryNext ? 
                        (IItemHandler) bottomHandler.get().getKey() : 
                        (IItemHandler) facingHandler.get().getKey();
                    IItemHandler secondaryDest = useSecondaryNext ? 
                        (IItemHandler) facingHandler.get().getKey() : 
                        (IItemHandler) bottomHandler.get().getKey();
                    
                    // Try primary destination first
                    ItemStack remainder = insertIntoHandler(primaryDest, stack);
                    if (remainder.isEmpty()) {
                        useSecondaryNext = !useSecondaryNext; // Alternate for next item
                        return true;
                    }
                    // If primary is full, try secondary
                    remainder = insertIntoHandler(secondaryDest, remainder);
                    if (remainder.isEmpty()) {
                        // Don't toggle since we used fallback
                        return true;
                    }
                    
                    itemEntity.setItem(remainder);
                    return false;
                } else {
                    // No hopper below, only use facing direction
                    IItemHandler destHandler = (IItemHandler) facingHandler.get().getKey();
                    ItemStack remainder = insertIntoHandler(destHandler, stack);
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

    private ItemStack insertIntoHandler(IItemHandler handler, ItemStack stack) {
        ItemStack remaining = stack.copy();
        for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
            remaining = handler.insertItem(slot, remaining, false);
        }
        return remaining;
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
        // First try to pull from container above
        Optional<Pair<Object, Object>> aboveHandler = getItemHandler(this, Direction.UP);
        if (aboveHandler.isPresent()) {
            return pullFromHandlerAndTransfer((IItemHandler) aboveHandler.get().getKey());
        }
        
        // If no container above, try to capture item entities
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

    protected boolean pullFromHandlerAndTransfer(IItemHandler sourceHandler) {
        Direction facing = this.getBlockState().getValue(HopperBlock.FACING);
        Optional<Pair<Object, Object>> facingHandler = getItemHandler(this, facing);
        
        // Try to pull one item from source
        for (int sourceSlot = 0; sourceSlot < sourceHandler.getSlots(); sourceSlot++) {
            ItemStack stack = sourceHandler.extractItem(sourceSlot, 1, true);
            
            if (!stack.isEmpty() && isItemAllowedByFilter(stack)) {
                // If facing down, only use that direction
                if (facing == Direction.DOWN && facingHandler.isPresent()) {
                    IItemHandler destHandler = (IItemHandler) facingHandler.get().getKey();
                    if (insertIntoHandler(destHandler, stack.copy()).isEmpty()) {
                        sourceHandler.extractItem(sourceSlot, 1, false);
                        return true;
                    }
                }
                // If facing sideways, check for hopper below for 50/50 split
                else if (facing != Direction.DOWN && facingHandler.isPresent()) {
                    Optional<Pair<Object, Object>> bottomHandler = getItemHandler(this, Direction.DOWN);
                    boolean hasHopperBelow = bottomHandler.isPresent() && isHopper(bottomHandler.get().getValue());
                    
                    if (hasHopperBelow) {
                        // 50/50 distribution between facing and bottom
                        IItemHandler primaryDest = useSecondaryNext ? 
                            (IItemHandler) bottomHandler.get().getKey() : 
                            (IItemHandler) facingHandler.get().getKey();
                        IItemHandler secondaryDest = useSecondaryNext ? 
                            (IItemHandler) facingHandler.get().getKey() : 
                            (IItemHandler) bottomHandler.get().getKey();
                        
                        // Try primary destination first
                        if (insertIntoHandler(primaryDest, stack.copy()).isEmpty()) {
                            sourceHandler.extractItem(sourceSlot, 1, false);
                            useSecondaryNext = !useSecondaryNext; // Alternate for next item
                            return true;
                        }
                        // If primary is full, try secondary
                        if (insertIntoHandler(secondaryDest, stack.copy()).isEmpty()) {
                            sourceHandler.extractItem(sourceSlot, 1, false);
                            // Don't toggle since we used fallback
                            return true;
                        }
                    } else {
                        // No hopper below, only use facing direction
                        IItemHandler destHandler = (IItemHandler) facingHandler.get().getKey();
                        if (insertIntoHandler(destHandler, stack.copy()).isEmpty()) {
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
        boolean hasAnyFilter = false;
        
        for (int i = 0; i < 5; i++) {
            ItemStack filterStack = this.filterSlots.getStackInSlot(i);
            if (!filterStack.isEmpty()) {
                hasAnyFilter = true;
                if (ItemStack.isSameItem(filterStack, itemToCheck)) {
                    return true;
                }
            }
        }
        
        // If no filters are set, allow everything
        return !hasAnyFilter;
    }

    protected Optional<Pair<Object, Object>> getItemHandler(Level level, double x, double y, double z, final Direction side) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(blockpos);
        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity != null) {
                IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockpos, state, blockEntity, side);
                if (handler != null) {
                    return Optional.of(ImmutablePair.of(handler, blockEntity));
                }
            }
            if (blockEntity instanceof WorldlyContainer container) {
                return Optional.of(ImmutablePair.of(new SidedInvWrapper(container, side), state));
            }
            if (blockEntity instanceof Container container) {
                return Optional.of(ImmutablePair.of(new InvWrapper(container), state));
            }
        }
        Block block = state.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            return Optional.of(ImmutablePair.of(new SidedInvWrapper(((WorldlyContainerHolder) block).getContainer(state, level, blockpos), side), state));
        }
        
        List<Entity> list = level.getEntities((Entity) null, 
            new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
            entity -> entity.isAlive() && (entity instanceof Container || 
                (!(entity instanceof LivingEntity) && entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side) != null)));
        
        if (!list.isEmpty()) {
            Entity entity = list.get(level.random.nextInt(list.size()));
            IItemHandler cap = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side);
            if (cap != null) {
                return Optional.of(ImmutablePair.of(cap, entity));
            }
            if (entity instanceof WorldlyContainer container) {
                return Optional.of(ImmutablePair.of(new SidedInvWrapper(container, side), entity));
            }
            if (entity instanceof Container containerEntity) {
                return Optional.of(ImmutablePair.of(new InvWrapper(containerEntity), entity));
            }
        }
        return Optional.empty();
    }

    private Optional<Pair<Object, Object>> getItemHandler(FilteredHopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    public ItemStackHandler getFilterSlots() {
        return filterSlots;
    }

    // Special methods for GUI interaction with ghost slots
    public ItemStack removeFilterItem(int index) {
        if (index >= 0 && index < 5) {
            ItemStack current = this.filterSlots.getStackInSlot(index);
            if (!current.isEmpty()) {
                this.filterSlots.setStackInSlot(index, ItemStack.EMPTY);
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
                filterStack.setCount(1); // Force ghost slots to only have 1 item
            }
            this.filterSlots.setStackInSlot(index, filterStack);
            this.setChanged();
        }
    }

    public ItemStack getFilterItem(int index) {
        if (index >= 0 && index < 5) {
            return this.filterSlots.getStackInSlot(index);
        }
        return ItemStack.EMPTY;
    }
}