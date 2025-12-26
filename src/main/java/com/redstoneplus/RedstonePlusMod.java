package com.redstoneplus;

import com.redstoneplus.registry.ModBlocks;
import com.redstoneplus.registry.ModItems;
import com.redstoneplus.registry.ModBlockEntities;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedstonePlusMod implements ModInitializer {
    public static final String MOD_ID = "redstone_plus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Redstone Plus...");
        
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        
        LOGGER.info("Redstone Plus loaded successfully!");
    }
}
