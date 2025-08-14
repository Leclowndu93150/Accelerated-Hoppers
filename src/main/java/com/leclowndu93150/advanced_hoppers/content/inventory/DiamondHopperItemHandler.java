package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.DiamondHopperBlockEntity;

public class DiamondHopperItemHandler extends BaseHopperItemHandler<DiamondHopperBlockEntity> {
    public DiamondHopperItemHandler(DiamondHopperBlockEntity hopper) {
        super(hopper, Config.diamondHopperTransferCooldown);
    }
}