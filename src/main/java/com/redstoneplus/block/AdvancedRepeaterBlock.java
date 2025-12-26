package com.redstoneplus.block;

import com.redstoneplus.blockentity.AdvancedRepeaterBlockEntity;
import com.redstoneplus.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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

public class AdvancedRepeaterBlock extends BlockWithEntity {
    public static final MapCodec<AdvancedRepeaterBlock> CODEC = createCodec(AdvancedRepeaterBlock::new);
    
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
    
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty DELAY = IntProperty.of("delay", 1, 40);
    public static final BooleanProperty POWERED = Properties.POWERED;
    
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    public AdvancedRepeaterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(DELAY, 1)
                .with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, DELAY, POWERED);
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
            int currentDelay = state.get(DELAY);
            int newDelay;
            
            if (player.isSneaking()) {
                newDelay = currentDelay - 5;
                if (newDelay < 1) newDelay = 40;
            } else {
                newDelay = currentDelay + 5;
                if (newDelay > 40) newDelay = 1;
            }
            
            world.setBlockState(pos, state.with(DELAY, newDelay));
            player.sendMessage(Text.literal("Delay: " + newDelay + " ticks (" + (newDelay * 50) + "ms)"), true);
        }
        return ActionResult.SUCCESS;
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
        return new AdvancedRepeaterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.ADVANCED_REPEATER_BE ? (BlockEntityTicker<T>) (world1, pos1, state1, be) -> AdvancedRepeaterBlockEntity.tick(world1, pos1, state1, (AdvancedRepeaterBlockEntity) be) : null;
    }
}
