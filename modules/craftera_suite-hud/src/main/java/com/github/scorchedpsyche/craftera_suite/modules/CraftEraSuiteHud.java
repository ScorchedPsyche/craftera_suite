package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.HudCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinHudListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerQuitHudListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudDatabaseAPI;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteHud extends JavaPlugin
{
    public static HudManager hudManager;
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
                        this, () -> hudManager.showHudForPlayers(), 0L, 5);

                // Listeners
                getServer().getPluginManager().registerEvents(new HudCommandListener(hudManager), this);
                getServer().getPluginManager().registerEvents(new PlayerJoinHudListener(hudManager), this);
                getServer().getPluginManager().registerEvents(new PlayerQuitHudListener(hudManager), this);
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
    public void onDisable()
    {
        // Cancel repeating task
        if( showHudForPlayersTask != null )
        {
            Bukkit.getScheduler().cancelTask(showHudForPlayersTask);
        }

        hudDatabaseAPI = null;
        hudManager = null;
        if ( showHudForPlayersTask != null )
        {
            Bukkit.getScheduler().cancelTask(showHudForPlayersTask);
            showHudForPlayersTask = null;
        }

        super.onDisable();
    }
}
