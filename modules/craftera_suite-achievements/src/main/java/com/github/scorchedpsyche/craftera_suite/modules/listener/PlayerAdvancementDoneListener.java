package com.github.scorchedpsyche.craftera_suite.modules.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementDoneListener implements Listener
{
    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
    {
        for ( String criteria : event.getAdvancement().getCriteria() )
        {
            System.out.println(event.getAdvancement().getKey().toString());
            System.out.println(criteria);
        }
    }
}
