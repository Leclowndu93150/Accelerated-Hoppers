package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.WoodenHopperBlockEntity;

public class WoodenHopperItemHandler extends BaseHopperItemHandler<WoodenHopperBlockEntity> {
    public WoodenHopperItemHandler(WoodenHopperBlockEntity hopper) {
        super(hopper, Config.woodenHopperTransferCooldown);
    }
}