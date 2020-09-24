package com.github.scorchedpsyche.craftera_suite.modules.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleUtils {
    /**
     * Logs an error (red colored) to the console with the source plugin's prefix.
     * @param plugin The source plugin of the message. The prefix will be captured from plugin.yml and used as the prefix for the message
     * @param message Message to the written to the console
     */
    public void logError(JavaPlugin plugin, String message)
    {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + plugin.getDescription().getPrefix() + "] ERROR: " + message);
    }
}
