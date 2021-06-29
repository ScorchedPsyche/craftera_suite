package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.events.EventsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.spectator_mode.SpectatorModeCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.EventsManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventsCommandListener implements Listener
{
    public EventsCommandListener(EventsManager eventsManager)
    {
        this.eventsManager = eventsManager;
    }

    private final EventsManager eventsManager;

    @EventHandler
    public void onEventsCommandEvent(EventsCommandEvent event) {
        if (event.getPlayer() != null) {
            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Events.Name.compact,
                    "OH YEAH BB! ENDERU DURAGON RAIDU");
        }
    }
}
