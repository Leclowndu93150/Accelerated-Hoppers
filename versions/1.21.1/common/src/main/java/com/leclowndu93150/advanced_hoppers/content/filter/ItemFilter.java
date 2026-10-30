package com.leclowndu93150.advanced_hoppers.content.filter;

import com.leclowndu93150.advanced_hoppers.transfer.SimpleItemHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemFilter {
    public static final int SIZE = 5;
    public static final int BLACKLIST = 0;
    public static final int TAGS = 1;
    public static final int EXACT = 2;

    private SimpleItemHandler slots = createSlots();
    private final boolean[] modes = new boolean[3];

    private static SimpleItemHandler createSlots() {
        return new SimpleItemHandler(SIZE) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    public SimpleItemHandler slots() {
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
        for (int i = 0; i < SIZE; i++) {
            ItemStack filter = slots.getStackInSlot(i);
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
        return modes[TAGS] && stack.getTags().anyMatch(filter::is);
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        this.slots = createSlots();
        if (tag.contains("FilterSlots")) {
            this.slots.deserializeNBT(provider, tag.getCompound("FilterSlots"));
        }
        modes[BLACKLIST] = tag.getBoolean("Blacklist");
        modes[TAGS] = tag.getBoolean("TagMode");
        modes[EXACT] = tag.getBoolean("ExactMode");
    }

    public void save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("FilterSlots", this.slots.serializeNBT(provider));
        tag.putBoolean("Blacklist", modes[BLACKLIST]);
        tag.putBoolean("TagMode", modes[TAGS]);
        tag.putBoolean("ExactMode", modes[EXACT]);
    }
}
