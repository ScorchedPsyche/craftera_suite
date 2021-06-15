package com.github.scorchedpsyche.craftera_suite.modules;

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
        HashMap<String, CommandModel> statusSubcommands = new HashMap<>();
        statusSubcommands.put("activate", new CommandModel());
        statusSubcommands.put("deactivate", new CommandModel());
        statusSubcommands.put("start", new CommandModel());
        statusSubcommands.put("end", new CommandModel());
        statusSubcommands.put("archive", new CommandModel());

        HashMap<String, CommandModel> accountSubcommands = new HashMap<>();
        accountSubcommands.put("enable", new CommandModel());
        accountSubcommands.put("disable", new CommandModel());

        HashMap<String, CommandModel> editSubcommands = new HashMap<>();
        editSubcommands.put("number", new CommandModel());
        editSubcommands.put("title", new CommandModel());
        editSubcommands.put("subtitle", new CommandModel());
        editSubcommands.put("status", new CommandModel().addSubcommands(statusSubcommands));
        editSubcommands.put("account", new CommandModel().addSubcommands(accountSubcommands));

        HashMap<String, CommandModel> selectedSubcommands = new HashMap<>();
        selectedSubcommands.put("delete", new CommandModel());
        selectedSubcommands.put("display", new CommandModel());
        selectedSubcommands.put("edit", new CommandModel().addSubcommands(editSubcommands));

        HashMap<String, CommandModel> seasonsSubcommands = new HashMap<>();
        seasonsSubcommands.put("create_new_and_select_for_editing", new CommandModel());
        seasonsSubcommands.put("current", new CommandModel());
        seasonsSubcommands.put("list", new CommandModel());
        seasonsSubcommands.put("select", new CommandModel());
        seasonsSubcommands.put("selected", new CommandModel().addSubcommands(selectedSubcommands));
        HashMap<String, CommandModel> seasons = new HashMap<>();
        seasons.put("seasons", new CommandModel(SuitePluginManager.Seasons.Permissions.seasons).addSubcommands(seasonsSubcommands));
        seasonsSubcommands = null;

        CraftEraSuiteCore.customTabCompleter.commands.putAll(seasons);
    }
}
