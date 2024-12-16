package com.leclowndu93150.accelerated_hoppers.events;

import com.leclowndu93150.accelerated_hoppers.AHMain;
import com.leclowndu93150.accelerated_hoppers.utils.TooltipUtil;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(value = Dist.CLIENT,modid = AHMain.MODID)
public class ClientEvents {

    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event){
        if(event.getItemStack().is(Items.HOPPER)){
            TooltipUtil.addIndexTooltip(1,event.getToolTip(),8);
        }
    }
}
