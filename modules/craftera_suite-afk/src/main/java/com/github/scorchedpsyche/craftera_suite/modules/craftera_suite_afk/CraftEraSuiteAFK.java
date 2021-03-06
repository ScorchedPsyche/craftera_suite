package com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk;

import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.listeners.PlayerJoinAFKListener;
import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.listeners.PlayerQuitAFKListener;
import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.main.AFKDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.main.AFKManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteAFK extends JavaPlugin {
    AFKManager afkManager;
    AFKDatabaseApi afkDatabaseApi;

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            afkDatabaseApi = new AFKDatabaseApi();

            // Setup and verify DB tables
            if( afkDatabaseApi.setupAndVerifySqlTable() )
            {
                afkManager = new AFKManager(afkDatabaseApi);

                // Add online players to AFK manager
                for (Player player  : Bukkit.getOnlinePlayers() )
                {
                    // Start their AFK timer (when plugin is reloaded a new entry is added)
                    afkManager.startOrResetAFKTimerForPlayer(player);
                }

                // Set up repeating task to update AFK state for players
                Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        this, () -> afkManager.updatePlayersAFKState(), 0L, 20); // 5 * 60 *

                // Listeners
                getServer().getPluginManager().registerEvents(new PlayerJoinAFKListener(afkManager), this);
                getServer().getPluginManager().registerEvents(new PlayerQuitAFKListener(afkManager), this);
            } else {
                // Failed to create database tables! Display error and disable plugin
                ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        afkManager.disable();

        afkManager = null;
        afkDatabaseApi = null;
    }
}
