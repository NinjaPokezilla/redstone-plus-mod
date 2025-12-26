package com.redstoneplus.block;

import com.redstoneplus.blockentity.ProgrammableTimerBlockEntity;
import com.redstoneplus.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProgrammableTimerBlock extends BlockWithEntity {
    public static final MapCodec<ProgrammableTimerBlock> CODEC = createCodec(ProgrammableTimerBlock::new);
    
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
    
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty ENABLED = Properties.ENABLED;
    public static final IntProperty INTERVAL = IntProperty.of("interval", 1, 15); // Represents 1-15 seconds
    
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    public ProgrammableTimerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(POWERED, false)
                .with(ENABLED, true)
                .with(INTERVAL, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, ENABLED, INTERVAL);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.isSneaking()) {
                // Toggle enabled
                boolean newEnabled = !state.get(ENABLED);
                world.setBlockState(pos, state.with(ENABLED, newEnabled));
                player.sendMessage(Text.literal("Timer: " + (newEnabled ? "LIGADO" : "DESLIGADO")), true);
            } else {
                // Change interval
                int currentInterval = state.get(INTERVAL);
                int newInterval = currentInterval % 15 + 1;
                world.setBlockState(pos, state.with(INTERVAL, newInterval));
                player.sendMessage(Text.literal("Intervalo: " + newInterval + "s"), true);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean shouldDisable = world.isReceivingRedstonePower(pos);
            if (shouldDisable && state.get(ENABLED)) {
                world.setBlockState(pos, state.with(ENABLED, false));
            } else if (!shouldDisable && !state.get(ENABLED)) {
                world.setBlockState(pos, state.with(ENABLED, true));
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
        return direction == state.get(FACING) && state.get(POWERED) ? 15 : 0;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ProgrammableTimerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.PROGRAMMABLE_TIMER_BE ? (BlockEntityTicker<T>) (world1, pos1, state1, be) -> ProgrammableTimerBlockEntity.tick(world1, pos1, state1, (ProgrammableTimerBlockEntity) be) : null;
    }
}
