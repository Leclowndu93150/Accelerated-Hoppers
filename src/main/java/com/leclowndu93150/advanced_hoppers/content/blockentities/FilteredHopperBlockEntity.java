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
import java.util.function.Supplier;

public class FilteredHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
    protected int transferCooldown = Config.filteredHopperTransferCooldown;
    protected long tickedGameTime;
    private ItemStackHandler inventory = new ItemStackHandler(5);

    public FilteredHopperBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.FILTERED_HOPPER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.transferCooldown = nbt.getInt("TransferCooldown");
        this.inventory = new ItemStackHandler();
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
        return this.inventory.getSlots();
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(this.inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            items.set(i, this.inventory.getStackInSlot(i));
        }
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemsIn) {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (i < itemsIn.size()) {
                this.inventory.setStackInSlot(i, itemsIn.get(i));
            } else {
                this.inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        // Prevent filter slots (0-4) from being extracted by other inventories
        if (index < 5) {
            return ItemStack.EMPTY;
        }
        this.unpackLootTable(null);
        ItemStack stack = this.inventory.extractItem(index, count, false);
        this.setChanged();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        // Prevent filter slots (0-4) from being extracted by other inventories
        if (index < 5) {
            return ItemStack.EMPTY;
        }
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
        return this.transferCooldown <= Config.filteredHopperTransferCooldown;
    }

    public static List<Entity> getAllAliveEntitiesAt(Level level, double x, double y, double z, Predicate<? super Entity> filtered) {
        return level.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                entity -> entity.isAlive() && filtered.test(entity));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FilteredHopperBlockEntity entity) {
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

    protected ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, Object destInventoryObj, ItemStack stack) {
        IItemHandler destInventory = (IItemHandler) destInventoryObj;
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    private ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot) {
        ItemStack result = stack;
        if (canInsertItemIntoSlot(destInventory, stack, slot)) {
            ItemStack remainingStack = destInventory.insertItem(slot, stack, false);
            return remainingStack;
        }
        return result;
    }

    private boolean canInsertItemIntoSlot(IItemHandler inventory, ItemStack stack, int slot) {
        return inventory.insertItem(slot, stack, true).getCount() < stack.getCount();
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
        List<Entity> list = getAllAliveEntitiesAt(level, x, y, z,
                entity -> entity instanceof Container || !(entity instanceof LivingEntity) && entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side) != null);
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
                return Optional.of(ImmutablePair.of(new InvWrapper((containerEntity)), entity));
            }
        }
        return Optional.empty();
    }

    protected boolean alternateDestination = false;

    protected boolean pullItemsFromItemHandler(Object itemHandler) {
        IItemHandler sourceHandler = (IItemHandler) itemHandler;
        Optional<Pair<Object, Object>> facingHandler = getItemHandler(this, this.getBlockState().getValue(HopperBlock.FACING));
        Optional<Pair<Object, Object>> bottomHandler = getItemHandler(this, Direction.DOWN);

        for (int sourceSlot = 0; sourceSlot < sourceHandler.getSlots(); sourceSlot++) {
            ItemStack stack = sourceHandler.extractItem(sourceSlot, 1, true);

            if (!stack.isEmpty() && isItemAllowedByFilter(stack)) {
                if (facingHandler.isPresent() && bottomHandler.isPresent()) {
                    IItemHandler primaryDest = alternateDestination ?
                            (IItemHandler) facingHandler.get().getKey() :
                            (IItemHandler) bottomHandler.get().getKey();

                    IItemHandler secondaryDest = alternateDestination ?
                            (IItemHandler) bottomHandler.get().getKey() :
                            (IItemHandler) facingHandler.get().getKey();

                    if (insertItemDirectlyIntoDestination(primaryDest, stack.copy()).isEmpty()) {
                        sourceHandler.extractItem(sourceSlot, 1, false);
                        alternateDestination = !alternateDestination;
                        return true;
                    }

                    if (insertItemDirectlyIntoDestination(secondaryDest, stack.copy()).isEmpty()) {
                        sourceHandler.extractItem(sourceSlot, 1, false);
                        alternateDestination = !alternateDestination;
                        return true;
                    }
                }
                else if (facingHandler.isPresent()) {
                    IItemHandler destHandler = (IItemHandler) facingHandler.get().getKey();
                    if (insertItemDirectlyIntoDestination(destHandler, stack.copy()).isEmpty()) {
                        sourceHandler.extractItem(sourceSlot, 1, false);
                        return true;
                    }
                }
                else if (bottomHandler.isPresent()) {
                    IItemHandler bottomDest = (IItemHandler) bottomHandler.get().getKey();
                    if (insertItemDirectlyIntoDestination(bottomDest, stack.copy()).isEmpty()) {
                        sourceHandler.extractItem(sourceSlot, 1, false);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ItemStack insertItemDirectlyIntoDestination(IItemHandler destInventory, ItemStack stack) {
        for (int slot = 0; slot < destInventory.getSlots(); slot++) {
            if (destInventory.isItemValid(slot, stack)) {
                ItemStack remaining = destInventory.insertItem(slot, stack, false);
                if (remaining.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }


    private boolean isItemAllowedByFilter(ItemStack itemToCheck) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < 5; i++) {
            ItemStack filterStack = this.getItem(i);
            if (!filterStack.isEmpty()) {
                hasAnyFilter = true;
                if (filterStack.getItem() == itemToCheck.getItem()) {
                    return true;
                }
            }
        }

        return !hasAnyFilter;
    }

    protected Object getOwnItemHandler() {
        return this.inventory;
    }

    protected void updateHopper(Supplier<Boolean> action) {
        if (this.level != null && !this.level.isClientSide) {
            if (this.isNotOnTransferCooldown() && this.getBlockState().getValue(HopperBlock.ENABLED)) {
                boolean flag = action.get();
                if (flag) {
                    this.setTransferCooldown(Config.filteredHopperTransferCooldown);
                    this.setChanged();
                }
            }
        }
    }

    private Optional<Pair<Object, Object>> getItemHandler(FilteredHopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }


    private boolean captureItem(ItemEntity itemEntity) {
        boolean flag = false;
        ItemStack itemstack = itemEntity.getItem().copy();
        if (isItemAllowedByFilter(itemstack)) {
            ItemStack itemstack1 = putStackInInventoryAllSlots(null, this, getOwnItemHandler(), itemstack);
            if (itemstack1.isEmpty()) {
                flag = true;
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
            } else {
                itemEntity.setItem(itemstack1);
            }
        }
        return flag;
    }

    protected boolean pullItems() {
        return getItemHandler(this, Direction.UP)
                .map(itemHandlerResult -> {
                        return pullItemsFromItemHandler(itemHandlerResult.getKey());
                }).orElseGet(() -> {
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