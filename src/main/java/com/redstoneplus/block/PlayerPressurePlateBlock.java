package com.redstoneplus.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PlayerPressurePlateBlock extends Block {
    public static final BooleanProperty POWERED = Properties.POWERED;
    
    protected static final VoxelShape PRESSED_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);
    protected static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);

    public PlayerPressurePlateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(POWERED) ? PRESSED_SHAPE : DEFAULT_SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            boolean hasPlayer = entity instanceof PlayerEntity;
            boolean isPowered = state.get(POWERED);
            
            if (hasPlayer && !isPowered) {
                world.setBlockState(pos, state.with(POWERED, true));
                world.updateNeighborsAlways(pos, this);
                world.updateNeighborsAlways(pos.down(), this);
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && !state.isOf(newState.getBlock())) {
            if (state.get(POWERED)) {
                world.updateNeighborsAlways(pos, this);
                world.updateNeighborsAlways(pos.down(), this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void scheduledTick(BlockState state, net.minecraft.server.world.ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (state.get(POWERED)) {
            boolean hasPlayer = !world.getEntitiesByClass(PlayerEntity.class, 
                    state.getOutlineShape(world, pos).getBoundingBox().offset(pos), 
                    player -> true).isEmpty();
            
            if (!hasPlayer) {
                world.setBlockState(pos, state.with(POWERED, false));
                world.updateNeighborsAlways(pos, this);
                world.updateNeighborsAlways(pos.down(), this);
            } else {
                world.scheduleBlockTick(pos, this, 10);
            }
        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && direction == Direction.UP ? 15 : 0;
    }
}
