package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinSeasonsListener implements Listener {
    private SeasonManager seasonManager;

    public PlayerJoinSeasonsListener(SeasonManager seasonManager)
    {
        this.seasonManager = seasonManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        // Check if there's a valid season active
        if( !seasonManager.isCurrentSeasonValid() )
        {
//            // No valid season.
//            SELECT
//                    select_list
//            FROM
//                    table
//            ORDER BY
//            column_1 ASC,
//            column_2 DESC;
//            seasonManager.createSeason();
        }
    }
}