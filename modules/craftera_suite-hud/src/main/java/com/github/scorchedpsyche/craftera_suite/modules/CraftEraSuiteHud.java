package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.HudCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerJoinListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerQuitListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudCommandManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudDatabaseAPI;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CraftEraSuiteHud extends JavaPlugin
{
    public CraftEraSuiteCore cesCore;
    public HudDatabaseAPI hudDatabaseAPI;
    public HudManager hudManager;
    public HudCommandManager hudCommandManager;
    
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

        pluginRootFolder = cesCore.folderUtils.getOrCreatePluginSubfolder(this);
//        playerConfigsFolder = new File( pluginRootFolder.toString() + File.separator + "players" );
//
//        if ( !playerConfigsFolder.exists() )
//        {
//            if ( !playerConfigsFolder.mkdirs() )
//            {
//               cesCore.consoleUtils.logError(
//                       "Player configuration folder failed to be created: check folder write permissions or try to create the folder manually. If everything looks OK and the issue still persists, report this to the developer. FOLDER PATH STRUCTURE THAT SHOULD HAVE BEEN CREATED: " + ChatColor.YELLOW + playerConfigsFolder.toString());
//            }
//        }

        hudDatabaseAPI = new HudDatabaseAPI(cesCore.databaseManager.database);
        hudManager = new HudManager(hudDatabaseAPI);
        hudCommandManager = new HudCommandManager(hudManager);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            hudManager.showHudForPlayers();
        }, 0L, 2);
    }
}
