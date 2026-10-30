package com.leclowndu93150.advanced_hoppers.content.items;

import com.leclowndu93150.advanced_hoppers.content.blockentities.LinkedHopperBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Consumer;

public class HopperLinkerItem extends Item {
    public HopperLinkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (!(level.getBlockEntity(pos) instanceof LinkedHopperBlockEntity hopper) || player == null) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (player.isShiftKeyDown()) {
            hopper.setTarget(null);
            stack.remove(DataComponents.LODESTONE_TRACKER);
            player.sendOverlayMessage(Component.translatable("message.advanced_hoppers.unlinked"));
            return InteractionResult.SUCCESS;
        }
        Optional<GlobalPos> source = Optional.ofNullable(stack.get(DataComponents.LODESTONE_TRACKER)).flatMap(LodestoneTracker::target);
        if (source.isEmpty()) {
            stack.set(DataComponents.LODESTONE_TRACKER, new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), pos)), false));
            player.sendOverlayMessage(Component.translatable("message.advanced_hoppers.source_selected", pos.getX(), pos.getY(), pos.getZ()));
            return InteractionResult.SUCCESS;
        }
        stack.remove(DataComponents.LODESTONE_TRACKER);
        GlobalPos from = source.get();
        if (from.dimension() != level.dimension() || !(level.getBlockEntity(from.pos()) instanceof LinkedHopperBlockEntity sourceHopper)
                || !sourceHopper.canLinkTo(pos)) {
            player.sendOverlayMessage(Component.translatable("message.advanced_hoppers.link_failed").withStyle(ChatFormatting.RED));
            return InteractionResult.FAIL;
        }
        sourceHopper.setTarget(pos);
        player.sendOverlayMessage(Component.translatable("message.advanced_hoppers.linked", pos.getX(), pos.getY(), pos.getZ()));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);
        if (tracker != null && tracker.target().isPresent()) {
            BlockPos pos = tracker.target().get().pos();
            builder.accept(Component.translatable("tooltip.advanced_hoppers.linker_source", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.AQUA));
        } else {
            builder.accept(Component.translatable("tooltip.advanced_hoppers.linker").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
    }
}
