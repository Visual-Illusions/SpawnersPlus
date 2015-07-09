package net.visualillusionsent.spawnersplus;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Copyright (C) 2015 Visual Illusions Entertainment
 * All Rights Reserved.
 *
 * @author Jason Jones (darkdiplomat)
 */
final class SpawnerEventHandler {
    private final SpawnersPlus reference;

    SpawnerEventHandler(SpawnersPlus spawnersPlus) {
        this.reference = spawnersPlus;
    }

    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        if (reference.isXPDisabled() && event.state.getBlock() instanceof BlockMobSpawner) {
            event.setExpToDrop(0); // Set the experience amount to 0
        }
    }

    @SubscribeEvent
    public void onDrops(BlockEvent.HarvestDropsEvent event) {
        if (reference.allowSpawnerDrop() && !event.isSilkTouching && event.state.getBlock() instanceof BlockMobSpawner) { // If already using silk touch, no need to drop more
            event.dropChance = 1.0f; // Make sure it always drops
            event.drops.add(new ItemStack(event.state.getBlock())); // Drop a spawner
        }
    }
}
