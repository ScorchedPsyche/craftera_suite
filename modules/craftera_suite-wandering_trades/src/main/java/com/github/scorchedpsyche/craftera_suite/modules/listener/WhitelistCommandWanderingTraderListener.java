package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteWanderingTrades;
import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WhitelistCommandWanderingTraderListener implements Listener
{
    public WhitelistCommandWanderingTraderListener(MerchantManager merchantManager)
    {
        this.merchantManager = merchantManager;
    }

    private MerchantManager merchantManager;

    @EventHandler
    public void onWhitelistCommand(PlayerCommandPreprocessEvent event)
    {
        if( event.getMessage().startsWith("/whitelist") )
        {
            String[] exploded = event.getMessage().split(" ");
            if( exploded.length == 3 )
            {
                if(exploded[1].equals("add") || exploded[1].equals("remove"))
                {
                    // /whitelist add
                     Bukkit.getScheduler().scheduleAsyncDelayedTask(
                         Bukkit.getPluginManager().getPlugin(SuitePluginManager.WanderingTrades.Name.pomXml),
                         new BukkitRunnable()
                         {
                            @Override
                            public void run()
                            {
                                merchantManager.reloadWhitelist();
                            }
                        }, 100L);
                }
            }
        }
    }
}
