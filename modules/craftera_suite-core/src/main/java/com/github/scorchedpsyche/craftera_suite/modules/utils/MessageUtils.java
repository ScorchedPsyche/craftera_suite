package com.github.scorchedpsyche.craftera_suite.modules.utils;

import org.bukkit.ChatColor;

public class MessageUtils
{
    public String formattedCommand(String command)
    {
        return "" + ChatColor.YELLOW + ChatColor.BOLD + command + ChatColor.RESET;
    }

    public String formattedCommandDescription(String description)
    {
        return "" + ChatColor.ITALIC + description + ChatColor.RESET;
    }

    public String formattedCommandWithDescription(String command, String description)
    {
        return formattedCommand(command) + ": " + formattedCommandDescription(description);
    }

    public String newLine()
    {
        return "\n" + ChatColor.RESET;
    }
}
