package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.GamesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitGamesListener implements Listener {

    public PlayerQuitGamesListener(GamesManager gamesManager)
    {
        this.gamesManager = gamesManager;
    }

    private GamesManager gamesManager;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        gamesManager.playerQuitServer(e.getPlayer());
    }
}