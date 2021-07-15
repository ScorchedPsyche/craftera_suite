package com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public final class PlayerChangedWorldSleepListener implements Listener {
    public PlayerChangedWorldSleepListener(SleepManager sleepManager)
    {
        this.sleepManager = sleepManager;
    }

    private final SleepManager sleepManager;

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e)
    {
        sleepManager.playerChangedWorld(e.getPlayer(), e.getFrom());
    }
}