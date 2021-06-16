package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerAdvancementDoneListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.AchievementManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.AchievementsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class CraftEraSuiteAchievements extends JavaPlugin
{
    private AchievementsDatabaseApi achievementsDatabaseApi;
    private AchievementManager achievementManager;

    @Override
    public void onEnable()
    {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {

            // Attempts to create plugin root folder
            File pluginRootFolder = FolderUtil.getOrCreatePluginSubfolder(this.getName());

            // Check if plugin root folder exists
            if( pluginRootFolder != null ) {
                // Plugin root folder created!
                ResourcesManager resourcesManager = new ResourcesManager();
                resourcesManager.copyResourcesToServer(this, pluginRootFolder, new ArrayList<String>() {{
                    add("advancements/vanilla/1.17/en-US.json");
                }});

                // Attempt to setup the rest of the plugin
                achievementsDatabaseApi = new AchievementsDatabaseApi();

                // Setup and verify DB tables
                if( achievementsDatabaseApi.setupAndVerifySqlTable() )
                {
                    achievementManager = new AchievementManager(achievementsDatabaseApi, pluginRootFolder);

                    // Try to setup Achievements Manager
                    if( achievementManager.setupManager() )
                    {
                        getServer().getPluginManager().registerEvents(new PlayerAdvancementDoneListener(achievementManager), this);
                    } else {
                        // Failed to create database tables! Display error and disable plugin
                        ConsoleUtil.logError(this.getName(), "Failed to setup Achievement Manager. Disabling!");
                        Bukkit.getPluginManager().disablePlugin(this);
                    }
                } else {
                    // Failed to create database tables! Display error and disable plugin
                    ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                    Bukkit.getPluginManager().disablePlugin(this);
                }
            } else {
                ConsoleUtil.logError(SuitePluginManager.Achievements.Name.full,
                        "Failed to create plugin's root folder. Disabling!");
                this.onDisable();
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
        achievementsDatabaseApi = null;
        achievementManager = null;

        super.onDisable();
    }
}
