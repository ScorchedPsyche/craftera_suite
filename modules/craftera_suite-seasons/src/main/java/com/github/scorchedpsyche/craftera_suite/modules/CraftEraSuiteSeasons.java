package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinSeasonsListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.SeasonsCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class CraftEraSuiteSeasons extends JavaPlugin
{
    public static SeasonManager seasonManager;

    private SeasonsDatabaseApi seasonsDatabaseApi;

    @Override
    public void onEnable()
    {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            if( getServer().getPluginManager().getPlugin("LuckPerms") != null )
            {
                // Attempt to setup the rest of the plugin
                seasonsDatabaseApi = new SeasonsDatabaseApi();

                // Setup and verify DB tables
                if( seasonsDatabaseApi.setupAndVerifySqlTable() )
                {
                    seasonManager = new SeasonManager(seasonsDatabaseApi).setCurrentSeason(seasonsDatabaseApi.fetchCurrentSeason());

                    // Check if there's no active season
                    if( seasonManager.current == null )
                    {
                        // No active season. Display warning
                        ConsoleUtil.logWarning( SuitePluginManager.Seasons.Name.full,
                                "Module is enabled but there is no active season? Ignore if this is intended.");
                    }

                    // Add plugin commands
                    addPluginCommands();

                    // Listeners
                    getServer().getPluginManager().registerEvents(new SeasonsCommandListener(seasonManager), this);
                    getServer().getPluginManager().registerEvents(new PlayerJoinSeasonsListener(seasonManager), this);
                } else {
                    // Failed to create database tables! Display error and disable plugin
                    ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                    Bukkit.getPluginManager().disablePlugin(this);
                }
            } else {
                // LuckPerms missing! Display error and disable plugin
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: LuckPerms MISSING! Download the dependency and RELOAD/RESTART the server.");
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
        seasonManager = null;
        seasonsDatabaseApi = null;

        super.onDisable();
    }

    private void addPluginCommands()
    {
        HashMap<String, CommandModel> seasonsSubcommands = new HashMap<>();

        seasonsSubcommands.put("create", new CommandModel());
        seasonsSubcommands.put("current", new CommandModel());
        seasonsSubcommands.put("end", new CommandModel());
        seasonsSubcommands.put("manage", new CommandModel());
        seasonsSubcommands.put("start", new CommandModel());
        HashMap<String, CommandModel> seasons = new HashMap<>();
        seasons.put("seasons", new CommandModel("craftera_suite.seasons").addSubcommands(seasonsSubcommands));
        seasonsSubcommands = null;

        CraftEraSuiteCore.customTabCompleter.commands.putAll(seasons);
    }
}
