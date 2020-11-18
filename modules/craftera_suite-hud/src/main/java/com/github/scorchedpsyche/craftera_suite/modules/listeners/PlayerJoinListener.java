package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    private final HudManager hudManager;

    public PlayerJoinListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        hudManager.enableHudForPlayer(e.getPlayer());
    }
}