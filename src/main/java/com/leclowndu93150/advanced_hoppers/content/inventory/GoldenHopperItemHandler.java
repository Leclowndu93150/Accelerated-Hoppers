package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.GoldenHopperBlockEntity;

public class GoldenHopperItemHandler extends BaseHopperItemHandler<GoldenHopperBlockEntity> {
    public GoldenHopperItemHandler(GoldenHopperBlockEntity hopper) {
        super(hopper, Config.goldenHopperTransferCooldown);
    }
}