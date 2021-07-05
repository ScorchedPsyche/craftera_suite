package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.PlayerManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        CraftEraSuiteCore.playerManagerList.put(e.getPlayer().getUniqueId().toString(), new PlayerManager(e.getPlayer()));
        CraftEraSuiteCore.startTitleAndSubtitleSendToPlayersTaskIfNotRunning();

        CraftEraSuiteCore.serverManager.loadAndVerifyServerMessages();

        if( !CraftEraSuiteCore.serverManager.messages.isEmpty() )
        {
            PlayerUtil.sendMessagesWithPluginPrefixOnePerLine(e.getPlayer(), SuitePluginManager.Core.Name.compact,
                    CraftEraSuiteCore.serverManager.messages);
        }
    }
}