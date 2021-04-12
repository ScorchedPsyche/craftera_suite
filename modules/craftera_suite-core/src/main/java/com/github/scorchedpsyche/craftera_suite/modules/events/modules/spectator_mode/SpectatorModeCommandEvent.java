package com.github.scorchedpsyche.craftera_suite.modules.events.modules.spectator_mode;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.ICommandEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpectatorModeCommandEvent extends Event implements Cancellable, ICommandEvent
{
    private final Player player;
    private final String[] args;
    private static final HandlerList handlers = new HandlerList();

    public SpectatorModeCommandEvent(Player player, String[] args)
    {
        this.player = player;
        this.args = args;
    }

    @Override
    public boolean isCancelled()
    {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel)
    {

    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public Player getPlayer()
    {
        return player;
    }

    @Override
    public String[] getArgs()
    {
        return args;
    }
}
