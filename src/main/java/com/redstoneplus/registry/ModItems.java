package com.redstoneplus.registry;

import com.redstoneplus.RedstonePlusMod;
import com.redstoneplus.item.RedstoneAnalyzerItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // Redstone Analyzer Tool
    public static final Item REDSTONE_ANALYZER = registerItem("redstone_analyzer",
            new RedstoneAnalyzerItem(new FabricItemSettings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(RedstonePlusMod.MOD_ID, name), item);
    }

    public static void register() {
        RedstonePlusMod.LOGGER.info("Registering Items for " + RedstonePlusMod.MOD_ID);
        
        // Adicionar itens ao grupo criativo de Redstone
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(ModBlocks.WIRELESS_TRANSMITTER);
            entries.add(ModBlocks.WIRELESS_RECEIVER);
            entries.add(ModBlocks.MEMORY_BLOCK);
            entries.add(ModBlocks.LOGIC_GATE);
            entries.add(ModBlocks.ADVANCED_REPEATER);
            entries.add(ModBlocks.PLAYER_PRESSURE_PLATE);
            entries.add(ModBlocks.FILTERED_HOPPER);
            entries.add(ModBlocks.REDSTONE_COUNTER);
            entries.add(ModBlocks.RANDOMIZER);
            entries.add(ModBlocks.PROGRAMMABLE_TIMER);
            entries.add(ModBlocks.REDSTONE_STORAGE);
            entries.add(REDSTONE_ANALYZER);
        });
    }
}
