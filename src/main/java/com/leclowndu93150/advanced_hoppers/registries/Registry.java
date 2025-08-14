package com.leclowndu93150.advanced_hoppers.registries;

import com.leclowndu93150.advanced_hoppers.content.blockentities.*;
import com.leclowndu93150.advanced_hoppers.content.blocks.*;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilterHopperContainer;
import com.leclowndu93150.advanced_hoppers.content.inventory.OtherHopperContainer;
import com.leclowndu93150.advanced_hoppers.content.inventory.WoodenHopperContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.leclowndu93150.advanced_hoppers.AHMain.MODID;

public class Registry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<WoodenHopperBlock> WOODEN_HOPPER = BLOCKS.register("wooden_hopper", () -> new WoodenHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).sound(SoundType.WOOD)));
    public static final DeferredBlock<IronHopperBlock> IRON_HOPPER = BLOCKS.register("iron_hopper", () -> new IronHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).sound(SoundType.METAL)));
    public static final DeferredBlock<GoldenHopperBlock> GOLDEN_HOPPER = BLOCKS.register("golden_hopper", () -> new GoldenHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).sound(SoundType.METAL)));
    public static final DeferredBlock<DiamondHopperBlock> DIAMOND_HOPPER = BLOCKS.register("diamond_hopper", () -> new DiamondHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).sound(SoundType.METAL)));
    public static final DeferredBlock<FilteredHopperBlock> FILTERED_HOPPER = BLOCKS.register("filtered_hopper", () -> new FilteredHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).sound(SoundType.METAL)));
    public static final DeferredBlock<NetheriteHopperBlock> NETHERITE_HOPPER = BLOCKS.register("netherite_hopper", () -> new NetheriteHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER).sound(SoundType.NETHERITE_BLOCK)));
    public static final DeferredBlock<FlopperBlock> FLOPPER = BLOCKS.register("flopper", () -> new FlopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER)));

    public static final DeferredItem<BlockItem> WOODEN_HOPPER_ITEM = ITEMS.register("wooden_hopper", () -> new BlockItem(WOODEN_HOPPER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> IRON_HOPPER_ITEM = ITEMS.register("iron_hopper", () -> new BlockItem(IRON_HOPPER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GOLDEN_HOPPER_ITEM = ITEMS.register("golden_hopper", () -> new BlockItem(GOLDEN_HOPPER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DIAMOND_HOPPER_ITEM = ITEMS.register("diamond_hopper", () -> new BlockItem(DIAMOND_HOPPER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> FILTERED_HOPPER_ITEM = ITEMS.register("filtered_hopper", () -> new BlockItem(FILTERED_HOPPER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> NETHERITE_HOPPER_ITEM = ITEMS.register("netherite_hopper", () -> new BlockItem(NETHERITE_HOPPER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> FLOPPER_ITEM = ITEMS.register("flopper", () -> new BlockItem(FLOPPER.get(), new Item.Properties()));


    //Block entity types
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final Supplier<BlockEntityType<WoodenHopperBlockEntity>> WOODEN_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("wooden_hopper", () -> BlockEntityType.Builder.of(WoodenHopperBlockEntity::new, WOODEN_HOPPER.get()).build(null));
    public static final Supplier<BlockEntityType<IronHopperBlockEntity>> IRON_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("iron_hopper", () -> BlockEntityType.Builder.of(IronHopperBlockEntity::new, IRON_HOPPER.get()).build(null));
    public static final Supplier<BlockEntityType<GoldenHopperBlockEntity>> GOLDEN_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("golden_hopper", () -> BlockEntityType.Builder.of(GoldenHopperBlockEntity::new, GOLDEN_HOPPER.get()).build(null));
    public static final Supplier<BlockEntityType<DiamondHopperBlockEntity>> DIAMOND_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("diamond_hopper", () -> BlockEntityType.Builder.of(DiamondHopperBlockEntity::new, DIAMOND_HOPPER.get()).build(null));
    public static final Supplier<BlockEntityType<FilteredHopperBlockEntity>> FILTERED_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("filtered_hopper", () -> BlockEntityType.Builder.of(FilteredHopperBlockEntity::new, FILTERED_HOPPER.get()).build(null));
    public static final Supplier<BlockEntityType<NetheriteHopperBlockEntity>> NETHERITE_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("netherite_hopper", () -> BlockEntityType.Builder.of(NetheriteHopperBlockEntity::new, NETHERITE_HOPPER.get()).build(null));
    public static final Supplier<BlockEntityType<FlopperBlockEntity>> FLOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("flopper", () -> BlockEntityType.Builder.of(FlopperBlockEntity::new, FLOPPER.get()).build(null));

    //Menu Types
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);
    public static final Supplier<MenuType<WoodenHopperContainer>> WOODEN_HOPPER_CONTAINER = MENU_TYPES.register("wooden_hopper", () -> IMenuTypeExtension.create((pWindowID, pInventory, pData) -> new WoodenHopperContainer(pWindowID, pInventory)));
    public static final Supplier<MenuType<OtherHopperContainer>> OTHER_HOPPER_CONTAINER = MENU_TYPES.register("other_hopper", () -> IMenuTypeExtension.create((pWindowID, pInventory, pData) -> new OtherHopperContainer(pWindowID, pInventory)));
    public static final Supplier<MenuType<FilterHopperContainer>> FILTER_HOPPER_CONTAINER = MENU_TYPES.register("filter_hopper", () -> IMenuTypeExtension.create((pWindowID, pInventory, pData) -> new FilterHopperContainer(pWindowID, pInventory)));


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AH_TAB = CREATIVE_MODE_TABS.register("advanced_hoppers", () -> CreativeModeTab.builder()
            .title(Component.literal("Advanced Hoppers"))
            .icon(() -> DIAMOND_HOPPER_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WOODEN_HOPPER_ITEM.get());
                output.accept(IRON_HOPPER_ITEM.get());
                output.accept(GOLDEN_HOPPER_ITEM.get());
                output.accept(DIAMOND_HOPPER_ITEM.get());
                output.accept(FILTERED_HOPPER_ITEM.get());
                output.accept(NETHERITE_HOPPER_ITEM.get());
                output.accept(FLOPPER_ITEM.get());
            }).build());

}
