package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.OtherHopperContainer;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class SplitterHopperBlockEntity extends BaseHopperBlockEntity {
    private static final List<Direction> OUTPUTS = List.of(Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    private int nextOutput;

    public SplitterHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.SPLITTER_HOPPER_BLOCK_ENTITY_TYPE, pos, state, 5, Config.splitterHopperTransferCooldown, "Splitter Hopper");
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider provider) {
        super.loadAdditional(input, provider);
        this.nextOutput = (input.contains("NextOutput") ? input.getInt("NextOutput") : 0);
    }

    @Override
    public void saveAdditional(CompoundTag output, HolderLookup.Provider provider) {
        super.saveAdditional(output, provider);
        output.putInt("NextOutput", this.nextOutput);
    }

    @Override
    protected List<Direction> outputDirections() {
        List<Direction> order = new ArrayList<>(OUTPUTS.size());
        for (int i = 0; i < OUTPUTS.size(); i++) {
            order.add(OUTPUTS.get((nextOutput + i) % OUTPUTS.size()));
        }
        return order;
    }

    @Override
    protected void onPushed(Direction direction) {
        this.nextOutput = (OUTPUTS.indexOf(direction) + 1) % OUTPUTS.size();
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new OtherHopperContainer(id, player, this);
    }
}
