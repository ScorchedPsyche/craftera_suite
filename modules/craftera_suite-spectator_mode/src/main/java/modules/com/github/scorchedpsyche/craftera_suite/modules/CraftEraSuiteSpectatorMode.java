package modules.com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.FolderUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.CollectionUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerJoinSpectatorListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerQuitSpectatorListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listeners.SpectatorModeCommandListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorDatabaseAPI;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class CraftEraSuiteSpectatorMode extends JavaPlugin {
    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static SpectatorModeManager spectatorModeManager;
    public static FileConfiguration config;

    private SpectatorDatabaseAPI spectatorDatabaseAPI;
    private static Integer processPlayersInSpectatorTaskId;
    private static Runnable processPlayersInSpectatorTask = new Runnable() {
        @Override
        public void run() {
            spectatorModeManager.calculatePlayerDistanceToExecutingLocationAndTeleportBackIfNeeded();
        }
    };

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            // Attempts to create plugin root folder
            File pluginRootFolder = FolderUtils.getOrCreatePluginSubfolder(this.getName());

            // Check if plugin root folder exists
            if( pluginRootFolder != null )
            {
                // Plugin root folder created!
                resourcesManager.copyResourcesToServer(this, pluginRootFolder, new ArrayList<String>(){{
                    add("config.yml");
                }});

                // Attempt to setup the rest of the plugin
                try
                {
                    config = new YamlConfiguration();
                    config.load(pluginRootFolder + File.separator + "config.yml");

                    spectatorDatabaseAPI = new SpectatorDatabaseAPI();

                    // Setup and verify DB tables
                    if( spectatorDatabaseAPI.setupAndVerifySqlTable() )
                    {
                        // Everything OK. Finish setting up
                        spectatorModeManager = new SpectatorModeManager( spectatorDatabaseAPI );
                        getServer().getPluginManager().registerEvents(new SpectatorModeCommandListener(spectatorModeManager), this);
                        getServer().getPluginManager().registerEvents(new PlayerJoinSpectatorListener(spectatorModeManager), this);
                        getServer().getPluginManager().registerEvents(new PlayerQuitSpectatorListener(spectatorModeManager), this);

                        spectatorModeManager.addOnlinePlayersToSpectator();

                        // Check if there are any players online and fetch those that are on spectator mode
                        startRepeatingTaskIfNotRunningAndPlayersOnlineAndInSpectator();
                    } else {
                        // Failed to create database tables! Display error and disable plugin
                        ConsoleUtils.logError(this.getName(), "Failed to create database tables. Disabling!");
                        Bukkit.getPluginManager().disablePlugin(this);
                    }
                } catch (IOException | InvalidConfigurationException e)
                {
                    pluginRootFolder = null;
                    e.printStackTrace();
                    this.onDisable();
                }
            } else {
                // Failed to create plugin's root folder
                pluginRootFolder = null;
                this.onDisable();
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public static void cancelRepeatingTaskIfNoPlayersOnlineOrNoneInSpectator ()
    {
        if( processPlayersInSpectatorTaskId != null )
        {
            if( CollectionUtils.isNullOrEmpty(Bukkit.getOnlinePlayers()) ||
                CollectionUtils.isNullOrEmpty(spectatorModeManager.playersInSpectator) )
            {
                Bukkit.getScheduler().cancelTask(processPlayersInSpectatorTaskId);
                ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
                        "Task CANCELLED: processing players in spectator mode");
            }
        }
    }

    public static void startRepeatingTaskIfNotRunningAndPlayersOnlineAndInSpectator()
    {
        if( processPlayersInSpectatorTaskId == null || !Bukkit.getScheduler().isCurrentlyRunning(processPlayersInSpectatorTaskId) )
        {
            if( !CollectionUtils.isNullOrEmpty(Bukkit.getOnlinePlayers()) &&
                !CollectionUtils.isNullOrEmpty(spectatorModeManager.playersInSpectator) )
            {
                processPlayersInSpectatorTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),
                        processPlayersInSpectatorTask, 0L, 5);
                ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
                        "Task STARTED: processing players in spectator mode");
            }

        }
    }

    @Override
    public void onDisable() {
        // Cancel repeating task
        cancelRepeatingTaskIfNoPlayersOnlineOrNoneInSpectator();
    }
}
