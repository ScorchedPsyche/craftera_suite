package com.github.scorchedpsyche.craftera_suite.modules.listeners.commands;

import com.github.scorchedpsyche.craftera_suite.modules.events.commands.hud.HudToggleCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HudToggleCommandListener implements Listener
{
    private HudManager hudManager;

    public HudToggleCommandListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    @EventHandler
    public void onHudCommandEvent(HudToggleCommandEvent event)
    {
        hudManager.toggleHudForPlayer( event.getPlayer() );
    }
}
