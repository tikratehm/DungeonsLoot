package com.tikrate.DungeonsLoot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DLTab implements TabCompleter {

    private final List<String> worlds = Arrays.asList("world", "world_nether", "world_the_end");
    private final List<String> chances = Arrays.asList("20", "40", "60", "80", "100");
    private final List<String> yesNo = Arrays.asList("да", "нет");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("dungeonsloot") || command.getName().equalsIgnoreCase("dl")) {
            if (args.length == 1) {
                return PrefixFilter(args[0], Arrays.asList("add", "set"));
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")) {
                    return PrefixFilter(args[1], worlds);
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    return PrefixFilter(args[2], chances);
                } else if (args[0].equalsIgnoreCase("set")) {
                    return PrefixFilter(args[2], yesNo);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<String> PrefixFilter(String prefix, List<String> options) {
        if (prefix.isEmpty()) {
            return options;
        }
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(prefix.toLowerCase())) {
                result.add(option);
            }
        }
        return result;
    }
}