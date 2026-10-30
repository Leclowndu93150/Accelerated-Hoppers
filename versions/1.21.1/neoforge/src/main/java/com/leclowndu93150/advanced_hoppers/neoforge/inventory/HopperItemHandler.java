package com.leclowndu93150.advanced_hoppers.neoforge.inventory;

import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;

public class HopperItemHandler extends BaseHopperItemHandler<BaseHopperBlockEntity> {
    public HopperItemHandler(BaseHopperBlockEntity hopper) {
        super(hopper, hopper.getBaseCooldown());
    }
}
