package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import com.github.scorchedpsyche.craftera_suite.modules.core.TradeListManager;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.WanderingTraderSpawnListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerHeadUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.FolderUtils;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
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
    public static List<GameProfile> playerProfiles;
    public static List<ItemStack> whitelistedPlayerHeads;

    public static MerchantManager merchantManager;
    public static TradeListManager tradeList;

    // Plugin startup logic
    @Override
    public void onEnable()
    {
        // Attempts to create plugin root folder
        File pluginRootFolder = FolderUtils.getOrCreatePluginSubfolder(this.getName());

        // Check if plugin root folder was created
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

                playerProfiles = new ArrayList<>();
                whitelistedPlayerHeads = new ArrayList<>();
                tradeList = new TradeListManager( pluginRootFolder + File.separator + "trade_lists");

                merchantManager = new MerchantManager();

                getServer().getPluginManager().registerEvents(new WanderingTraderSpawnListener(), this);

                // Cache heads so that trades aren't locked until head is loaded
                Bukkit.getScheduler().runTaskAsynchronously(this, PlayerHeadUtils::preloadPlayerHeads);
            } catch (IOException | InvalidConfigurationException e)
            {
                e.printStackTrace();
                pluginRootFolder = null;
                this.onDisable();
            }
        } else {
            pluginRootFolder = null;
            this.onDisable();
        }
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
        resourcesManager = null;
        config = null;
        playerProfiles = null;
        whitelistedPlayerHeads = null;
        merchantManager = null;
        tradeList = null;
        ConsoleUtils.logMessage(SuitePluginManager.WanderingTrades.Name.full,
                                "Plugin disabled");
    }
}
