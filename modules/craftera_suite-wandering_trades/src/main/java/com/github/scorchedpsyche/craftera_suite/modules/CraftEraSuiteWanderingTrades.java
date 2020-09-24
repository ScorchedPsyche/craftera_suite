package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import com.github.scorchedpsyche.craftera_suite.modules.core.TradeListManager;
import com.github.scorchedpsyche.craftera_suite.modules.listeners.WanderingTraderSpawnListener;
import com.mojang.authlib.GameProfile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CraftEraSuiteWanderingTrades extends JavaPlugin
{
    public static CraftEraSuiteCore craftEraSuiteCore = JavaPlugin.getPlugin(CraftEraSuiteCore.class);
    public static FileConfiguration config;
    public static List<GameProfile> playerProfiles;
    public static List<ItemStack> whitelistedPlayerHeads;

    public static MerchantManager merchantManager;
    public static TradeListManager tradeList;

    // Plugin startup logic
    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();
        playerProfiles = new ArrayList<>();
        config = getConfig();

        whitelistedPlayerHeads = new ArrayList<>();
        tradeList = new TradeListManager(this);

        merchantManager = new MerchantManager();

        getServer().getPluginManager().registerEvents(new WanderingTraderSpawnListener(), this);
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
    }
}
