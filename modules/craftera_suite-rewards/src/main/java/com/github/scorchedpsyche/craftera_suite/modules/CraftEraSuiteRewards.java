package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinSeasonsListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.SeasonsCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteRewards extends JavaPlugin {

    private SeasonsDatabaseApi seasonsDatabaseApi;

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            // Check if LuckPerms is loaded
            if( getServer().getPluginManager().getPlugin("LuckPerms") != null )
            {
                // Attempt to setup the rest of the plugin
                seasonsDatabaseApi = new SeasonsDatabaseApi();

                // Setup and verify DB tables
                if( seasonsDatabaseApi.setupAndVerifySqlTable() )
                {

                } else {
                    // Failed to create database tables! Display error and disable plugin
                    ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                    Bukkit.getPluginManager().disablePlugin(this);
                }
            } else {
                // LuckPerms missing! Display error and disable plugin
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix()
                    + "] ERROR: 'LuckPerms' MISSING! Download the dependency and RELOAD/RESTART the server.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix()
                + "] ERROR: 'CraftEra Suite - Core' MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
