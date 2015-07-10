package net.visualillusionsent.spawnersplus;

import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

/**
 * Copyright (c) 2015 Visual Illusions Entertainment
 * All Rights Reserved
 *
 * @author Jason Jones (darkdiplomat)
 */
@Mod(modid = "spawnersplus", useMetadata = true, canBeDeactivated = true)
public class SpawnersPlus {
    private boolean xpDisabled, dropsEnabled;
    private ArrayList<String> allowedMobSet = Lists.newArrayList();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        xpDisabled = config.get(CATEGORY_GENERAL, "disableXPDrops", true).getBoolean();
        dropsEnabled = config.get(CATEGORY_GENERAL, "allowSpawnerDrop", true).getBoolean();
        String[] mobSetTemp = config.get(CATEGORY_GENERAL, "allowedMobs", new String[]{ "Chicken", "Cow", "Creeper", "Enderman", "Ozelot", "Pig", "Sheep", "Wolf", "Zombie" }).getStringList();
        for (String mob : mobSetTemp) {
            allowedMobSet.add(WordUtils.capitalizeFully(mob)); // Fix case
        }

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpawnerEventHandler(this));
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new SetSpawnerCommand(this));
    }

    boolean isXPDisabled() {
        return xpDisabled;
    }

    boolean allowSpawnerDrop() {
        return dropsEnabled;
    }

    List allowedMobSet() {
        return allowedMobSet;
    }
}
