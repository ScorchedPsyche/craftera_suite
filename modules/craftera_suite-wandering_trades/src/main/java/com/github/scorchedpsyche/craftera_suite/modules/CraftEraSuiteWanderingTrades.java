package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import com.github.scorchedpsyche.craftera_suite.modules.core.TradeListManager;
import com.github.scorchedpsyche.craftera_suite.modules.listener.WanderingTraderSpawnListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.task.PreloadPlayerHeadsTask;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CraftEraSuiteWanderingTrades extends JavaPlugin
{
    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static FileConfiguration config;
    public static List<ItemStack> whitelistedPlayerHeads;
    public static MerchantManager merchantManager;
    public static TradeListManager tradeListManager;

    public static PreloadPlayerHeadsTask preloadPlayerHeadsTask = new PreloadPlayerHeadsTask(
            SuitePluginManager.WanderingTrades.Name.full, "preloadPlayerHeadsTask");

    // Plugin startup logic
    @Override
    public void onEnable()
    {
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
                    add("trade_lists/heads_decoration.json");
                    add("trade_lists/heads_players.json");
                    add("trade_lists/items.json");
                    add("trade_lists_exporting/CES - Wandering Trades - Decoration  Heads – Vanilla.csv");
                    add("trade_lists_exporting/CES - Wandering Trades - Decoration  Heads – Vanilla.json");
                    add("trade_lists_exporting/CES - Wandering Trades - Decoration Heads – Food.csv");
                    add("trade_lists_exporting/CES - Wandering Trades - Decoration Heads – Food.json");
                    add("trade_lists_exporting/CES - Wandering Trades - Decoration Heads – Non-Vanilla.csv");
                    add("trade_lists_exporting/CES - Wandering Trades - Decoration Heads – Non-Vanilla.json");
                    add("trade_lists_exporting/export_list.py");
                    add("config.yml");
                }});

                // Attempt to setup the rest of the plugin
                try
                {
                    config = new YamlConfiguration();
                    config.load(pluginRootFolder + File.separator + "config.yml");

                    whitelistedPlayerHeads = new ArrayList<>();
                    tradeListManager = new TradeListManager(pluginRootFolder + File.separator + "trade_lists");

                    merchantManager = new MerchantManager();

                    getServer().getPluginManager().registerEvents(new WanderingTraderSpawnListener(), this);

                    // Cache heads so that trades aren't locked until head is loaded when trader is spawned
                    preloadPlayerHeadsTask.runTaskAsynchronously(this);
                } catch (IOException | InvalidConfigurationException e)
                {
                    pluginRootFolder = null;
                    e.printStackTrace();
                    this.onDisable();
                }
            } else {
                this.onDisable();
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
        WanderingTraderSpawnListener.onDisable();
        resourcesManager = null;
        config = null;
        whitelistedPlayerHeads = null;
        merchantManager = null;
        tradeListManager = null;
        preloadPlayerHeadsTask = null;

        super.onDisable();
    }
}
