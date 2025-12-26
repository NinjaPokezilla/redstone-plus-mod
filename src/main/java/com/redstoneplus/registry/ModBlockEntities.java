package com.redstoneplus.registry;

import com.redstoneplus.RedstonePlusMod;
import com.redstoneplus.blockentity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<WirelessTransmitterBlockEntity> WIRELESS_TRANSMITTER_BE;
    public static BlockEntityType<WirelessReceiverBlockEntity> WIRELESS_RECEIVER_BE;
    public static BlockEntityType<MemoryBlockEntity> MEMORY_BLOCK_BE;
    public static BlockEntityType<LogicGateBlockEntity> LOGIC_GATE_BE;
    public static BlockEntityType<AdvancedRepeaterBlockEntity> ADVANCED_REPEATER_BE;
    public static BlockEntityType<FilteredHopperBlockEntity> FILTERED_HOPPER_BE;
    public static BlockEntityType<RedstoneCounterBlockEntity> REDSTONE_COUNTER_BE;
    public static BlockEntityType<RandomizerBlockEntity> RANDOMIZER_BE;
    public static BlockEntityType<ProgrammableTimerBlockEntity> PROGRAMMABLE_TIMER_BE;
    public static BlockEntityType<RedstoneStorageBlockEntity> REDSTONE_STORAGE_BE;

    public static void register() {
        WIRELESS_TRANSMITTER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "wireless_transmitter_be"),
                FabricBlockEntityTypeBuilder.create(WirelessTransmitterBlockEntity::new, ModBlocks.WIRELESS_TRANSMITTER).build()
        );

        WIRELESS_RECEIVER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "wireless_receiver_be"),
                FabricBlockEntityTypeBuilder.create(WirelessReceiverBlockEntity::new, ModBlocks.WIRELESS_RECEIVER).build()
        );

        MEMORY_BLOCK_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "memory_block_be"),
                FabricBlockEntityTypeBuilder.create(MemoryBlockEntity::new, ModBlocks.MEMORY_BLOCK).build()
        );

        LOGIC_GATE_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "logic_gate_be"),
                FabricBlockEntityTypeBuilder.create(LogicGateBlockEntity::new, ModBlocks.LOGIC_GATE).build()
        );

        ADVANCED_REPEATER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "advanced_repeater_be"),
                FabricBlockEntityTypeBuilder.create(AdvancedRepeaterBlockEntity::new, ModBlocks.ADVANCED_REPEATER).build()
        );

        FILTERED_HOPPER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "filtered_hopper_be"),
                FabricBlockEntityTypeBuilder.create(FilteredHopperBlockEntity::new, ModBlocks.FILTERED_HOPPER).build()
        );

        REDSTONE_COUNTER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "redstone_counter_be"),
                FabricBlockEntityTypeBuilder.create(RedstoneCounterBlockEntity::new, ModBlocks.REDSTONE_COUNTER).build()
        );

        RANDOMIZER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "randomizer_be"),
                FabricBlockEntityTypeBuilder.create(RandomizerBlockEntity::new, ModBlocks.RANDOMIZER).build()
        );

        PROGRAMMABLE_TIMER_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "programmable_timer_be"),
                FabricBlockEntityTypeBuilder.create(ProgrammableTimerBlockEntity::new, ModBlocks.PROGRAMMABLE_TIMER).build()
        );

        REDSTONE_STORAGE_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(RedstonePlusMod.MOD_ID, "redstone_storage_be"),
                FabricBlockEntityTypeBuilder.create(RedstoneStorageBlockEntity::new, ModBlocks.REDSTONE_STORAGE).build()
        );

        RedstonePlusMod.LOGGER.info("Registering Block Entities for " + RedstonePlusMod.MOD_ID);
    }
}
