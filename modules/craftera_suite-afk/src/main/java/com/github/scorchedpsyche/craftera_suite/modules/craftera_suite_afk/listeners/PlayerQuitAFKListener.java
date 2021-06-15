package com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.main.AFKManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitAFKListener implements Listener {
    private final AFKManager afkManager;

    public PlayerQuitAFKListener(AFKManager afkManager)
    {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        afkManager.playerLogout(e.getPlayer());
    }
}