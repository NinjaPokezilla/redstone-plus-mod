package com.redstoneplus.blockentity;

import com.redstoneplus.block.ProgrammableTimerBlock;
import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProgrammableTimerBlockEntity extends BlockEntity {
    private int tickCounter = 0;

    public ProgrammableTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROGRAMMABLE_TIMER_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ProgrammableTimerBlockEntity be) {
        if (world.isClient) return;
        if (!state.get(ProgrammableTimerBlock.ENABLED)) return;
        
        be.tickCounter++;
        
        int interval = state.get(ProgrammableTimerBlock.INTERVAL) * 20; // Convert seconds to ticks
        
        if (be.tickCounter >= interval) {
            boolean currentPowered = state.get(ProgrammableTimerBlock.POWERED);
            world.setBlockState(pos, state.with(ProgrammableTimerBlock.POWERED, !currentPowered));
            
            // Quick pulse - turn off after 2 ticks if just turned on
            if (!currentPowered) {
                be.tickCounter = -2; // Will turn off in 2 ticks
            } else {
                be.tickCounter = 0;
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("tickCounter", tickCounter);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        tickCounter = nbt.getInt("tickCounter");
    }
}
