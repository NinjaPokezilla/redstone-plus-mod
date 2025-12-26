package com.redstoneplus.blockentity;

import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class RedstoneStorageBlockEntity extends BlockEntity {
    public RedstoneStorageBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_STORAGE_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
