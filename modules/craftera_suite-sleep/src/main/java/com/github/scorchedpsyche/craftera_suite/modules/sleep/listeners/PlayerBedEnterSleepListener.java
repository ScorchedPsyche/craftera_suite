package com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public final class PlayerBedEnterSleepListener implements Listener {
    public PlayerBedEnterSleepListener(SleepManager sleepManager)
    {
        this.sleepManager = sleepManager;
    }

    private final SleepManager sleepManager;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e)
    {
        sleepManager.playerIsTryingToSleep(e);
    }
}