package com.redstoneplus.block;

import com.redstoneplus.blockentity.RedstoneCounterBlockEntity;
import com.redstoneplus.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
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

public class RedstoneCounterBlock extends BlockWithEntity {
    public static final MapCodec<RedstoneCounterBlock> CODEC = createCodec(RedstoneCounterBlock::new);
    
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
    
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty COUNT = IntProperty.of("count", 0, 15);
    public static final IntProperty TARGET = IntProperty.of("target", 1, 15);
    
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    
    private boolean wasReceivingPower = false;

    public RedstoneCounterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(COUNT, 0)
                .with(TARGET, 8));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, COUNT, TARGET);
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
                // Reset counter
                world.setBlockState(pos, state.with(COUNT, 0));
                player.sendMessage(Text.literal("Contador resetado"), true);
            } else {
                // Change target
                int currentTarget = state.get(TARGET);
                int newTarget = currentTarget % 15 + 1;
                world.setBlockState(pos, state.with(TARGET, newTarget));
                player.sendMessage(Text.literal("Alvo: " + newTarget + " (atual: " + state.get(COUNT) + ")"), true);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            Direction facing = state.get(FACING);
            BlockPos backPos = pos.offset(facing.getOpposite());
            
            boolean isReceivingPower = world.getEmittedRedstonePower(backPos, facing.getOpposite()) > 0;
            
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof RedstoneCounterBlockEntity counterBE) {
                if (isReceivingPower && !counterBE.wasReceivingPower()) {
                    // Rising edge - increment counter
                    int currentCount = state.get(COUNT);
                    int newCount = (currentCount + 1) % 16;
                    world.setBlockState(pos, state.with(COUNT, newCount));
                }
                counterBE.setWasReceivingPower(isReceivingPower);
            }
        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        int count = state.get(COUNT);
        int target = state.get(TARGET);
        return count >= target ? 15 : count;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (direction == state.get(FACING)) {
            int count = state.get(COUNT);
            int target = state.get(TARGET);
            return count >= target ? 15 : 0;
        }
        return 0;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneCounterBlockEntity(pos, state);
    }
}
