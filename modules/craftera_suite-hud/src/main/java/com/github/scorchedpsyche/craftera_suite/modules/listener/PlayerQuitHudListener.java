package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitHudListener implements Listener {
    private HudManager hudManager;

    public PlayerQuitHudListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        hudManager.disableHudForPlayer(e.getPlayer());
    }
}