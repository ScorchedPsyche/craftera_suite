package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.HudCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerJoinListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerQuitListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudDatabaseAPI;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteHud extends JavaPlugin
{
    public HudManager hudManager;
    private HudDatabaseAPI hudDatabaseAPI;
    private Integer showHudForPlayersTask;

    @Override
    public void onEnable()
    {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            hudDatabaseAPI = new HudDatabaseAPI();

            // Setup and verify DB tables
            if( hudDatabaseAPI.setupAndVerifySqlTable() )
            {
                hudManager = new HudManager( hudDatabaseAPI );

                // Set up repeating task to update HUD for players
                showHudForPlayersTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        this, () -> hudManager.showHudForPlayers(), 0L, 2);

                getServer().getPluginManager().registerEvents(new HudCommandListener(hudManager), this);
                getServer().getPluginManager().registerEvents(new PlayerJoinListener(hudManager), this);
                getServer().getPluginManager().registerEvents(new PlayerQuitListener(hudManager), this);
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
        // Cancel repeating task
        if( showHudForPlayersTask != null )
        {
            Bukkit.getScheduler().cancelTask(showHudForPlayersTask);
        }

        // Plugin shutdown logic
        hudDatabaseAPI = null;
        hudManager = null;
    }
}
