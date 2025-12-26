package com.redstoneplus.blockentity;

import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class RedstoneCounterBlockEntity extends BlockEntity {
    private boolean wasReceivingPower = false;

    public RedstoneCounterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_COUNTER_BE, pos, state);
    }

    public boolean wasReceivingPower() {
        return wasReceivingPower;
    }

    public void setWasReceivingPower(boolean value) {
        this.wasReceivingPower = value;
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("wasReceivingPower", wasReceivingPower);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        wasReceivingPower = nbt.getBoolean("wasReceivingPower");
    }
}
