package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandsEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HudCommandListener implements Listener
{
    private HudManager hudManager;

    public HudCommandListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    @EventHandler
    public void onHudCommandEvent(HudCommandsEvent event)
    {
        if( event.getArgs() != null && event.getArgs().length > 0 )
        {
            switch( event.getArgs()[0] )
            {
                default: // toggle
                    hudManager.toggleHudForPlayer( event.getPlayer() );
                    break;
            }
        } else {
            // TODO: Display HUD subcommand help
            event.getPlayer().sendMessage("FAIL");
        }

//        hudManager.toggleHudForPlayer( event.getPlayer() );
    }
}
