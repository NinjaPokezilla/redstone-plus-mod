package com.redstoneplus.blockentity;

import com.redstoneplus.block.AdvancedRepeaterBlock;
import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AdvancedRepeaterBlockEntity extends BlockEntity {
    private int tickCounter = 0;
    private boolean wasReceivingPower = false;
    private boolean scheduledPulse = false;

    public AdvancedRepeaterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_REPEATER_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, AdvancedRepeaterBlockEntity be) {
        if (world.isClient) return;
        
        Direction facing = state.get(AdvancedRepeaterBlock.FACING);
        BlockPos backPos = pos.offset(facing.getOpposite());
        boolean isReceivingPower = world.getEmittedRedstonePower(backPos, facing.getOpposite()) > 0;
        
        int delay = state.get(AdvancedRepeaterBlock.DELAY);
        
        if (isReceivingPower && !be.wasReceivingPower) {
            be.scheduledPulse = true;
            be.tickCounter = 0;
        }
        
        if (be.scheduledPulse) {
            be.tickCounter++;
            if (be.tickCounter >= delay) {
                boolean currentPowered = state.get(AdvancedRepeaterBlock.POWERED);
                world.setBlockState(pos, state.with(AdvancedRepeaterBlock.POWERED, !currentPowered));
                if (!currentPowered) {
                    be.scheduledPulse = false;
                }
                be.tickCounter = 0;
            }
        }
        
        if (!isReceivingPower && state.get(AdvancedRepeaterBlock.POWERED)) {
            be.tickCounter++;
            if (be.tickCounter >= delay) {
                world.setBlockState(pos, state.with(AdvancedRepeaterBlock.POWERED, false));
                be.tickCounter = 0;
            }
        }
        
        be.wasReceivingPower = isReceivingPower;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("tickCounter", tickCounter);
        nbt.putBoolean("wasReceivingPower", wasReceivingPower);
        nbt.putBoolean("scheduledPulse", scheduledPulse);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        tickCounter = nbt.getInt("tickCounter");
        wasReceivingPower = nbt.getBoolean("wasReceivingPower");
        scheduledPulse = nbt.getBoolean("scheduledPulse");
    }
}
