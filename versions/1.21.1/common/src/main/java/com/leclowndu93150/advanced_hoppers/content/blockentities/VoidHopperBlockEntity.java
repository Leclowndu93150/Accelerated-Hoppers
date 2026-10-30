package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.filter.ItemFilter;
import com.leclowndu93150.advanced_hoppers.content.inventory.VoidHopperMenu;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VoidHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
    private int transferCooldown;
    private final ItemFilter filter = new ItemFilter();
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return filter.mode(ItemFilter.BLACKLIST) ? 1 : 0;
        }

        @Override
        public void set(int index, int value) {
            filter.setMode(ItemFilter.BLACKLIST, value != 0);
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public VoidHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.VOID_HOPPER_BLOCK_ENTITY_TYPE, pos, state);
    }

    public boolean accepts(ItemStack stack) {
        return !stack.isEmpty() && filter.matches(stack);
    }

    public void toggleBlacklist() {
        filter.toggle(ItemFilter.BLACKLIST);
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.transferCooldown = tag.getInt("TransferCooldown");
        this.filter.load(tag, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("TransferCooldown", this.transferCooldown);
        this.filter.save(tag, provider);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VoidHopperBlockEntity entity) {
        if (level.isClientSide) {
            return;
        }
        entity.transferCooldown--;
        if (entity.transferCooldown > 0 || !state.getValue(HopperBlock.ENABLED)) {
            return;
        }
        entity.transferCooldown = 0;
        if (entity.voidFromAbove() || entity.voidEntities()) {
            entity.transferCooldown = Config.voidHopperTransferCooldown;
            entity.setChanged();
        }
    }

    public void onItemEntityIsCaptured(ItemEntity itemEntity) {
        if (!itemEntity.isRemoved() && accepts(itemEntity.getItem())) {
            itemEntity.discard();
        }
    }

    private boolean voidEntities() {
        BlockPos above = this.worldPosition.above();
        BlockState aboveState = this.level.getBlockState(above);
        if (!aboveState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS) && aboveState.isCollisionShapeFullBlock(this.level, above)) {
            return false;
        }
        for (ItemEntity itemEntity : HopperBlockEntity.getItemsAtAndAbove(this.level, this)) {
            if (accepts(itemEntity.getItem())) {
                itemEntity.discard();
                return true;
            }
        }
        return false;
    }

    private boolean voidFromAbove() {
        ItemHandler handler = sourceHandler();
        if (handler == null) {
            return false;
        }
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack resource = handler.getStackInSlot(slot);
            if (!accepts(resource)) {
                continue;
            }
            if (!handler.extractItem(slot, resource.getMaxStackSize(), false).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private @Nullable ItemHandler sourceHandler() {
        BlockPos above = this.worldPosition.above();
        BlockState state = this.level.getBlockState(above);
        BlockEntity blockEntity = state.hasBlockEntity() ? this.level.getBlockEntity(above) : null;
        if (blockEntity != null) {
            ItemHandler handler = Services.PLATFORM.getBlockItemHandler(this.level, above, state, blockEntity, Direction.DOWN);
            if (handler != null) {
                return handler;
            }
            if (blockEntity instanceof WorldlyContainer container) {
                return Services.PLATFORM.wrapSidedContainer(container, Direction.DOWN);
            }
            if (blockEntity instanceof Container container) {
                return Services.PLATFORM.wrapContainer(container);
            }
        }
        if (state.getBlock() instanceof WorldlyContainerHolder holder) {
            return Services.PLATFORM.wrapSidedContainer(holder.getContainer(state, this.level, above), Direction.DOWN);
        }
        return null;
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public double getLevelX() {
        return this.worldPosition.getX() + 0.5D;
    }

    @Override
    public double getLevelY() {
        return this.worldPosition.getY() + 0.5D;
    }

    @Override
    public double getLevelZ() {
        return this.worldPosition.getZ() + 0.5D;
    }

    @Override
    public boolean isGridAligned() {
        return true;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new VoidHopperMenu(id, player, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return ItemFilter.SIZE;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(ItemFilter.SIZE, ItemStack.EMPTY);
        for (int i = 0; i < ItemFilter.SIZE; i++) {
            items.set(i, this.filter.slots().getStackInSlot(i));
        }
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < ItemFilter.SIZE && i < items.size(); i++) {
            this.filter.slots().setStackInSlot(i, single(items.get(i)));
        }
    }

    private static ItemStack single(ItemStack stack) {
        ItemStack copy = stack.copy();
        if (!copy.isEmpty()) {
            copy.setCount(1);
        }
        return copy;
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
    public void setItem(int index, ItemStack stack) {
        if (index < ItemFilter.SIZE) {
            this.filter.slots().setStackInSlot(index, single(stack));
            this.setChanged();
        }
    }

    @Override
    public ItemStack getItem(int index) {
        return index < ItemFilter.SIZE ? this.filter.slots().getStackInSlot(index) : ItemStack.EMPTY;
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
}
