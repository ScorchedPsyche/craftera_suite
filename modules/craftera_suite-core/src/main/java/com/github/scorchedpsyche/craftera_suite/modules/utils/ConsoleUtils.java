package com.github.scorchedpsyche.craftera_suite.modules.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleUtils {
    public ConsoleUtils()
    {
    }

    public ConsoleUtils(String prefix)
    {
        this.prefix = prefix;
    }

    private String prefix;

    /**
     * Logs an error (red colored) to the console with the source plugin's prefix.
     * @param plugin The source plugin of the message. The prefix will be captured from plugin.yml and used as the prefix for the message
     * @param message Message to the written to the console
     */
    public void logError(JavaPlugin plugin, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "[" + plugin.getDescription().getPrefix() + "] ERROR: " + message);
    }

    public void logSuccess(String message)
    {
        String pluginPrefix = ( prefix != null ) ? prefix : "CraftEra Suite";

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.GREEN + "[" + pluginPrefix + "] SUCCESS: " + message);
    }
}
