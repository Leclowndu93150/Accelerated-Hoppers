package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilteredFlopperMenu;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class FilteredFlopperBlockEntity extends FlopperBlockEntity implements MenuProvider {
    private Fluid filter = Fluids.EMPTY;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BuiltInRegistries.FLUID.getId(tank.getFluid().getFluid());
                case 1 -> tank.getFluidAmount() & 0xFFFF;
                case 2 -> (tank.getFluidAmount() >>> 16) & 0xFFFF;
                case 3 -> tank.getCapacity() & 0xFFFF;
                case 4 -> (tank.getCapacity() >>> 16) & 0xFFFF;
                default -> BuiltInRegistries.FLUID.getId(filter);
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    public FilteredFlopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.FILTERED_FLOPPER_BLOCK_ENTITY_TYPE, pos, state, Config.flopperCapacity, () -> Config.flopperIORate);
    }

    public Fluid getFilter() {
        return filter;
    }

    public void setFilter(Fluid fluid) {
        this.filter = fluid.isSame(Fluids.EMPTY) ? Fluids.EMPTY : fluid.defaultFluidState().getType();
        this.setChanged();
    }

    @Override
    public boolean accepts(FluidVolume fluid) {
        return filter == Fluids.EMPTY || fluid.getFluid().isSame(filter);
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider provider) {
        super.loadAdditional(input, provider);
        ResourceLocation id = ResourceLocation.tryParse(input.getString("FilterFluid"));
        this.filter = id == null ? Fluids.EMPTY : BuiltInRegistries.FLUID.getOptional(id).orElse(Fluids.EMPTY);
    }

    @Override
    public void saveAdditional(CompoundTag output, HolderLookup.Provider provider) {
        super.saveAdditional(output, provider);
        if (this.filter != Fluids.EMPTY) {
            output.putString("FilterFluid", BuiltInRegistries.FLUID.getKey(this.filter).toString());
        }
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FilteredFlopperMenu(id, inventory, this, this.data);
    }
}
