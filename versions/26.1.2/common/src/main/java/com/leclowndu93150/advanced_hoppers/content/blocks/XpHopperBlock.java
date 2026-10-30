package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.XpHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class XpHopperBlock extends ConfiguredHopperBlock<XpHopperBlockEntity> {
    public XpHopperBlock(Properties properties) {
        super(properties, () -> AHRegistry.XP_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.xpHopperCollectInterval, XpHopperBlockEntity::tick);
    }

    @Override
    public void appendTooltip(Consumer<Component> tooltip) {
        tooltip.accept(Component.translatable("tooltip.advanced_hoppers.xp_hopper").withStyle(ChatFormatting.GRAY));
    }
}
