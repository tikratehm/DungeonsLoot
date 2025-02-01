package com.tikrate.DungeonsLoot.listeners;

import com.tikrate.DungeonsLoot.manager.DLManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LootListener implements Listener {

    private final DLManager lootManager;
    private final Random random = new Random();

    public LootListener(DLManager lootManager) {
        this.lootManager = lootManager;
    }

    @EventHandler
    public void LootGenerate(LootGenerateEvent event) {
        String worldName = event.getWorld().getName();

        if (!lootManager.isDefaultLootEnabled(worldName)) {
            event.getLoot().clear();
        }

        lootManager.getLootForWorld(worldName).forEach(loot -> {
            if (random.nextDouble() * 100 <= loot.getChance()) {
                event.getLoot().add(new ItemStack(loot.getMaterial(), 1));
            }
        });
    }
}