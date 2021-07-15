package com.github.scorchedpsyche.craftera_suite.modules.sleep;

import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners.*;
import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class CraftEraSuiteSleep extends JavaPlugin {
    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static FileConfiguration config;
    private SleepManager sleepManager;

    /**
     * Plugin's startup code
     */
    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
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

                    sleepManager = new SleepManager();

                    // Add plugin commands
                    addPluginCommands();

                    // Listeners
                    getServer().getPluginManager().registerEvents(new PlayerBedEnterSleepListener(sleepManager), this);
                    getServer().getPluginManager().registerEvents(new PlayerBedLeaveSleepListener(sleepManager), this);
                    getServer().getPluginManager().registerEvents(new PlayerChangedWorldSleepListener(sleepManager), this);
//            getServer().getPluginManager().registerEvents(new PlayerJoinSleepListener(sleepManager), this);
                    getServer().getPluginManager().registerEvents(new PlayerQuitSleepListener(sleepManager), this);
                    getServer().getPluginManager().registerEvents(new SleepCommandListener(sleepManager), this);
                    getServer().getPluginManager().registerEvents(new TimeSkipSleepListener(sleepManager), this);

                } catch (IOException | InvalidConfigurationException e)
                {
                    e.printStackTrace();
                    this.onDisable();
                }
            } else {
                ConsoleUtil.logError(SuitePluginManager.Achievements.Name.full,
                        "Failed to create plugin's root folder. Disabling!");
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


    /**
     * Plugin's cleanup code
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Adds plugin's commands to CORE's TabCompleter
     */
    private void addPluginCommands()
    {
        CustomTabCompleter.commands.putAll(
            new CommandModel()
                .sibling("sleep")
                .subCommands);
    }
}
