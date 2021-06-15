package com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.main.AFKManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinAFKListener implements Listener {
    private final AFKManager afkManager;

    public PlayerJoinAFKListener(AFKManager afkManager)
    {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        afkManager.startOrResetAFKTimerForPlayer(e.getPlayer());
    }
}