package com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitSleepListener implements Listener {
    public PlayerQuitSleepListener(SleepManager sleepManager)
    {
        this.sleepManager = sleepManager;
    }

    private SleepManager sleepManager;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        sleepManager.playerLeftTheGame(e.getPlayer());
    }
}