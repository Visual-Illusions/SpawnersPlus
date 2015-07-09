package net.visualillusionsent.spawnersplus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

/**
 * Copyright (c) 2015 Visual Illusions Entertainment
 * All Rights Reserved
 *
 * @author Jason Jones (darkdiplomat)
 */
@Mod(modid = "spawnersplus", useMetadata = true, canBeDeactivated = true)
public class SpawnersPlus {
    private static Configuration config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        config.get(CATEGORY_GENERAL, "disableXPDrops", true);

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpawnerEventHandler());
    }

    static boolean isXPDisabled() {
        return config.get("disableXPDrops", CATEGORY_GENERAL, true).getBoolean();
    }
}
