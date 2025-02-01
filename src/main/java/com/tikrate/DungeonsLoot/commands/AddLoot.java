package com.tikrate.DungeonsLoot.commands;

import com.tikrate.DungeonsLoot.manager.DLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddLoot implements CommandExecutor {

    private final DLManager lootManager;

    public AddLoot(DLManager lootManager) {
        this.lootManager = lootManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "Использование команды:");
            sender.sendMessage(ChatColor.YELLOW + "/dl add <мир> <шанс> - Добавить лут в мир");
            sender.sendMessage(ChatColor.YELLOW + "/dl set <мир> <да/нет> - Включить/отключить стандартный лут");
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {

            if (args.length != 3) {
                sender.sendMessage(ChatColor.RED + "Использование: /dl add <мир> <шанс>");
                return true;
            }

            Player player = (Player) sender;
            String world = args[1];
            double chance;

            try {
                chance = Double.parseDouble(args[2].replace("%", ""));
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Шанс должен быть числом!");
                return true;
            }

            if (chance < 1 || chance > 100) {
                sender.sendMessage(ChatColor.RED + "Шанс должен быть от 1 до 100.");
                return true;
            }

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand == null || itemInHand.getType().isAir()) {
                sender.sendMessage(ChatColor.RED + "Вы должны держать предмет в руке, чтобы добавить его в лут.");
                return true;
            }

            lootManager.addLoot(world, itemInHand.getType(), chance);
            player.sendMessage(ChatColor.GREEN + "Предмет успешно добавлен в лут мира " + world + " с шансом " + chance + "%.");
            return true;

        } else if (args[0].equalsIgnoreCase("set")) {

            return false;

        } else {
            sender.sendMessage(ChatColor.RED + "Неизвестная команда. Введите /dl для справки.");
            return true;
        }
    }
}