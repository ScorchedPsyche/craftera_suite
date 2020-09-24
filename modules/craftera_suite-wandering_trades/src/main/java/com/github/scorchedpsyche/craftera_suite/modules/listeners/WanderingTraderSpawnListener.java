package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteWanderingTrades;
import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class WanderingTraderSpawnListener implements Listener
{
    @EventHandler
    public void onWanderingTraderSpawned(EntitySpawnEvent event)
    {
        if ( event.getEntity().getType() == EntityType.WANDERING_TRADER )
        {
            WanderingTrader wanderingTrader = (WanderingTrader) event.getEntity();

            // Should remove default trades?
            if( CraftEraSuiteWanderingTrades.config.contains("remove_default_trades") &&
                CraftEraSuiteWanderingTrades.config.getBoolean("remove_default_trades") )
            {
                MerchantManager.removeDefaultTrades(wanderingTrader);
            }

            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteWanderingTrades.getPlugin(CraftEraSuiteWanderingTrades.class), () -> {
//                Bukkit.getScheduler().runTask(CraftEraSuiteWanderingTrades.getPlugin(CraftEraSuiteWanderingTrades.class), () -> {
                    CraftEraSuiteWanderingTrades.merchantManager.setMerchantTrades( wanderingTrader );
//                });
            });
        }
    }
}
