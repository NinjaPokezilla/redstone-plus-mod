package com.redstoneplus.blockentity;

import com.redstoneplus.block.WirelessTransmitterBlock;
import com.redstoneplus.registry.ModBlockEntities;
import com.redstoneplus.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WirelessTransmitterBlockEntity extends BlockEntity {
    public WirelessTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WIRELESS_TRANSMITTER_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, WirelessTransmitterBlockEntity be) {
        if (world.isClient) return;
        
        int channel = state.get(WirelessTransmitterBlock.CHANNEL);
        int power = state.get(WirelessTransmitterBlock.POWER);
        
        // Find all receivers within 64 blocks with same channel
        int range = 64;
        for (BlockPos targetPos : BlockPos.iterate(pos.add(-range, -range, -range), pos.add(range, range, range))) {
            BlockState targetState = world.getBlockState(targetPos);
            if (targetState.isOf(ModBlocks.WIRELESS_RECEIVER)) {
                int receiverChannel = targetState.get(WirelessTransmitterBlock.CHANNEL);
                if (receiverChannel == channel) {
                    int currentPower = targetState.get(WirelessTransmitterBlock.POWER);
                    if (currentPower != power) {
                        world.setBlockState(targetPos, targetState.with(WirelessTransmitterBlock.POWER, power));
                    }
                }
            }
        }
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
