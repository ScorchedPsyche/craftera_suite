package modules.com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinSpectatorListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerQuitSpectatorListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listener.SpectatorModeCommandListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorDatabaseAPI;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;
import modules.com.github.scorchedpsyche.craftera_suite.modules.task.ProcessPlayersInSpectatorTask;
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
    public static ProcessPlayersInSpectatorTask processPlayersInSpectatorTask;

//    private static Integer processPlayersInSpectatorTaskId;
//    private static Runnable processPlayersInSpectatorTask = new Runnable() {
//        @Override
//        public void run() {
//            spectatorModeManager.calculateSpectatorsDistanceToExecutingLocationAndTeleportBackIfNeeded();
//        }
//    };

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            // Attempts to create plugin root folder
            File pluginRootFolder = FolderUtil.getOrCreatePluginSubfolder(this.getName());

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
                        spectatorModeManager.addOnlinePlayersToSpectator();

                        processPlayersInSpectatorTask = new ProcessPlayersInSpectatorTask(
                                SuitePluginManager.SpectatorMode.Name.full,
                                "processPlayersInSpectator",
                                spectatorModeManager);

                        // Check if there are any players online and fetch those that are on spectator mode
                        if( !spectatorModeManager.playersInSpectator.isEmpty() )
                        {
                            startRepeatingTaskIfNotRunning();
                        }

                        getServer().getPluginManager().registerEvents(new SpectatorModeCommandListener(spectatorModeManager), this);
                        getServer().getPluginManager().registerEvents(new PlayerJoinSpectatorListener(spectatorModeManager), this);
                        getServer().getPluginManager().registerEvents(new PlayerQuitSpectatorListener(spectatorModeManager), this);


                    } else {
                        // Failed to create database tables! Display error and disable plugin
                        ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
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

    @Override
    public void onDisable() {
        // Cancel repeating task
        cancelRepeatingTaskIfRunning();
    }

    public static void startRepeatingTaskIfNotRunning()
    {
//        ConsoleUtils.logSuccess("start");
        if( processPlayersInSpectatorTask == null || !processPlayersInSpectatorTask.isRunning() )
        {
            processPlayersInSpectatorTask = new ProcessPlayersInSpectatorTask(
                SuitePluginManager.SpectatorMode.Name.full,
                "processPlayersInSpectator",
                spectatorModeManager);
            processPlayersInSpectatorTask.runTaskTimer(CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),
                    0L, SuitePluginManager.SpectatorMode.Task.ProcessPlayersInSpectator.period);
//            ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
//                    "Task STARTED: processing players in spectator mode");
        }
//        if( processPlayersInSpectatorTaskId == null || !Bukkit.getScheduler().isCurrentlyRunning(processPlayersInSpectatorTaskId) )
//        {
//            processPlayersInSpectatorTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
//                    CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),
//                    processPlayersInSpectatorTask, 0L, 5);
//            ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
//                    "Task STARTED: processing players in spectator mode");
//        }
    }

    public static void cancelRepeatingTaskIfRunning()
    {
        if( processPlayersInSpectatorTask != null && processPlayersInSpectatorTask.isRunning() )
        {
            processPlayersInSpectatorTask.cancel();
//            ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
//                    "Task CANCELLED: processing players in spectator mode");
        }
//        if( processPlayersInSpectatorTaskId != null && Bukkit.getScheduler().isCurrentlyRunning(processPlayersInSpectatorTaskId) )
//        {
//            Bukkit.getScheduler().cancelTask(processPlayersInSpectatorTaskId);
//            ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
//                    "Task CANCELLED: processing players in spectator mode");
//        }
    }

//    public static void startRepeatingTaskIfNotRunningAndPlayersOnlineAndInSpectator()
//    {
//        if( processPlayersInSpectatorTaskId == null || !Bukkit.getScheduler().isCurrentlyRunning(processPlayersInSpectatorTaskId) )
//        {
//            if( !CollectionUtils.isNullOrEmpty(Bukkit.getOnlinePlayers()) &&
//                    !CollectionUtils.isNullOrEmpty(spectatorModeManager.playersInSpectator) )
//            {
//                processPlayersInSpectatorTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
//                        CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),
//                        processPlayersInSpectatorTask, 0L, 5);
//                ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
//                        "Task STARTED: processing players in spectator mode");
//            }
//
//        }
//    }
//
//    public static void cancelRepeatingTaskIfRunningAndNoPlayersInSpectator()
//    {
//        if( processPlayersInSpectatorTaskId != null && spectatorModeManager.playersInSpectator.isEmpty() )
//        {
//            Bukkit.getScheduler().cancelTask(processPlayersInSpectatorTaskId);
//            ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
//                    "Task CANCELLED: processing players in spectator mode");
//        }
//    }
}
