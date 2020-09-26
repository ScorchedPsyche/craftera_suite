package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import com.github.scorchedpsyche.craftera_suite.modules.core.TradeListManager;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.WanderingTraderSpawnListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.mojang.authlib.GameProfile;
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
    public static CraftEraSuiteCore cesCore = JavaPlugin.getPlugin(CraftEraSuiteCore.class);
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
        resourcesManager.pluginRootFolder =  cesCore.folderUtils.getOrCreatePluginSubfolder(this);
        resourcesManager.copyResourcesToServer(this, new ArrayList<String>(){{
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

        try
        {
            config = new YamlConfiguration();
            config.load(resourcesManager.pluginRootFolder + File.separator + "config.yml");
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        playerProfiles = new ArrayList<>();
        whitelistedPlayerHeads = new ArrayList<>();
        tradeList = new TradeListManager(
                resourcesManager.pluginRootFolder + File.separator + "trade_lists");

        merchantManager = new MerchantManager();

        getServer().getPluginManager().registerEvents(new WanderingTraderSpawnListener(), this);
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
    }
}
