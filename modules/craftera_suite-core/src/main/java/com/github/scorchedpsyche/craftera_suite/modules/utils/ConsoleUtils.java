package com.github.scorchedpsyche.craftera_suite.modules.utils;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ConsoleUtils {
    private static final String defaultPrefix = "CraftEra Suite";
    /**
     * Logs an error (red colored) to the console with the source plugin's prefix.
     * * @param message Message to the written to the console
     */
    public static void logError(String message)
    {
        logError(defaultPrefix, message);
    }

    /**
     * Logs an error (red colored) to the console with the source plugin's prefix.
     * * @param message Message to the written to the console
     */
    public static void logError(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "[" + validCustomPrefixOrDefault(pluginPrefixName) + "] ERROR: " + message);
    }

    public static void logSuccess(String message)
    {
        logSuccess(defaultPrefix, message);
    }

    public static void logSuccess(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.GREEN + "[" + validCustomPrefixOrDefault(pluginPrefixName) + "] SUCCESS: " + message);
    }

    public static void logMessage(String message)
    {
        logMessage(defaultPrefix, message);
    }

    public static void logMessage(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage("[" + validCustomPrefixOrDefault(pluginPrefixName) + "] " + message);
    }

    private static String validCustomPrefixOrDefault(String pluginPrefixName)
    {
        return ( StringUtils.isNullOrEmpty(pluginPrefixName) ) ? defaultPrefix : pluginPrefixName;
    }
}
