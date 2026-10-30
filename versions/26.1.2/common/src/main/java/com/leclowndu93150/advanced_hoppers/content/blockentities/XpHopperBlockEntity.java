package com.leclowndu93150.advanced_hoppers.content.blockentities;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.inventory.XpHopperMenu;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class XpHopperBlockEntity extends BlockEntity implements MenuProvider {
    public static final int TAKE_ONE = 0;
    public static final int TAKE_TEN = 1;
    public static final int TAKE_ALL = 2;

    private int storedXp;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? storedXp & 0xFFFF : (storedXp >>> 16) & 0xFFFF;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public XpHopperBlockEntity(BlockPos pos, BlockState state) {
        super(AHRegistry.XP_HOPPER_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static int xpForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        }
        return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
    }

    public static int levelsFor(int points) {
        int level = 0;
        while (points >= xpForNextLevel(level)) {
            points -= xpForNextLevel(level);
            level++;
        }
        return level;
    }

    public int getStoredXp() {
        return storedXp;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.storedXp = Math.max(0, input.getIntOr("StoredXp", 0));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("StoredXp", this.storedXp);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, XpHopperBlockEntity entity) {
        if (!level.isClientSide() && state.getValue(HopperBlock.ENABLED) && level.getGameTime() % Math.max(1, Config.xpHopperCollectInterval) == 0) {
            AABB area = new AABB(pos).expandTowards(0, 1, 0);
            for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class, area, EntitySelector.ENTITY_STILL_ALIVE)) {
                entity.absorb(orb);
            }
        }
    }

    public void absorb(ExperienceOrb orb) {
        if (orb.isRemoved()) {
            return;
        }
        long total = (long) this.storedXp + (long) orb.getValue() * orb.count;
        this.storedXp = (int) Math.min(Integer.MAX_VALUE, total);
        orb.discard();
        this.setChanged();
    }

    public void take(Player player, int mode) {
        int points = switch (mode) {
            case TAKE_ONE -> pointsForLevels(player, 1);
            case TAKE_TEN -> pointsForLevels(player, 10);
            default -> this.storedXp;
        };
        points = Math.min(points, this.storedXp);
        if (points <= 0) {
            return;
        }
        this.storedXp -= points;
        player.giveExperiencePoints(points);
        this.setChanged();
    }

    private static int pointsForLevels(Player player, int levels) {
        int points = Math.round(xpForNextLevel(player.experienceLevel) * (1.0F - player.experienceProgress));
        for (int i = 1; i < levels; i++) {
            points += xpForNextLevel(player.experienceLevel + i);
        }
        return points;
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new XpHopperMenu(id, inventory, this, this.data);
    }
}
