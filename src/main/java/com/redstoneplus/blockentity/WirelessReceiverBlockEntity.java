package com.redstoneplus.blockentity;

import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WirelessReceiverBlockEntity extends BlockEntity {
    public WirelessReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WIRELESS_RECEIVER_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, WirelessReceiverBlockEntity be) {
        // Receiver is updated by the transmitter
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
