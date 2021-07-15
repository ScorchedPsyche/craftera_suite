package com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public final class TimeSkipSleepListener implements Listener {
    public TimeSkipSleepListener(SleepManager sleepManager)
    {
        this.sleepManager = sleepManager;
    }

    private final SleepManager sleepManager;


    @EventHandler
    public void onTimeSkip(TimeSkipEvent e)
    {
        if( e.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP )
        {
            e.setCancelled(true);
        }
    }
}