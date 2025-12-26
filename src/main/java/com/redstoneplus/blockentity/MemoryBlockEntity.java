package com.redstoneplus.blockentity;

import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class MemoryBlockEntity extends BlockEntity {
    private boolean savedState = false;

    public MemoryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MEMORY_BLOCK_BE, pos, state);
    }

    public boolean getSavedState() {
        return savedState;
    }

    public void setSavedState(boolean state) {
        this.savedState = state;
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("savedState", savedState);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        savedState = nbt.getBoolean("savedState");
    }
}
