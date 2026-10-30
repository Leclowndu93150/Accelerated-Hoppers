package com.leclowndu93150.advanced_hoppers.content.items;

import com.leclowndu93150.advanced_hoppers.content.blocks.TooltipBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class TooltipBlockItem extends BlockItem {
    public TooltipBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (getBlock() instanceof TooltipBlock tooltipBlock) {
            tooltipBlock.appendTooltip(builder);
        }
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
    }
}
