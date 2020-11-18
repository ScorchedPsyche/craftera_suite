package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {
    private HudManager hudManager;

    public PlayerQuitListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        hudManager.disableHudForPlayer(e.getPlayer());
    }
}