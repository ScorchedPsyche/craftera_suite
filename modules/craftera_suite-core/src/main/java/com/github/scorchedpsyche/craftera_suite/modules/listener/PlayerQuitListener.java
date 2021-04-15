package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {

//    public PlayerQuitListener(HudManager hudManager)
//    {
//        this.hudManager = hudManager;
//    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        CraftEraSuiteCore.playerManagerList.remove(e.getPlayer().getUniqueId().toString());
        if( CraftEraSuiteCore.playerManagerList.isEmpty() )
        {
            CraftEraSuiteCore.cancelTitleAndSubtitleSendToPlayersTaskIfRunning();
        }
    }
}