package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.PulseHopperMenu;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.ItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class PulseHopperBlockEntity extends BaseHopperBlockEntity {
    public static final int DEFAULT_PER_PULSE = 8;

    private int perPulse = DEFAULT_PER_PULSE;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? perPulse : getBlockState().getValue(HopperBlock.ENABLED) ? 0 : 1;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                setPerPulse(value);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public PulseHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.PULSE_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, Config.pulseHopperTransferCooldown, "Pulse Hopper");
    }

    public void setPerPulse(int perPulse) {
        this.perPulse = Mth.clamp(perPulse, 1, 64);
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider provider) {
        super.loadAdditional(input, provider);
        this.perPulse = Mth.clamp((input.contains("PerPulse") ? input.getInt("PerPulse") : DEFAULT_PER_PULSE), 1, 64);
    }

    @Override
    public void saveAdditional(CompoundTag output, HolderLookup.Provider provider) {
        super.saveAdditional(output, provider);
        output.putInt("PerPulse", this.perPulse);
    }

    @Override
    protected boolean canTransfer() {
        return true;
    }

    @Override
    protected boolean transferItemsOut() {
        return false;
    }

    public void pulse() {
        if (this.level == null || this.level.isClientSide || this.isEmpty()) {
            return;
        }
        Direction facing = this.getBlockState().getValue(HopperBlock.FACING);
        Optional<Pair<ItemHandler, Object>> destination = getItemHandler(this, facing);
        if (destination.isEmpty()) {
            return;
        }
        int remaining = this.perPulse;
        while (remaining > 0) {
            int moved = pushAmount(destination.get(), remaining);
            if (moved == 0) {
                break;
            }
            remaining -= moved;
        }
        if (remaining < this.perPulse) {
            this.setChanged();
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new PulseHopperMenu(id, player, this, this.data);
    }
}
