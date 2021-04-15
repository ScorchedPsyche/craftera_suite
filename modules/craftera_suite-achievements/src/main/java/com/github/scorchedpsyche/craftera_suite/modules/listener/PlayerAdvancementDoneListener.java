package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementDoneListener implements Listener
{
    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
    {
        String advNamespacedKey = event.getAdvancement().getKey().toString();

        // Check if it's not a recipe
        if( !advNamespacedKey.startsWith("minecraft:recipes/") )
        {
            ConsoleUtils.logSuccess(advNamespacedKey);

//            for ( String criteria : event.getAdvancement().getCriteria() )
//            {
//                if( criteria.substring(0, 3).equals("has") )
//                {
//                    System.out.println(event.getAdvancement().getKey().toString());
//                    System.out.println(criteria);
//                } else {
//                    ConsoleUtils.logSuccess(event.getAdvancement().getKey().toString());
//                    ConsoleUtils.logSuccess(criteria);
//                }
//            }
        }
    }
}
