package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.VacuumHopperMenu;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

public class VacuumHopperBlockEntity extends BaseHopperBlockEntity {
    public static final int DEFAULT_RANGE = 4;

    private int range = DEFAULT_RANGE;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return range;
        }

        @Override
        public void set(int index, int value) {
            setRange(value);
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public VacuumHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.VACUUM_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, Config.vacuumHopperTransferCooldown, "Vacuum Hopper");
    }

    public void setRange(int range) {
        this.range = Mth.clamp(range, 1, Config.vacuumHopperMaxRange);
        this.setChanged();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.range = Mth.clamp(input.getIntOr("Range", DEFAULT_RANGE), 1, Config.vacuumHopperMaxRange);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Range", this.range);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VacuumHopperBlockEntity entity) {
        if (!level.isClientSide()) {
            entity.transferCooldown--;
            entity.tickedGameTime = level.getGameTime();
            if (entity.isNotOnTransferCooldown()) {
                entity.setTransferCooldown(0);
                entity.updateHopper(() -> entity.pullItems() || entity.vacuum());
            }
        }
    }

    private boolean vacuum() {
        AABB area = new AABB(this.worldPosition).inflate(this.range);
        for (ItemEntity itemEntity : this.level.getEntitiesOfClass(ItemEntity.class, area, EntitySelector.ENTITY_STILL_ALIVE)) {
            if (!itemEntity.getItem().isEmpty() && captureItem(itemEntity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new VacuumHopperMenu(id, player, this, this.data);
    }
}
