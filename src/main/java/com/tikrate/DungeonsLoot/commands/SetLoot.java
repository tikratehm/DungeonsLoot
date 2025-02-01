package com.tikrate.DungeonsLoot.commands;

import com.tikrate.DungeonsLoot.manager.DLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetLoot implements CommandExecutor {

    private final DLManager lootManager;

    public SetLoot(DLManager lootManager) {
        this.lootManager = lootManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "Использование команды:");
            sender.sendMessage(ChatColor.YELLOW + "/dl add <мир> <шанс> - Добавить лут в мир");
            sender.sendMessage(ChatColor.YELLOW + "/dl set <мир> <да/нет> - Включить/отключить стандартный лут");
            return true;
        }

        if (args.length != 3 || !args[0].equalsIgnoreCase("set")) {
            sender.sendMessage(ChatColor.RED + "Использование: /dl set <мир> <да/нет>");
            return true;
        }

        String world = args[1];
        String option = args[2].toLowerCase();

        if (!option.equals("да") && !option.equals("нет")) {
            sender.sendMessage(ChatColor.RED + "Пожалуйста, укажите 'да' или 'нет'.");
            return true;
        }

        boolean generateDefaultLoot = option.equals("да");
        lootManager.setDefaultLootGeneration(world, generateDefaultLoot);
        sender.sendMessage(ChatColor.GREEN + "Генерация стандартного лута для мира " + world + ": " + (generateDefaultLoot ? "включена" : "отключена") + ".");
        return true;
    }
}