package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.LinkedHopperMenu;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.Nullable;

public class LinkedHopperBlockEntity extends BaseHopperBlockEntity {
    private @Nullable BlockPos target;
    private @Nullable BlockPos source;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == 0) {
                return (target == null ? 0 : 1) | (getSource() == null ? 0 : 2);
            }
            BlockPos pos = index <= 6 ? target : source;
            if (pos == null) {
                return 0;
            }
            int offset = (index - 1) % 6;
            int value = switch (offset / 2) {
                case 0 -> pos.getX();
                case 1 -> pos.getY();
                default -> pos.getZ();
            };
            return offset % 2 == 0 ? value & 0xFFFF : (value >>> 16) & 0xFFFF;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 13;
        }
    };

    public LinkedHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.LINKED_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, Config.linkedHopperTransferCooldown, "Linked Hopper");
    }

    public @Nullable BlockPos getTarget() {
        return target;
    }

    public void setTarget(@Nullable BlockPos target) {
        this.target = target;
        this.setChanged();
        if (target != null && this.level != null && this.level.getBlockEntity(target) instanceof LinkedHopperBlockEntity receiver) {
            receiver.setSource(this.worldPosition);
        }
    }

    public @Nullable BlockPos getSource() {
        if (this.source != null && this.level != null && this.level.isLoaded(this.source)
                && !(this.level.getBlockEntity(this.source) instanceof LinkedHopperBlockEntity sender && this.worldPosition.equals(sender.getTarget()))) {
            setSource(null);
        }
        return this.source;
    }

    private void setSource(@Nullable BlockPos source) {
        this.source = source;
        this.setChanged();
    }

    public boolean canLinkTo(BlockPos other) {
        return !other.equals(this.worldPosition) && other.closerThan(this.worldPosition, Config.linkedHopperMaxDistance + 1);
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider provider) {
        super.loadAdditional(input, provider);
        this.target = input.contains("Target") ? BlockPos.of(input.getLong("Target")) : null;
        this.source = input.contains("Source") ? BlockPos.of(input.getLong("Source")) : null;
    }

    @Override
    public void saveAdditional(CompoundTag output, HolderLookup.Provider provider) {
        super.saveAdditional(output, provider);
        if (this.target != null) {
            output.putLong("Target", this.target.asLong());
        }
        if (this.source != null) {
            output.putLong("Source", this.source.asLong());
        }
    }

    @Override
    protected boolean transferItemsOut() {
        if (this.target == null || this.level == null || !this.level.isLoaded(this.target) || !canLinkTo(this.target)) {
            return super.transferItemsOut();
        }
        if (!(this.level.getBlockEntity(this.target) instanceof LinkedHopperBlockEntity other)) {
            return super.transferItemsOut();
        }
        if (other.getSource() == null) {
            other.setSource(this.worldPosition);
        }
        ItemHandler handler = Services.PLATFORM.getBlockItemHandler(this.level, this.target, other.getBlockState(), other, Direction.UP);
        if (handler == null) {
            return false;
        }
        return pushInto(ImmutablePair.of(handler, other));
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new LinkedHopperMenu(id, player, this, this.data);
    }
}
