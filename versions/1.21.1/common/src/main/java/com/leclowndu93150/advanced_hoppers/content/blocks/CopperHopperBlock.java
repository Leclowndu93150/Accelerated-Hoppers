package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.CopperHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public class CopperHopperBlock extends ConfiguredHopperBlock<CopperHopperBlockEntity> implements ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
    private final WeatheringCopper.WeatherState age;
    private final boolean waxed;

    public CopperHopperBlock(Properties properties, WeatheringCopper.WeatherState age, boolean waxed) {
        super(properties, () -> AHRegistry.COPPER_HOPPER_BLOCK_ENTITY_TYPE, () -> cooldownFor(age), BaseHopperBlockEntity::baseTick);
        this.age = age;
        this.waxed = waxed;
    }

    public static int cooldownFor(WeatheringCopper.WeatherState age) {
        return switch (age) {
            case UNAFFECTED -> Config.copperHopperTransferCooldown;
            case EXPOSED -> Config.exposedCopperHopperTransferCooldown;
            case WEATHERED -> Config.weatheredCopperHopperTransferCooldown;
            case OXIDIZED -> Config.oxidizedCopperHopperTransferCooldown;
        };
    }

    public boolean isWaxed() {
        return waxed;
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return age;
    }

    @Override
    public float getChanceModifier() {
        return age == WeatheringCopper.WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        if (waxed || age == WeatheringCopper.WeatherState.OXIDIZED) {
            return Optional.empty();
        }
        return Optional.of(AHRegistry.COPPER_HOPPERS[age.ordinal() + 1].withPropertiesOf(state));
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !waxed && age != WeatheringCopper.WeatherState.OXIDIZED;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        boolean waxable = stack.is(Items.HONEYCOMB) && !waxed;
        boolean scrapable = stack.getItem() instanceof AxeItem && (waxed || age != WeatheringCopper.WeatherState.UNAFFECTED);
        if (waxable || scrapable) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() instanceof CopperHopperBlock) {
            return;
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
