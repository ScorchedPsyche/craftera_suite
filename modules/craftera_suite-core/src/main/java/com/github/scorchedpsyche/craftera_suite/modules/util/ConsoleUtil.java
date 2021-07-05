package com.github.scorchedpsyche.craftera_suite.modules.util;

import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ConsoleUtil {
    private static final String defaultPrefix = "CraftEra Suite";

    /**
     * Logs a debug message (aqua colored) to the console with default prefix (CraftEra Suite).
     * @param message Message to the written to the console
     */
    public static void debugMessage(String message)
    {
        debugMessage(defaultPrefix, message);
    }

    /**
     * Logs a debug message (aqua colored) to the console with a plugin's prefix.
     * @param pluginPrefixName The prefix for the plugin
     * @param message Message to the written to the console
     */
    public static void debugMessage(String pluginPrefixName, String message)
    {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.AQUA + "\n[" + validCustomPrefixOrDefault(pluginPrefixName) +
                        "] DEBUG: " + message + "\n" + ChatColor.RESET);
    }

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
        StringFormattedModel errorStr = new StringFormattedModel()
            .red("\n[").add(validCustomPrefixOrDefault(pluginPrefixName)).add("] ERROR: " ).nl().nl()
            .add(message).reset().nl();
        Bukkit.getConsoleSender().sendMessage(errorStr.toString());
//        Bukkit.getConsoleSender().sendMessage(
//                ChatColor.RED + "\n[" + validCustomPrefixOrDefault(pluginPrefixName) +
//                        "] ERROR: " + message + "\n" + ChatColor.RESET);
    }

    /**
     * Logs an SQL query error (red colored) to the console with a plugin's prefix.
     * @param sql SQL statement that triggered the exception
     * @param sqlMessage The debug error returned from the driver
     */
    public static void logErrorSQL(String sql, String sqlMessage)
    {
        StringFormattedModel errorStr = new StringFormattedModel()
            .red("\n[").add(defaultPrefix).add("] ERROR executing SQL: " ).nl().nl().add(sql).nl().nl().add(sqlMessage);
        Bukkit.getConsoleSender().sendMessage(errorStr.toString());
    }

    /**
     * Logs an SQL query error (red colored) to the console with a plugin's prefix.
     * @param sql SQL statement that triggered the exception
     * @param sqlMessage The debug error returned from the driver
     */
    public static void logErrorSQLWithPluginPrefix(String pluginPrefixName, String method, String sql, String sqlMessage)
    {
        StringFormattedModel errorStr = new StringFormattedModel()
            .red("\n[").add(validCustomPrefixOrDefault(pluginPrefixName)).add("] ERROR executing SQL for method")
            .add(method).add(": " ).nl().nl().add(sql).nl().nl().add(sqlMessage);
        Bukkit.getConsoleSender().sendMessage(errorStr.toString());
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
                        "] WARNING: " + message + "\n" + ChatColor.RESET);
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
                        "] SUCCESS: " + message + "\n" + ChatColor.RESET);
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
