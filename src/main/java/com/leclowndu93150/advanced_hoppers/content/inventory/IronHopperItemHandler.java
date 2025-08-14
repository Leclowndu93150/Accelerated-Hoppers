package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.IronHopperBlockEntity;

public class IronHopperItemHandler extends BaseHopperItemHandler<IronHopperBlockEntity> {
    public IronHopperItemHandler(IronHopperBlockEntity hopper) {
        super(hopper, Config.ironHopperTransferCooldown);
    }
}