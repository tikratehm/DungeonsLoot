package com.tikrate.DungeonsLoot.manager;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootSettings {

    private final List<LootEntry> lootEntries;
    private boolean defaultLoot;

    public LootSettings() {
        this.lootEntries = new ArrayList<>();
        this.defaultLoot = true;
    }

    public void addLoot(ItemStack item, double chance) {
        lootEntries.add(new LootEntry(item, chance));
    }

    public boolean DefLootEnable() {
        return defaultLoot;
    }

    public void setDefaultLoot(boolean defaultLoot) {
        this.defaultLoot = defaultLoot;
    }

    private static class LootEntry {
        private final ItemStack item;
        private final double chance;

        public LootEntry(ItemStack item, double chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public double getChance() {
            return chance;
        }
    }
}