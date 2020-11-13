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
            // Should remove default trades?
            if( CraftEraSuiteWanderingTrades.config.getBoolean("remove_default_trades") )
            {
                MerchantManager.removeDefaultTrades((WanderingTrader) event.getEntity());
            }

            // Set trade asynchronously
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    CraftEraSuiteWanderingTrades.getPlugin(CraftEraSuiteWanderingTrades.class), () -> {
                        Bukkit.getScheduler().runTaskAsynchronously(
                                CraftEraSuiteWanderingTrades.getPlugin(CraftEraSuiteWanderingTrades.class), () -> {
                                    CraftEraSuiteWanderingTrades.merchantManager.setMerchantTrades(
                                            (WanderingTrader) event.getEntity() );

//                                    for(MerchantRecipe recipe : ((WanderingTrader) event.getEntity()).getRecipes())
//                                    {
//                                        ItemMeta itemMeta = recipe.getResult().getItemMeta().clone();
//                                        itemMeta.serialize();
//                                        ConsoleUtils.logMessage("Item serialized: " + recipe.getResult().getType() );
//                                    }
                        });
            }, 1L);
        }
    }
}
