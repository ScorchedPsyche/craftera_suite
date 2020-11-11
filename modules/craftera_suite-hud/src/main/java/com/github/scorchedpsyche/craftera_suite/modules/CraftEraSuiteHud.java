package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.HudCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerJoinListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerQuitListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudDatabaseAPI;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteHud extends JavaPlugin
{
    public HudManager hudManager;

    @Override
    public void onEnable()
    {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            setup();

            getServer().getPluginManager().registerEvents(new HudCommandListener(hudManager), this);
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(hudManager), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(hudManager), this);
        } else {
            // Core dependency missing! Display error
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        hudManager = null;
    }

    private void setup()
    {
        hudManager = new HudManager( new HudDatabaseAPI( DatabaseManager.database ) );

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            hudManager.showHudForPlayers();
        }, 0L, 2);
    }
}
