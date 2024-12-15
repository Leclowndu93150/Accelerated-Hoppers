package com.leclowndu93150.accelerated_hoppers.registries;

import com.leclowndu93150.accelerated_hoppers.content.blockentities.WoodenHopperBlockEntity;
import com.leclowndu93150.accelerated_hoppers.content.blocks.WoodenHopperBlock;
import com.leclowndu93150.accelerated_hoppers.content.inventory.WoodenHopperContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.leclowndu93150.accelerated_hoppers.AHMain.MODID;

public class Registry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<WoodenHopperBlock> WOODEN_HOPPER = BLOCKS.register("wooden_hopper", () -> new WoodenHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER)));
    public static final DeferredItem<BlockItem> WOODEN_HOPPER_ITEM = ITEMS.register("wooden_hopper", () -> new BlockItem(WOODEN_HOPPER.get(), new Item.Properties()));


    //Block entity types
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    //gonna have wooden, iron, gold, diamond, and netherite hoppers
    public static final Supplier<BlockEntityType<WoodenHopperBlockEntity>> WOODEN_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("wooden_hopper", () -> BlockEntityType.Builder.of(WoodenHopperBlockEntity::new, WOODEN_HOPPER.get()).build(null));

    //Menu Types
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);
    public static final Supplier<MenuType<WoodenHopperContainer>> WOODEN_HOPPER_CONTAINER = MENU_TYPES.register("wooden_hopper", () -> IMenuTypeExtension.create((pWindowID, pInventory, pData) -> new WoodenHopperContainer(pWindowID, pInventory)));


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AH_TAB = CREATIVE_MODE_TABS.register("accelerated_hoppers", () -> CreativeModeTab.builder()
            .title(Component.literal("Accelerated Hoppers"))
            .icon(() -> Items.IRON_BLOCK.getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WOODEN_HOPPER_ITEM.get());
            }).build());

}
