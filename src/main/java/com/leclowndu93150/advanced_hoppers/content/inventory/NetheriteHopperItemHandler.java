package com.leclowndu93150.advanced_hoppers.content.inventory;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.NetheriteHopperBlockEntity;

public class NetheriteHopperItemHandler extends BaseHopperItemHandler<NetheriteHopperBlockEntity> {
    public NetheriteHopperItemHandler(NetheriteHopperBlockEntity hopper) {
        super(hopper, Config.netheriteHopperTransferCooldown);
    }
}