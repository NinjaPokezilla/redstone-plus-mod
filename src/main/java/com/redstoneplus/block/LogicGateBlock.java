package com.redstoneplus.block;

import com.redstoneplus.blockentity.LogicGateBlockEntity;
import com.redstoneplus.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.BooleanProperty;
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

public class LogicGateBlock extends BlockWithEntity {
    public static final MapCodec<LogicGateBlock> CODEC = createCodec(LogicGateBlock::new);
    
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
    
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty MODE = IntProperty.of("mode", 0, 5); // 0=AND, 1=OR, 2=XOR, 3=NOT, 4=NAND, 5=NOR
    public static final BooleanProperty POWERED = Properties.POWERED;
    
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    
    private static final String[] MODE_NAMES = {"AND", "OR", "XOR", "NOT", "NAND", "NOR"};

    public LogicGateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(MODE, 0)
                .with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, POWERED);
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
            int currentMode = state.get(MODE);
            int newMode = (currentMode + 1) % 6;
            world.setBlockState(pos, state.with(MODE, newMode));
            player.sendMessage(Text.literal("Porta: " + MODE_NAMES[newMode]), true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            updateOutput(world, pos, state);
        }
    }

    private void updateOutput(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        int mode = state.get(MODE);
        
        // Get inputs from left, right, and back
        Direction left = facing.rotateYClockwise();
        Direction right = facing.rotateYCounterclockwise();
        Direction back = facing.getOpposite();
        
        boolean inputLeft = world.getEmittedRedstonePower(pos.offset(left), left) > 0;
        boolean inputRight = world.getEmittedRedstonePower(pos.offset(right), right) > 0;
        boolean inputBack = world.getEmittedRedstonePower(pos.offset(back), back) > 0;
        
        boolean output = calculateOutput(mode, inputLeft, inputRight, inputBack);
        
        if (state.get(POWERED) != output) {
            world.setBlockState(pos, state.with(POWERED, output));
        }
    }

    private boolean calculateOutput(int mode, boolean a, boolean b, boolean c) {
        return switch (mode) {
            case 0 -> a && b && c;       // AND
            case 1 -> a || b || c;       // OR
            case 2 -> (a ^ b) ^ c;       // XOR
            case 3 -> !a;                // NOT (only uses back input)
            case 4 -> !(a && b && c);    // NAND
            case 5 -> !(a || b || c);    // NOR
            default -> false;
        };
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
        return new LogicGateBlockEntity(pos, state);
    }
}
