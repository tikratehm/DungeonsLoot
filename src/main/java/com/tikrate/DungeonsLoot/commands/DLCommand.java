package com.tikrate.DungeonsLoot.commands;

import com.tikrate.DungeonsLoot.manager.DLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DLCommand implements CommandExecutor {

    private final DLManager lootManager;

    public DLCommand(DLManager lootManager) {
        this.lootManager = lootManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Message(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            return AddCommand(sender, args);
        }
        if (args[0].equalsIgnoreCase("set")) {
            return SetCommand(sender, args);
        }

        sender.sendMessage(ChatColor.RED + "Неизвестная подкоманда. Доступные команды:");
        Message(sender);
        return true;
    }

    private void Message(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lootManager.getMessage("help_header")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lootManager.getMessage("add")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lootManager.getMessage("set")));
    }

    private boolean AddCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду.");
            return true;
        }

        if (!sender.hasPermission("dungeonsloot.add")) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав на использование команды /dl add.");
            return true;
        }

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
            sender.sendMessage(ChatColor.RED + "Шанс должен быть между 1 и 100.");
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType().isAir()) {
            sender.sendMessage(ChatColor.RED + "Вы должны держать предмет в руке, чтобы добавить его в лут.");
            return true;
        }

        lootManager.addLoot(world, itemInHand.getType(), chance);

        String message = lootManager.getMessage("item_chance")
                .replace("%world%", world)
                .replace("%item%", itemInHand.getType().toString())
                .replace("%chance%", String.valueOf(chance));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        return true;
    }

    private boolean SetCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду.");
            return true;
        }

        if (!sender.hasPermission("dungeonsloot.set")) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав на использование команды /dl set.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Использование: /dl set <мир> <да/нет>");
            return true;
        }

        String world = args[1];
        String option = args[2].toLowerCase();

        if (!option.equals("да") && !option.equals("нет")) {
            sender.sendMessage(ChatColor.RED + "Пожалуйста, используйте 'да' или 'нет'.");
            return true;
        }

        boolean enable = option.equals("да");
        lootManager.setDefaultLootGeneration(world, enable);

        String messageKey = enable ? "loot_enabled" : "loot_disabled";
        String message = lootManager.getMessage(messageKey)
                .replace("%world%", world);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        lootManager.saveSettings();
        return true;
    }
}