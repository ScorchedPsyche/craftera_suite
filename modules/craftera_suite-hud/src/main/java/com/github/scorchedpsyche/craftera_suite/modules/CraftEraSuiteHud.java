package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.HudCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinHudListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerQuitHudListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudDatabaseAPI;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class CraftEraSuiteHud extends JavaPlugin
{
    public static HudManager hudManager;
    private HudDatabaseAPI hudDatabaseAPI;
//    private Integer showHudForPlayersTask;

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
//                showHudForPlayersTask =
                Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        this, () -> hudManager.showHudForPlayers(), 0L, 5);

                // Add plugin commands
                addPluginCommands();

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
        // Cancel all tasks
        Bukkit.getScheduler().cancelTasks(this);
//        if( showHudForPlayersTask != null )
//        {
//            Bukkit.getScheduler().cancelTask(showHudForPlayersTask);
//        }

        hudDatabaseAPI = null;
        hudManager = null;
//        if ( showHudForPlayersTask != null )
//        {
//            Bukkit.getScheduler().cancelTask(showHudForPlayersTask);
//            showHudForPlayersTask = null;
//        }

        super.onDisable();
    }

    private void addPluginCommands()
    {
//        HashMap<String, CommandModel> hudConfigFormatWorldTimeSubcommands = new HashMap<>();
//        hudConfigFormatWorldTimeSubcommands.put("as24Hour", new CommandModel());
//        hudConfigFormatWorldTimeSubcommands.put("asTicks", new CommandModel());
//
////        HashMap<String, CommandModel> hudConfigFormatSubcommands = new HashMap<>();
////        hudConfigFormatSubcommands.put("world_time", new CommandModel().addSubcommands(hudConfigFormatWorldTimeSubcommands));
////
////        HashMap<String, CommandModel> hudConfigSubcommands = new HashMap<>();
////        hudConfigSubcommands.put("format", new CommandModel().addSubcommands(hudConfigFormatSubcommands));
//
////        CommandModel hudCommands = new CommandModel().
////            add("hud")
////                .add("config")
////                    .add("display_mode");
//
//        HashMap<String, CommandModel> hudSubcommands = new HashMap<>();
//
//        hudSubcommands
//            .put("config", new CommandModel()
//                .addCommand("display_mode")
//                    .addSubcommand("compact")
//                    .addSubcommand("extended")
//                .addCommand("colorize")
//                    .addSubcommand("coordinates")
//                    .addSubcommand("nether_portal_coordinates")
//                    .addSubcommand("player_orientation")
//                    .addSubcommand("tool_durability")
//                    .addSubcommand("world_time")
//                .addCommand("format")
////                    .addSubcommandWithSubcommands("world_time", hudConfigFormatWorldTimeSubcommands)
//            );
//
//        hudSubcommands
//            .put("toggle", new CommandModel()
//                .addCommand("coordinates")
//                .addCommand("nether_portal_coordinates")
//                .addCommand("player_orientation"));
//
//        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-commerce") )
//        {
//            hudSubcommands.get("toggle").addCommand( "plugin_commerce");
//        }
//        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-spectator") )
//        {
//            hudSubcommands.get("toggle").addCommand( "plugin_spectator");
//        }
//
//        hudSubcommands.get("toggle")
//                .addCommand("server_time")
//                .addCommand("tool_durability")
//                .addCommand("world_time");
//
//        HashMap<String, CommandModel> hud = new HashMap<>();
//        hud.put("hud", new CommandModel().addSubcommands(hudSubcommands));
//        hudSubcommands = null;

//        hudConfigFormatWorldTimeSubcommands.put("as24Hour", new CommandModel());
//        hudConfigFormatWorldTimeSubcommands.put("asTicks", new CommandModel());

        CustomTabCompleter.commands.putAll(
            new CommandModel()
            .sibling("hud")
                .child("config")
                    .child("display_mode")
                        .child("compact")
                        .sibling("extended")
                        .back()
                    .sibling("colorize")
                        .child("coordinates")
                        .sibling("nether_portal_coordinates")
                        .sibling("player_orientation")
                        .sibling("tool_durability")
                        .sibling("world_time")
                        .back()
                    .sibling("format")
                        .child("world_time")
                            .child("as24Hour")
                            .sibling("asTicks")
                            .back()
                        .back()
                    .back()
                .sibling("toggle")
                    .child("coordinates")
                    .sibling("nether_portal_coordinates")
                    .sibling("player_orientation")
                    .sibling("server_time")
                    .sibling("tool_durability")
                    .sibling("world_time")
            .subCommands);
    }
}
