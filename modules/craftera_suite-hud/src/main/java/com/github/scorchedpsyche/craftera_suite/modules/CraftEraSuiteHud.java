package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.HudCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerJoinListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerQuitListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudDatabaseAPI;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.FolderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CraftEraSuiteHud extends JavaPlugin
{
    public CraftEraSuiteCore cesCore;
    public HudDatabaseAPI hudDatabaseAPI;
    public HudManager hudManager;
    
    public File pluginRootFolder;
//    public File playerConfigsFolder;

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
    }

    private void setup()
    {
        cesCore = (CraftEraSuiteCore) Bukkit.getPluginManager().getPlugin("craftera_suite-core");

        pluginRootFolder = FolderUtils.getOrCreatePluginSubfolder(this.getName());

        hudDatabaseAPI = new HudDatabaseAPI(cesCore.databaseManager.database);
        hudManager = new HudManager(hudDatabaseAPI);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            hudManager.showHudForPlayers();
        }, 0L, 2);
    }
}
