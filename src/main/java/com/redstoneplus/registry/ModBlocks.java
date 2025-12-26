package com.redstoneplus.registry;

import com.redstoneplus.RedstonePlusMod;
import com.redstoneplus.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // Wireless Redstone
    public static final Block WIRELESS_TRANSMITTER = registerBlock("wireless_transmitter",
            new WirelessTransmitterBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    public static final Block WIRELESS_RECEIVER = registerBlock("wireless_receiver",
            new WirelessReceiverBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Memory Block
    public static final Block MEMORY_BLOCK = registerBlock("memory_block",
            new MemoryBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Logic Gate
    public static final Block LOGIC_GATE = registerBlock("logic_gate",
            new LogicGateBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Advanced Repeater
    public static final Block ADVANCED_REPEATER = registerBlock("advanced_repeater",
            new AdvancedRepeaterBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Player Pressure Plate
    public static final Block PLAYER_PRESSURE_PLATE = registerBlock("player_pressure_plate",
            new PlayerPressurePlateBlock(FabricBlockSettings.copyOf(Blocks.STONE_PRESSURE_PLATE).strength(0.5f)));
    
    // Filtered Hopper
    public static final Block FILTERED_HOPPER = registerBlock("filtered_hopper",
            new FilteredHopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER).strength(3.0f)));
    
    // Redstone Counter
    public static final Block REDSTONE_COUNTER = registerBlock("redstone_counter",
            new RedstoneCounterBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Randomizer
    public static final Block RANDOMIZER = registerBlock("randomizer",
            new RandomizerBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Programmable Timer
    public static final Block PROGRAMMABLE_TIMER = registerBlock("programmable_timer",
            new ProgrammableTimerBlock(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5f)));
    
    // Redstone Storage Block
    public static final Block REDSTONE_STORAGE = registerBlock("redstone_storage",
            new RedstoneStorageBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK).strength(0.5f)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(RedstonePlusMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(RedstonePlusMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void register() {
        RedstonePlusMod.LOGGER.info("Registering Blocks for " + RedstonePlusMod.MOD_ID);
    }
}
