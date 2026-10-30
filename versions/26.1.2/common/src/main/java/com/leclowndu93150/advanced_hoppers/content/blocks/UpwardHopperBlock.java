package com.leclowndu93150.advanced_hoppers.content.blocks;

import com.leclowndu93150.advanced_hoppers.Config;
import com.leclowndu93150.advanced_hoppers.content.blockentities.BaseHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.content.blockentities.UpwardHopperBlockEntity;
import com.leclowndu93150.advanced_hoppers.registries.AHRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class UpwardHopperBlock extends ConfiguredHopperBlock<UpwardHopperBlockEntity> {
    private static final VoxelShape BODY = Shapes.or(Block.box(0, 0, 0, 16, 6, 16), Block.box(4, 6, 4, 12, 12, 12));
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.UP, Shapes.or(BODY, Block.box(6, 12, 6, 10, 16, 10)));
        SHAPES.put(Direction.NORTH, Shapes.or(BODY, Block.box(6, 8, 0, 10, 12, 4)));
        SHAPES.put(Direction.SOUTH, Shapes.or(BODY, Block.box(6, 8, 12, 10, 12, 16)));
        SHAPES.put(Direction.WEST, Shapes.or(BODY, Block.box(0, 8, 6, 4, 12, 10)));
        SHAPES.put(Direction.EAST, Shapes.or(BODY, Block.box(12, 8, 6, 16, 12, 10)));
    }

    public UpwardHopperBlock(Properties properties) {
        super(properties, () -> AHRegistry.UPWARD_HOPPER_BLOCK_ENTITY_TYPE, () -> Config.upwardHopperTransferCooldown, BaseHopperBlockEntity::baseTick);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(UpwardHopperBlockEntity.outputOf(state));
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPES.get(UpwardHopperBlockEntity.outputOf(state));
    }
}
