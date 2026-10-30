package com.leclowndu93150.advanced_hoppers.registries;

import com.leclowndu93150.advanced_hoppers.AHMain;
import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.CopperHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.DiamondHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredFlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FilteredHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.FlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.GoldenHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.IronHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.LinkedHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.NetheriteHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.PlacingFlopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.PulseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.SplitterHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.StackHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.UpwardHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.VacuumHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.VoidHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.WoodenHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.XpHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blocks.ConfiguredHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.CopperHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.DiamondHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.FilteredHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.FlopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.GoldenHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.IronHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.NetheriteHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.PulseHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.UpwardHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.WoodenHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.blocks.XpHopperBlock;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilterHopperContainer;
import com.leclowndu93150.advanced_hoppers.content.inventory.FilteredFlopperMenu;
import com.leclowndu93150.advanced_hoppers.content.inventory.LinkedHopperMenu;
import com.leclowndu93150.advanced_hoppers.content.inventory.OtherHopperContainer;
import com.leclowndu93150.advanced_hoppers.content.inventory.PulseHopperMenu;
import com.leclowndu93150.advanced_hoppers.content.inventory.VacuumHopperMenu;
import com.leclowndu93150.advanced_hoppers.content.inventory.VoidHopperMenu;
import com.leclowndu93150.advanced_hoppers.content.inventory.WoodenHopperContainer;
import com.leclowndu93150.advanced_hoppers.content.inventory.XpHopperMenu;
import com.leclowndu93150.advanced_hoppers.content.items.HopperLinkerItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class AHRegistry {
    private static final String[] COPPER_NAMES = {"copper_hopper", "exposed_copper_hopper", "weathered_copper_hopper", "oxidized_copper_hopper"};

    public static WoodenHopperBlock WOODEN_HOPPER;
    public static IronHopperBlock IRON_HOPPER;
    public static GoldenHopperBlock GOLDEN_HOPPER;
    public static DiamondHopperBlock DIAMOND_HOPPER;
    public static FilteredHopperBlock FILTERED_HOPPER;
    public static NetheriteHopperBlock NETHERITE_HOPPER;
    public static FlopperBlock FLOPPER;
    public static final CopperHopperBlock[] COPPER_HOPPERS = new CopperHopperBlock[4];
    public static final CopperHopperBlock[] WAXED_COPPER_HOPPERS = new CopperHopperBlock[4];
    public static ConfiguredHopperBlock<StackHopperBlockEntity> STACK_HOPPER;
    public static ConfiguredHopperBlock<VoidHopperBlockEntity> VOID_HOPPER;
    public static ConfiguredHopperBlock<SplitterHopperBlockEntity> SPLITTER_HOPPER;
    public static ConfiguredHopperBlock<VacuumHopperBlockEntity> VACUUM_HOPPER;
    public static PulseHopperBlock PULSE_HOPPER;
    public static UpwardHopperBlock UPWARD_HOPPER;
    public static ConfiguredHopperBlock<LinkedHopperBlockEntity> LINKED_HOPPER;
    public static XpHopperBlock XP_HOPPER;
    public static FlopperBlock IRON_FLOPPER;
    public static FlopperBlock GOLDEN_FLOPPER;
    public static FlopperBlock DIAMOND_FLOPPER;
    public static FlopperBlock FILTERED_FLOPPER;
    public static FlopperBlock PLACING_FLOPPER;

    public static BlockItem WOODEN_HOPPER_ITEM;
    public static BlockItem IRON_HOPPER_ITEM;
    public static BlockItem GOLDEN_HOPPER_ITEM;
    public static BlockItem DIAMOND_HOPPER_ITEM;
    public static BlockItem FILTERED_HOPPER_ITEM;
    public static BlockItem NETHERITE_HOPPER_ITEM;
    public static BlockItem FLOPPER_ITEM;
    public static HopperLinkerItem HOPPER_LINKER;

    public static BlockEntityType<WoodenHopperBlockEntity> WOODEN_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<IronHopperBlockEntity> IRON_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<GoldenHopperBlockEntity> GOLDEN_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<DiamondHopperBlockEntity> DIAMOND_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<FilteredHopperBlockEntity> FILTERED_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<NetheriteHopperBlockEntity> NETHERITE_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<FlopperBlockEntity> FLOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<CopperHopperBlockEntity> COPPER_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<StackHopperBlockEntity> STACK_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<VoidHopperBlockEntity> VOID_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<SplitterHopperBlockEntity> SPLITTER_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<VacuumHopperBlockEntity> VACUUM_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<PulseHopperBlockEntity> PULSE_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<UpwardHopperBlockEntity> UPWARD_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<LinkedHopperBlockEntity> LINKED_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<XpHopperBlockEntity> XP_HOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<FlopperBlockEntity> IRON_FLOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<FlopperBlockEntity> GOLDEN_FLOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<FlopperBlockEntity> DIAMOND_FLOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<FilteredFlopperBlockEntity> FILTERED_FLOPPER_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<PlacingFlopperBlockEntity> PLACING_FLOPPER_BLOCK_ENTITY_TYPE;

    public static MenuType<WoodenHopperContainer> WOODEN_HOPPER_CONTAINER;
    public static MenuType<OtherHopperContainer> OTHER_HOPPER_CONTAINER;
    public static MenuType<FilterHopperContainer> FILTER_HOPPER_CONTAINER;
    public static MenuType<VoidHopperMenu> VOID_HOPPER_MENU;
    public static MenuType<VacuumHopperMenu> VACUUM_HOPPER_MENU;
    public static MenuType<PulseHopperMenu> PULSE_HOPPER_MENU;
    public static MenuType<LinkedHopperMenu> LINKED_HOPPER_MENU;
    public static MenuType<XpHopperMenu> XP_HOPPER_MENU;
    public static MenuType<FilteredFlopperMenu> FILTERED_FLOPPER_MENU;

    public static CreativeModeTab AH_TAB;

    private static final Map<String, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Item> ITEMS = new LinkedHashMap<>();
    private static final Map<String, BlockEntityType<?>> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

    public static void init(Supplier<CreativeModeTab.Builder> tabBuilder) {
        WOODEN_HOPPER = block("wooden_hopper", new WoodenHopperBlock(blockProperties("wooden_hopper").sound(SoundType.WOOD)));
        IRON_HOPPER = block("iron_hopper", new IronHopperBlock(blockProperties("iron_hopper").sound(SoundType.METAL)));
        GOLDEN_HOPPER = block("golden_hopper", new GoldenHopperBlock(blockProperties("golden_hopper").sound(SoundType.METAL)));
        DIAMOND_HOPPER = block("diamond_hopper", new DiamondHopperBlock(blockProperties("diamond_hopper").sound(SoundType.METAL)));
        FILTERED_HOPPER = block("filtered_hopper", new FilteredHopperBlock(blockProperties("filtered_hopper").sound(SoundType.METAL)));
        NETHERITE_HOPPER = block("netherite_hopper", new NetheriteHopperBlock(blockProperties("netherite_hopper").sound(SoundType.NETHERITE_BLOCK)));
        FLOPPER = block("flopper", new FlopperBlock(blockProperties("flopper")));
        for (WeatheringCopper.WeatherState age : WeatheringCopper.WeatherState.values()) {
            String name = COPPER_NAMES[age.ordinal()];
            COPPER_HOPPERS[age.ordinal()] = block(name, new CopperHopperBlock(blockProperties(name).sound(SoundType.COPPER), age, false));
        }
        for (WeatheringCopper.WeatherState age : WeatheringCopper.WeatherState.values()) {
            String name = "waxed_" + COPPER_NAMES[age.ordinal()];
            WAXED_COPPER_HOPPERS[age.ordinal()] = block(name, new CopperHopperBlock(blockProperties(name).sound(SoundType.COPPER), age, true));
        }
        STACK_HOPPER = block("stack_hopper", new ConfiguredHopperBlock<>(blockProperties("stack_hopper").sound(SoundType.METAL),
                () -> STACK_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.stackHopperTransferCooldown, BaseHopperBlockEntity::baseTick));
        VOID_HOPPER = block("void_hopper", new ConfiguredHopperBlock<>(blockProperties("void_hopper").sound(SoundType.STONE),
                () -> VOID_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.voidHopperTransferCooldown, VoidHopperBlockEntity::tick));
        SPLITTER_HOPPER = block("splitter_hopper", new ConfiguredHopperBlock<>(blockProperties("splitter_hopper").sound(SoundType.STONE),
                () -> SPLITTER_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.splitterHopperTransferCooldown, BaseHopperBlockEntity::baseTick));
        VACUUM_HOPPER = block("vacuum_hopper", new ConfiguredHopperBlock<>(blockProperties("vacuum_hopper").sound(SoundType.TUFF_BRICKS),
                () -> VACUUM_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.vacuumHopperTransferCooldown, VacuumHopperBlockEntity::tick));
        PULSE_HOPPER = block("pulse_hopper", new PulseHopperBlock(blockProperties("pulse_hopper").sound(SoundType.STONE)));
        UPWARD_HOPPER = block("upward_hopper", new UpwardHopperBlock(blockProperties("upward_hopper").sound(SoundType.STONE)));
        LINKED_HOPPER = block("linked_hopper", new ConfiguredHopperBlock<>(blockProperties("linked_hopper").sound(SoundType.STONE),
                () -> LINKED_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.linkedHopperTransferCooldown, BaseHopperBlockEntity::baseTick));
        XP_HOPPER = block("xp_hopper", new XpHopperBlock(blockProperties("xp_hopper").sound(SoundType.SCULK_CATALYST)));
        IRON_FLOPPER = block("iron_flopper", new FlopperBlock(blockProperties("iron_flopper").sound(SoundType.METAL),
                () -> IRON_FLOPPER_BLOCK_ENTITY_TYPE, () -> Config.ironFlopperCapacity, () -> Config.ironFlopperIORate));
        GOLDEN_FLOPPER = block("golden_flopper", new FlopperBlock(blockProperties("golden_flopper").sound(SoundType.METAL),
                () -> GOLDEN_FLOPPER_BLOCK_ENTITY_TYPE, () -> Config.goldenFlopperCapacity, () -> Config.goldenFlopperIORate));
        DIAMOND_FLOPPER = block("diamond_flopper", new FlopperBlock(blockProperties("diamond_flopper").sound(SoundType.METAL),
                () -> DIAMOND_FLOPPER_BLOCK_ENTITY_TYPE, () -> Config.diamondFlopperCapacity, () -> Config.diamondFlopperIORate));
        FILTERED_FLOPPER = block("filtered_flopper", new FlopperBlock(blockProperties("filtered_flopper").sound(SoundType.COPPER),
                () -> FILTERED_FLOPPER_BLOCK_ENTITY_TYPE, () -> Config.flopperCapacity, () -> Config.flopperIORate));
        PLACING_FLOPPER = block("placing_flopper", new FlopperBlock(blockProperties("placing_flopper").sound(SoundType.COPPER),
                () -> PLACING_FLOPPER_BLOCK_ENTITY_TYPE, () -> Config.flopperCapacity, () -> Config.flopperIORate));

        WOODEN_HOPPER_ITEM = (BlockItem) ITEMS.get("wooden_hopper");
        IRON_HOPPER_ITEM = (BlockItem) ITEMS.get("iron_hopper");
        GOLDEN_HOPPER_ITEM = (BlockItem) ITEMS.get("golden_hopper");
        DIAMOND_HOPPER_ITEM = (BlockItem) ITEMS.get("diamond_hopper");
        FILTERED_HOPPER_ITEM = (BlockItem) ITEMS.get("filtered_hopper");
        NETHERITE_HOPPER_ITEM = (BlockItem) ITEMS.get("netherite_hopper");
        FLOPPER_ITEM = (BlockItem) ITEMS.get("flopper");
        HOPPER_LINKER = new HopperLinkerItem(new Item.Properties().stacksTo(1));
        ITEMS.put("hopper_linker", HOPPER_LINKER);

        WOODEN_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("wooden_hopper", BlockEntityType.Builder.of(WoodenHopperBlockEntity::new, WOODEN_HOPPER).build(null));
        IRON_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("iron_hopper", BlockEntityType.Builder.of(IronHopperBlockEntity::new, IRON_HOPPER).build(null));
        GOLDEN_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("golden_hopper", BlockEntityType.Builder.of(GoldenHopperBlockEntity::new, GOLDEN_HOPPER).build(null));
        DIAMOND_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("diamond_hopper", BlockEntityType.Builder.of(DiamondHopperBlockEntity::new, DIAMOND_HOPPER).build(null));
        FILTERED_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("filtered_hopper", BlockEntityType.Builder.of(FilteredHopperBlockEntity::new, FILTERED_HOPPER).build(null));
        NETHERITE_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("netherite_hopper", BlockEntityType.Builder.of(NetheriteHopperBlockEntity::new, NETHERITE_HOPPER).build(null));
        FLOPPER_BLOCK_ENTITY_TYPE = blockEntity("flopper", BlockEntityType.Builder.of(FlopperBlockEntity::new, FLOPPER).build(null));
        Set<Block> copperBlocks = new HashSet<>(List.of(COPPER_HOPPERS));
        copperBlocks.addAll(List.of(WAXED_COPPER_HOPPERS));
        COPPER_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("copper_hopper", BlockEntityType.Builder.of(CopperHopperBlockEntity::new, copperBlocks.toArray(new Block[0])).build(null));
        STACK_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("stack_hopper", BlockEntityType.Builder.of(StackHopperBlockEntity::new, STACK_HOPPER).build(null));
        VOID_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("void_hopper", BlockEntityType.Builder.of(VoidHopperBlockEntity::new, VOID_HOPPER).build(null));
        SPLITTER_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("splitter_hopper", BlockEntityType.Builder.of(SplitterHopperBlockEntity::new, SPLITTER_HOPPER).build(null));
        VACUUM_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("vacuum_hopper", BlockEntityType.Builder.of(VacuumHopperBlockEntity::new, VACUUM_HOPPER).build(null));
        PULSE_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("pulse_hopper", BlockEntityType.Builder.of(PulseHopperBlockEntity::new, PULSE_HOPPER).build(null));
        UPWARD_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("upward_hopper", BlockEntityType.Builder.of(UpwardHopperBlockEntity::new, UPWARD_HOPPER).build(null));
        LINKED_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("linked_hopper", BlockEntityType.Builder.of(LinkedHopperBlockEntity::new, LINKED_HOPPER).build(null));
        XP_HOPPER_BLOCK_ENTITY_TYPE = blockEntity("xp_hopper", BlockEntityType.Builder.of(XpHopperBlockEntity::new, XP_HOPPER).build(null));
        IRON_FLOPPER_BLOCK_ENTITY_TYPE = blockEntity("iron_flopper", BlockEntityType.Builder.of((pos, state) -> new FlopperBlockEntity(
                IRON_FLOPPER_BLOCK_ENTITY_TYPE, pos, state, Config.ironFlopperCapacity, () -> Config.ironFlopperIORate), IRON_FLOPPER).build(null));
        GOLDEN_FLOPPER_BLOCK_ENTITY_TYPE = blockEntity("golden_flopper", BlockEntityType.Builder.of((pos, state) -> new FlopperBlockEntity(
                GOLDEN_FLOPPER_BLOCK_ENTITY_TYPE, pos, state, Config.goldenFlopperCapacity, () -> Config.goldenFlopperIORate), GOLDEN_FLOPPER).build(null));
        DIAMOND_FLOPPER_BLOCK_ENTITY_TYPE = blockEntity("diamond_flopper", BlockEntityType.Builder.of((pos, state) -> new FlopperBlockEntity(
                DIAMOND_FLOPPER_BLOCK_ENTITY_TYPE, pos, state, Config.diamondFlopperCapacity, () -> Config.diamondFlopperIORate), DIAMOND_FLOPPER).build(null));
        FILTERED_FLOPPER_BLOCK_ENTITY_TYPE = blockEntity("filtered_flopper", BlockEntityType.Builder.of(FilteredFlopperBlockEntity::new, FILTERED_FLOPPER).build(null));
        PLACING_FLOPPER_BLOCK_ENTITY_TYPE = blockEntity("placing_flopper", BlockEntityType.Builder.of(PlacingFlopperBlockEntity::new, PLACING_FLOPPER).build(null));

        WOODEN_HOPPER_CONTAINER = new MenuType<>(WoodenHopperContainer::new, FeatureFlags.VANILLA_SET);
        OTHER_HOPPER_CONTAINER = new MenuType<>(OtherHopperContainer::new, FeatureFlags.VANILLA_SET);
        FILTER_HOPPER_CONTAINER = new MenuType<>(FilterHopperContainer::new, FeatureFlags.VANILLA_SET);
        VOID_HOPPER_MENU = new MenuType<>(VoidHopperMenu::new, FeatureFlags.VANILLA_SET);
        VACUUM_HOPPER_MENU = new MenuType<>(VacuumHopperMenu::new, FeatureFlags.VANILLA_SET);
        PULSE_HOPPER_MENU = new MenuType<>(PulseHopperMenu::new, FeatureFlags.VANILLA_SET);
        LINKED_HOPPER_MENU = new MenuType<>(LinkedHopperMenu::new, FeatureFlags.VANILLA_SET);
        XP_HOPPER_MENU = new MenuType<>(XpHopperMenu::new, FeatureFlags.VANILLA_SET);
        FILTERED_FLOPPER_MENU = new MenuType<>(FilteredFlopperMenu::new, FeatureFlags.VANILLA_SET);

        AH_TAB = tabBuilder.get()
                .title(Component.literal("Advanced Hoppers"))
                .icon(() -> DIAMOND_HOPPER_ITEM.getDefaultInstance())
                .displayItems((parameters, output) -> ITEMS.values().forEach(output::accept))
                .build();
    }

    private static <B extends Block> B block(String name, B block) {
        BLOCKS.put(name, block);
        ITEMS.put(name, new BlockItem(block, new Item.Properties()));
        return block;
    }

    private static <T extends BlockEntityType<?>> T blockEntity(String name, T type) {
        BLOCK_ENTITY_TYPES.put(name, type);
        return type;
    }

    private static BlockBehaviour.Properties blockProperties(String name) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER);
    }

    public static List<BlockEntityType<? extends BaseHopperBlockEntity>> baseHopperTypes() {
        List<BlockEntityType<? extends BaseHopperBlockEntity>> types = new ArrayList<>();
        types.add(WOODEN_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(IRON_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(GOLDEN_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(DIAMOND_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(NETHERITE_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(COPPER_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(STACK_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(SPLITTER_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(VACUUM_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(PULSE_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(UPWARD_HOPPER_BLOCK_ENTITY_TYPE);
        types.add(LINKED_HOPPER_BLOCK_ENTITY_TYPE);
        return types;
    }

    public static List<BlockEntityType<? extends FlopperBlockEntity>> flopperTypes() {
        return List.of(FLOPPER_BLOCK_ENTITY_TYPE, IRON_FLOPPER_BLOCK_ENTITY_TYPE, GOLDEN_FLOPPER_BLOCK_ENTITY_TYPE,
                DIAMOND_FLOPPER_BLOCK_ENTITY_TYPE, FILTERED_FLOPPER_BLOCK_ENTITY_TYPE, PLACING_FLOPPER_BLOCK_ENTITY_TYPE);
    }

    public static void registerBlocks(BiConsumer<ResourceLocation, Block> registrar) {
        BLOCKS.forEach((name, block) -> registrar.accept(AHMain.id(name), block));
    }

    public static void registerItems(BiConsumer<ResourceLocation, Item> registrar) {
        ITEMS.forEach((name, item) -> registrar.accept(AHMain.id(name), item));
    }

    public static void registerBlockEntityTypes(BiConsumer<ResourceLocation, BlockEntityType<?>> registrar) {
        BLOCK_ENTITY_TYPES.forEach((name, type) -> registrar.accept(AHMain.id(name), type));
    }

    public static void registerMenuTypes(BiConsumer<ResourceLocation, MenuType<?>> registrar) {
        registrar.accept(AHMain.id("wooden_hopper"), WOODEN_HOPPER_CONTAINER);
        registrar.accept(AHMain.id("other_hopper"), OTHER_HOPPER_CONTAINER);
        registrar.accept(AHMain.id("filter_hopper"), FILTER_HOPPER_CONTAINER);
        registrar.accept(AHMain.id("void_hopper"), VOID_HOPPER_MENU);
        registrar.accept(AHMain.id("vacuum_hopper"), VACUUM_HOPPER_MENU);
        registrar.accept(AHMain.id("pulse_hopper"), PULSE_HOPPER_MENU);
        registrar.accept(AHMain.id("linked_hopper"), LINKED_HOPPER_MENU);
        registrar.accept(AHMain.id("xp_hopper"), XP_HOPPER_MENU);
        registrar.accept(AHMain.id("filtered_flopper"), FILTERED_FLOPPER_MENU);
    }

    public static void registerCreativeTabs(BiConsumer<ResourceLocation, CreativeModeTab> registrar) {
        registrar.accept(AHMain.id("advanced_hoppers"), AH_TAB);
    }
}
