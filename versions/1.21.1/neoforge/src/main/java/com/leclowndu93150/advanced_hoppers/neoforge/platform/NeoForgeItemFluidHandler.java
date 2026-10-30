package com.leclowndu93150.advanced_hoppers.neoforge.platform;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class NeoForgeItemFluidHandler extends NeoForgeFluidHandler {
    private final IFluidHandlerItem itemHandler;
    private final Player player;
    private final InteractionHand hand;

    public NeoForgeItemFluidHandler(IFluidHandlerItem itemHandler, Player player, InteractionHand hand) {
        super(itemHandler);
        this.itemHandler = itemHandler;
        this.player = player;
        this.hand = hand;
    }

    @Override
    protected void onExecuted() {
        player.setItemInHand(hand, itemHandler.getContainer());
    }
}
