package com.leclowndu93150.accelerated_hoppers.content.inventory;

import com.leclowndu93150.accelerated_hoppers.registries.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WoodenHopperContainer extends AbstractContainerMenu {
    public Container hopper;

    public WoodenHopperContainer(int id, Inventory playerInventory, Container inventory) {
        super(Registry.WOODEN_HOPPER_CONTAINER.get(), id);
        this.hopper = inventory;
        checkContainerSize(inventory, 1);
        inventory.startOpen(playerInventory.player);
        this.addSlot(new Slot(inventory, 0, 80, 20));
        for(int l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
    }

    public WoodenHopperContainer(int id, Inventory playerInventoryIn) {
        this(id, playerInventoryIn, new SimpleContainer(1));
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.hopper.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.hopper.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.hopper.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return this.stillValid(player);
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        this.hopper.stopOpen(playerIn);
    }
}
