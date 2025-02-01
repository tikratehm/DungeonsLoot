package com.tikrate.DungeonsLoot.manager;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DLManager {

    private final Map<String, List<LootItem>> worldLoot = new HashMap<>();
    private final Map<String, Boolean> defaultLootSettings = new HashMap<>();
    private final FileConfiguration config;
    private final JavaPlugin plugin;
    private final Map<String, String> messages = new HashMap<>();
    public Map<String, Boolean> getDefaultLootSettings() {
        return defaultLootSettings;
    }


    public DLManager(FileConfiguration config, JavaPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    public void addLoot(String world, Material material, double chance) {
        worldLoot.computeIfAbsent(world, k -> new ArrayList<>()).add(new LootItem(material, chance));

        saveLootForWorld(world);
    }

    public List<LootItem> getLootForWorld(String world) {
        return worldLoot.getOrDefault(world, new ArrayList<>());
    }

    public void setDefaultLootGeneration(String world, boolean enabled) {
        defaultLootSettings.put(world, enabled);

        config.set("worlds." + world + ".default-loot", enabled);
        saveConfig();
    }

    public boolean isDefaultLootEnabled(String world) {
        return defaultLootSettings.getOrDefault(world, true);
    }

    private void saveLootForWorld(String world) {
        List<Map<String, Object>> lootList = new ArrayList<>();
        if (worldLoot.containsKey(world)) {
            for (LootItem loot : worldLoot.get(world)) {
                Map<String, Object> lootData = new HashMap<>();
                lootData.put("material", loot.getMaterial().toString());
                lootData.put("chance", loot.getChance());
                lootList.add(lootData);
            }
        }
        config.set("worlds." + world + ".loot-chances", lootList);
        saveConfig();
    }

    public void saveSettings() {
        for (Map.Entry<String, Boolean> entry : defaultLootSettings.entrySet()) {
            config.set("worlds." + entry.getKey() + ".default-loot", entry.getValue());
        }

        for (String world : worldLoot.keySet()) {
            saveLootForWorld(world);
        }

        saveConfig();
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    public void loadSettings() {
        messages.put("no_permission", config.getString("messages.no_permission", "&cУ вас нет прав на выполнение этой команды."));
        messages.put("help_header", config.getString("messages.command_help_header", "&6Доступные команды для /dl:"));
        messages.put("add", config.getString("messages.command_add", "&e/dl add <мир> <шанс> &f- Добавить предмет в лут указанного мира."));
        messages.put("set", config.getString("messages.command_set", "&e/dl set <мир> <да/нет> &f- Включить или отключить стандартный лут."));
    }

    public static class LootItem {
        private final Material material;
        private final double chance;

        public LootItem(Material material, double chance) {
            this.material = material;
            this.chance = chance;
        }

        public Material getMaterial() {
            return material;
        }

        public double getChance() {
            return chance;
        }
    }

    public String getMessage(String key) {
        return plugin.getConfig().getString("messages." + key, "Сообщение не найдено в конфиге: " + key);
    }
}