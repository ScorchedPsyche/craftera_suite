package com.github.scorchedpsyche.craftera_suite.modules.events.commands.hud;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HudToggleCommandEvent extends Event implements Cancellable
{
    private final Player player;
    private static final HandlerList handlers = new HandlerList();

    public HudToggleCommandEvent(Player playerName)
    {
        this.player = playerName;
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

    public Player getPlayer()
    {
        return player;
    }
}
