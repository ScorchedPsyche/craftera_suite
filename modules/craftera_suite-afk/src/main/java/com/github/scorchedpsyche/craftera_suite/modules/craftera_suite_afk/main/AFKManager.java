package com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.main;

import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.model.PlayerAFKModel;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager {
    private AFKDatabaseApi afkDatabaseApi;
    private final long timeItTakesToGoAFK = 5; // seconds
    private HashMap<UUID, PlayerAFKModel> players = new HashMap<>();

    public AFKManager(AFKDatabaseApi afkDatabaseApi)
    {
        this.afkDatabaseApi = afkDatabaseApi;
    }

    public void startOrResetAFKTimerForPlayer(Player player)
    {
        // Check if player is already on the list
        if ( players.containsKey(player.getUniqueId()) )
        {
            // Yes. Just restart the timer
            players.get(player.getUniqueId()).timerStartOrRestart();
        } else {
            // Not on the list. Add them and start the timer
            players.put( player.getUniqueId(), new PlayerAFKModel(player).timerStartOrRestart() );
        }
    }

    public void playerWentAFK(Player player)
    {
        afkDatabaseApi.addAFKForPlayerIfNotExists( players.get(player.getUniqueId()).markAsAFK(timeItTakesToGoAFK).updateAFKTimer() );
    }


    /**
     * Updates player final and total AFK timer.
     * @param player Player who's timer will be updated
     */
    public void updatePlayerAFKTimer(Player player)
    {
        PlayerAFKModel playerAFKModel = players.get(player.getUniqueId()).updateAFKTimer();

        if ( !afkDatabaseApi.updateAFKTimerForPlayer(playerAFKModel) )
        {
            ConsoleUtil.logError(SuitePluginManager.AFK.Name.full,
                    "Unable to update player AFK timer. Report to developer!");
        }
    }

    /**
     * Updates player final and total AFK timer and restarts their timer.
     * @param player Player who's timer will be updated
     */
    public void playerLeftAFK(Player player)
    {
        updatePlayerAFKTimer(player);
        startOrResetAFKTimerForPlayer(player);
    }

    /**
     * Processes player in case of a logout or server shutdown.
     * @param player Player that will be processed
     */
    public void playerLogout(Player player)
    {
        PlayerAFKModel playerAFKModel = players.get(player.getUniqueId());

        // Check if player is AFK
        if( playerAFKModel.isAFK() )
        {
            // Is AFK. Update AFK timer before removal
            playerAFKModel.updateAFKTimer();
        }
        players.remove(player.getUniqueId());
    }

    public void updatePlayersAFKState()
    {
        // Iterate through all players
        for (Map.Entry<UUID, PlayerAFKModel> entry : players.entrySet())
        {
            PlayerAFKModel playerAFKModel = entry.getValue();

            // Check if player has moved
            if( playerAFKModel.hasMoved() )
            {
                // Moved

                // Check if AFK
                if( playerAFKModel.isAFK() )
                {
                    // Is AFK. Then player left AFK
                    playerAFKModel.markAsNotAFK();
                    playerLeftAFK(Bukkit.getPlayer(playerAFKModel.getPlayer().getUniqueId()));
                }

                // Restart AFK timer
                playerAFKModel.timerStartOrRestart();
            } else {
                // Player hasn't moved

                // Check if player is AFK
                if ( !playerAFKModel.isAFK() )
                {
                    // Not AFK. Check for how long
                    if( playerAFKModel.hasBeenAFKFor(this.timeItTakesToGoAFK) )
                    {
                        // AFK for too long. Mark them as AFK
                        playerWentAFK( Bukkit.getPlayer(playerAFKModel.getPlayer().getUniqueId()) );
                    }
                } else {
                    // Player is AFK already. Increase timer
                    updatePlayerAFKTimer( Bukkit.getPlayer(playerAFKModel.getPlayer().getUniqueId()) );
                }
            }
        }
    }
}
