package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.SeasonsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.listeners.SeasonsCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CraftEraSuiteSeasons extends JavaPlugin
{
    private SeasonsDatabaseApi seasonsDatabaseApi;

    @Override
    public void onEnable()
    {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            // Attempt to setup the rest of the plugin
            seasonsDatabaseApi = new SeasonsDatabaseApi();

            // Setup and verify DB tables
            if( seasonsDatabaseApi.setupAndVerifySqlTable() )
            {
                getServer().getPluginManager().registerEvents(new SeasonsCommandListener(), this);

            } else {
                // Failed to create database tables! Display error and disable plugin
                ConsoleUtils.logError(this.getName(), "Failed to create database tables. Disabling!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }
}
