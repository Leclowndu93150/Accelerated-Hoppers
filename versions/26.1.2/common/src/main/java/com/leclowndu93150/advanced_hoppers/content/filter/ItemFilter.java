package com.leclowndu93150.advanced_hoppers.content.filter;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ItemFilter {
    public static final int SIZE = 5;
    public static final int BLACKLIST = 0;
    public static final int TAGS = 1;
    public static final int EXACT = 2;

    private NonNullList<ItemStack> slots = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private final boolean[] modes = new boolean[3];

    public NonNullList<ItemStack> slots() {
        return slots;
    }

    public boolean mode(int mode) {
        return modes[mode];
    }

    public void toggle(int mode) {
        modes[mode] = !modes[mode];
    }

    public void setMode(int mode, boolean value) {
        modes[mode] = value;
    }

    public boolean matches(ItemStack stack) {
        boolean any = false;
        boolean listed = false;
        for (ItemStack filter : slots) {
            if (filter.isEmpty()) {
                continue;
            }
            any = true;
            if (same(filter, stack)) {
                listed = true;
                break;
            }
        }
        if (!any) {
            return true;
        }
        return modes[BLACKLIST] != listed;
    }

    private boolean same(ItemStack filter, ItemStack stack) {
        if (modes[EXACT] ? ItemStack.isSameItemSameComponents(filter, stack) : ItemStack.isSameItem(filter, stack)) {
            return true;
        }
        return modes[TAGS] && stack.typeHolder().tags().anyMatch(filter::is);
    }

    public void load(ValueInput input) {
        this.slots = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        input.child("FilterSlots").ifPresent(filterInput -> ContainerHelper.loadAllItems(filterInput, this.slots));
        modes[BLACKLIST] = input.getBooleanOr("Blacklist", false);
        modes[TAGS] = input.getBooleanOr("TagMode", false);
        modes[EXACT] = input.getBooleanOr("ExactMode", false);
    }

    public void save(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("FilterSlots"), this.slots, true);
        output.putBoolean("Blacklist", modes[BLACKLIST]);
        output.putBoolean("TagMode", modes[TAGS]);
        output.putBoolean("ExactMode", modes[EXACT]);
    }
}
