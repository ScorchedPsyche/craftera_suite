package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinHudListener implements Listener {
    private final HudManager hudManager;

    public PlayerJoinHudListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        hudManager.enableHudForPlayer(e.getPlayer());
    }
}