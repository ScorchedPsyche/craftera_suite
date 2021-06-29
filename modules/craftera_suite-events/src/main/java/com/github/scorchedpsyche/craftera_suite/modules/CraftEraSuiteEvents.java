package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.EventsCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.EventsManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class CraftEraSuiteEvents extends JavaPlugin {
    private EventsManager eventsManager;

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            // Add plugin commands
            addPluginCommands();

            // Listeners
            getServer().getPluginManager().registerEvents(new EventsCommandListener(eventsManager), this);
//
//            spectatorDatabaseAPI = new SpectatorDatabaseAPI();
//
//            // Setup and verify DB tables
//            if( spectatorDatabaseAPI.setupAndVerifySqlTable() )
//            {
//                // Everything OK. Finish setting up
//                spectatorModeManager = new SpectatorModeManager( spectatorDatabaseAPI );
//                spectatorModeManager.addOnlinePlayersToSpectator();
//
//                processPlayersInSpectatorTask = new ProcessPlayersInSpectatorTask(
//                        SuitePluginManager.SpectatorMode.Name.full,
//                        "processPlayersInSpectator",
//                        spectatorModeManager);
//
//                // Check if there are any players online and fetch those that are on spectator mode
//                if( !spectatorModeManager.playersInSpectator.isEmpty() )
//                {
//                    startRepeatingTaskIfNotRunning();
//                }
//
//                // Add plugin commands
//                addPluginCommands();
//            } else {
//                // Failed to create database tables! Display error and disable plugin
//                ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
//                Bukkit.getPluginManager().disablePlugin(this);
//            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void addPluginCommands()
    {
        HashMap<String, CommandModel> eventsSubcommands = new HashMap<>();
        for(SuitePluginManager.Events.Type eventType : SuitePluginManager.Events.Type.values())
        {
            eventsSubcommands.put(eventType.toString(), new CommandModel());
        }

        HashMap<String, CommandModel> events = new HashMap<>();
        events.put("events", new CommandModel(SuitePluginManager.Events.Permissions.events).addSubcommands(eventsSubcommands));

        CustomTabCompleter.commands.putAll(events);
    }
}
