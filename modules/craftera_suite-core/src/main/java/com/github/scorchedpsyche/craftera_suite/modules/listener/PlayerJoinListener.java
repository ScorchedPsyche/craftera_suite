package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        CraftEraSuiteCore.playerManagerList.put(e.getPlayer().getUniqueId().toString(), new PlayerManager());
        CraftEraSuiteCore.startTitleAndSubtitleSendToPlayersTaskIfNotRunning();
    }
}