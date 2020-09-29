package com.github.scorchedpsyche.craftera_suite.modules.utils;

import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ConsoleUtils {
    public ConsoleUtils()
    {
    }

    public ConsoleUtils(String prefix)
    {
        this.prefix = prefix;
    }

    private StringUtils stringUtils;
    private String prefix;

    /**
     * Logs an error (red colored) to the console with the source plugin's prefix.
     * @param plugin The source plugin of the message. The prefix will be captured from plugin.yml and used as the prefix for the message
     * @param message Message to the written to the console
     */
    public void logError(String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "[" + getPrefix() + "] ERROR: " + message);
    }

    public void logSuccess(String message)
    {

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.GREEN + "[" + getPrefix() + "] SUCCESS: " + message);
    }

    public String getPrefix()
    {
        return ( prefix != null && !stringUtils.isEmpty(prefix) ) ? prefix : "CraftEra Suite";
    }
}
