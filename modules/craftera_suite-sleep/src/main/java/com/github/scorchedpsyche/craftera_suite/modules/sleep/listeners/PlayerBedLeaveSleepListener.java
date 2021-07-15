package com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public final class PlayerBedLeaveSleepListener implements Listener {
    public PlayerBedLeaveSleepListener(SleepManager sleepManager)
    {
        this.sleepManager = sleepManager;
    }

    private final SleepManager sleepManager;

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent e)
    {
        sleepManager.playerLeftBed(e.getPlayer());
    }
}