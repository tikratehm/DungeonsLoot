package com.tikrate.DungeonsLoot;

import com.tikrate.DungeonsLoot.commands.DLCommand;
import com.tikrate.DungeonsLoot.commands.DLTab;
import com.tikrate.DungeonsLoot.listeners.LootListener;
import com.tikrate.DungeonsLoot.manager.DLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DLLoot extends JavaPlugin {

    private DLManager lootManager;

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            getLogger().info("Конфигурация плагина загружена!");

            lootManager = new DLManager(getConfig(), this);
            lootManager.loadSettings();

            DLCommand command = new DLCommand(lootManager);
            getCommand("dungeonsloot").setExecutor(command);
            getCommand("dungeonsloot").setTabCompleter(new DLTab());

            getServer().getPluginManager().registerEvents(new LootListener(lootManager), this);

            getLogger().info("DungeonsLoot плагин успешно активирован!");
        } catch (Exception e) {
            getLogger().severe("Ошибка при активации плагина:");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (lootManager != null) {
            lootManager.saveSettings();
        }
        getLogger().info("DungeonsLoot плагин был отключён.");
    }
}