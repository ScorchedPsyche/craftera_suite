package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteWanderingTrades;
import com.github.scorchedpsyche.craftera_suite.modules.core.MerchantManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitTask;

public class WanderingTraderSpawnListener implements Listener
{
    private static BukkitTask setMerchantTrades;

    @EventHandler
    public void onWanderingTraderSpawned(EntitySpawnEvent event)
    {
        if ( event.getEntity().getType() == EntityType.WANDERING_TRADER )
        {
            // Should remove default trades?
            if( CraftEraSuiteWanderingTrades.config.getBoolean("remove_default_trades") )
            {
                MerchantManager.removeDefaultTrades((WanderingTrader) event.getEntity());
            }

            // Set trade asynchronously
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    CraftEraSuiteWanderingTrades.getPlugin(CraftEraSuiteWanderingTrades.class), () -> {
                        setMerchantTrades = Bukkit.getScheduler().runTaskAsynchronously(
                                CraftEraSuiteWanderingTrades.getPlugin(CraftEraSuiteWanderingTrades.class), () -> {
                                    CraftEraSuiteWanderingTrades.merchantManager.setMerchantTrades(
                                            (WanderingTrader) event.getEntity() );
                        });
            }, 1L);
        }
    }

    public static void onDisable()
    {
        if( setMerchantTrades != null )
        {
            setMerchantTrades.cancel();
        }
    }
}
