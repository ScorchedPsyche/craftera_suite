package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.ChatColor;

public class MessageUtil
{
    public static String formattedCommand(String command)
    {
        return "" + ChatColor.YELLOW + ChatColor.BOLD + command + ChatColor.RESET;
    }

    public static String formattedCommandDescription(String description)
    {
        return "" + ChatColor.ITALIC + description + ChatColor.RESET;
    }

    public static String formattedCommandWithDescription(String command, String description)
    {
        return formattedCommand(command) + ": " + formattedCommandDescription(description);
    }

    public static String newLine()
    {
        return "\n" + ChatColor.RESET;
    }
}
