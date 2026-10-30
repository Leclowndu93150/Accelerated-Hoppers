package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredFlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.platform.Services;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import com.leclowndu93150.advanced_hoppers.transfer.FluidVolume;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class FilteredFlopperMenu extends AbstractContainerMenu {
    private final @Nullable FilteredFlopperBlockEntity flopper;
    private final ContainerData data;
    private final Container ghost = new SimpleContainer(1);

    public FilteredFlopperMenu(int id, Inventory playerInventory, @Nullable FilteredFlopperBlockEntity flopper, ContainerData data) {
        super(AHRegistry.FILTERED_FLOPPER_MENU, id);
        this.flopper = flopper;
        this.data = data;
        if (flopper != null && flopper.getFilter() != Fluids.EMPTY) {
            ghost.setItem(0, new ItemStack(flopper.getFilter().getBucket()));
        }
        this.addSlot(new Slot(ghost, 0, 52, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
        this.addDataSlots(data);
    }

    public FilteredFlopperMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, null, new SimpleContainerData(6));
    }

    private static int join(int low, int high) {
        return (low & 0xFFFF) | ((high & 0xFFFF) << 16);
    }

    public Fluid getFluid() {
        return BuiltInRegistries.FLUID.byId(data.get(0));
    }

    public int getAmount() {
        return join(data.get(1), data.get(2));
    }

    public int getCapacity() {
        return Math.max(1, join(data.get(3), data.get(4)));
    }

    public Fluid getFilter() {
        return BuiltInRegistries.FLUID.byId(data.get(5));
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ClickType input, Player player) {
        if (slotIndex != 0) {
            super.clicked(slotIndex, buttonNum, input, player);
            return;
        }
        ItemStack carried = getCarried();
        FluidVolume contained = carried.isEmpty() ? FluidVolume.EMPTY : Services.PLATFORM.getContainedFluid(carried);
        if (!carried.isEmpty() && contained.isEmpty()) {
            return;
        }
        ghost.setItem(0, carried.isEmpty() ? ItemStack.EMPTY : carried.copyWithCount(1));
        if (flopper != null) {
            flopper.setFilter(contained.isEmpty() ? Fluids.EMPTY : contained.getFluid());
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return flopper == null || !flopper.isRemoved() && Container.stillValidBlockEntity(flopper, player);
    }
}
