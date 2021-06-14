package com.github.scorchedpsyche.craftera_suite.modules.util;

import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ConsoleUtil {
    private static final String defaultPrefix = "CraftEra Suite";

    /**
     * Logs an error (red colored) to the console with default prefix (CraftEra Suite).
     * @param message Message to the written to the console
     */
    public static void logError(String message)
    {
        logError(defaultPrefix, message);
    }

    /**
     * Logs an error (red colored) to the console with a plugin's prefix.
     * @param pluginPrefixName The prefix for the plugin
     * @param message Message to the written to the console
     */
    public static void logError(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "\n[" + validCustomPrefixOrDefault(pluginPrefixName) +
                        "] ERROR: " + message + ChatColor.RESET + "\n");
    }

    /**
     * Logs a warning (yellow colored) to the console with default prefix (CraftEra Suite).
     * @param message Message to the written to the console
     */
    public static void logWarning(String message)
    {
        logWarning(defaultPrefix, message);
    }

    /**
     * Logs a warning (yellow colored) to the console with a plugin's prefix.
     * @param pluginPrefixName The prefix for the plugin
     * @param message Message to the written to the console
     */
    public static void logWarning(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.YELLOW + "\n[" + validCustomPrefixOrDefault(pluginPrefixName) +
                        "] WARNING: " + message + ChatColor.RESET + "\n");
    }

    /**
     * Logs a success (green colored) to the console with default prefix (CraftEra Suite).
     * @param message Message to the written to the console
     */
    public static void logSuccess(String message)
    {
        logSuccess(defaultPrefix, message);
    }

    /**
     * Logs a success (green colored) to the console with a plugin's prefix.
     * @param pluginPrefixName The prefix for the plugin
     * @param message Message to the written to the console
     */
    public static void logSuccess(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.GREEN + "\n[" + validCustomPrefixOrDefault(pluginPrefixName) +
                        "] SUCCESS: " + message + ChatColor.RESET + "\n");
    }

    /**
     * Logs a message (no color) to the console with default prefix (CraftEra Suite).
     * @param message Message to the written to the console
     */
    public static void logMessage(String message)
    {
        logMessage(defaultPrefix, message);
    }

    /**
     * Logs a message (no color) to the console with a plugin's prefix.
     * @param pluginPrefixName The prefix for the plugin
     * @param message Message to the written to the console
     */
    public static void logMessage(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage("[" + validCustomPrefixOrDefault(pluginPrefixName) + "] " + message);
    }

    private static String validCustomPrefixOrDefault(String pluginPrefixName)
    {
        return ( StringUtil.isNullOrEmpty(pluginPrefixName) ) ? defaultPrefix : pluginPrefixName;
    }
}
