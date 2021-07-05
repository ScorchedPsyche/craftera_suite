package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.GamesCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.games.EnderDragonDeathGamesListener;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.games.EnderDragonSpawnGamesListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.GamesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class CraftEraSuiteGames extends JavaPlugin {
    private GamesManager gamesManager;

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            // Initialization
            gamesManager = new GamesManager();

            // Add plugin commands
            addPluginCommands();

            // Listeners
            getServer().getPluginManager().registerEvents(new GamesCommandListener(gamesManager), this);
            getServer().getPluginManager().registerEvents(new EnderDragonSpawnGamesListener(gamesManager), this);
            getServer().getPluginManager().registerEvents(new EnderDragonDeathGamesListener(gamesManager), this);
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
        HashMap<String, CommandModel> prepareSubcommands = new HashMap<>();
        for(SuitePluginManager.Games.Type eventType : SuitePluginManager.Games.Type.values())
        {
            prepareSubcommands.put(eventType.toString(), new CommandModel());
        }

        HashMap<String, CommandModel> eventsSubcommands = new HashMap<>();
        eventsSubcommands.put("prepare", new CommandModel(SuitePluginManager.Games.Permissions.prepare).addSubcommands(prepareSubcommands));
        eventsSubcommands.put("cancel", new CommandModel(SuitePluginManager.Games.Permissions.cancel));
        eventsSubcommands.put("open_subscriptions", new CommandModel(SuitePluginManager.Games.Permissions.open_subscriptions));
        eventsSubcommands.put("announce", new CommandModel(SuitePluginManager.Games.Permissions.announce));
        eventsSubcommands.put("join", new CommandModel(SuitePluginManager.Games.Permissions.join, true));
        eventsSubcommands.put("start", new CommandModel(SuitePluginManager.Games.Permissions.start));

        HashMap<String, CommandModel> events = new HashMap<>();
        events.put("games", new CommandModel(SuitePluginManager.Games.Permissions.games).addSubcommands(eventsSubcommands));

        CustomTabCompleter.commands.putAll(events);
    }
}
