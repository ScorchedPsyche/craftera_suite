package craftera_suite.craftera_suite_statistics.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import craftera_suite.craftera_suite_statistics.model.PlayerLoginModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatisticsManager {
    private StatisticsDatabaseApi statisticsDatabaseApi;
    private HashMap<UUID, PlayerLoginModel> players = new HashMap<>();

    public StatisticsManager(StatisticsDatabaseApi statisticsDatabaseApi)
    {
        this.statisticsDatabaseApi = statisticsDatabaseApi;
    }

    public void startLoginTimerForPlayer(Player player)
    {
        // Check if player is already on the list
        if ( players.containsKey(player.getUniqueId()) )
        {
            // Yes. Just start the timer
            players.get(player.getUniqueId()).timerStart();
        } else {
            // Not on the list. Add them and start the timer
            players.put( player.getUniqueId(), new PlayerLoginModel(player).timerStart() );
            statisticsDatabaseApi.addLoginForPlayerIfNotExists(players.get(player.getUniqueId()));
        }
    }

    /**
     * Processes player in case of a logout or server shutdown.
     * @param player Player that will be processed
     */
    public void playerLogout(Player player)
    {
        updatePlayerLoginTimer(player);

        players.remove(player.getUniqueId());
    }


    /**
     * Updates player final and total Statistics timer.
     * @param player Player who's timer will be updated
     */
    public void updatePlayerLoginTimer(Player player)
    {
        PlayerLoginModel playerLoginModel = players.get(player.getUniqueId()).updateLoginTimer();

        if ( !statisticsDatabaseApi.updateLoginTimerForPlayer(playerLoginModel) )
        {
            ConsoleUtil.logError(SuitePluginManager.Statistics.Name.full,
                    "Unable to update player Statistics timer. Report to developer!");
        }
    }

    public void updatePlayersLoginTimer()
    {
        // Iterate through all players
        for (Map.Entry<UUID, PlayerLoginModel> entry : players.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getValue().getPlayer().getUniqueId());

            // Check if player fetch was successful
            if (player != null) {
                // Increase timer
                updatePlayerLoginTimer(player);
            } else {
                ConsoleUtil.logError(SuitePluginManager.Statistics.Name.full,
                        "Failed to update player login state. Report to developer!");
            }
        }
    }

    public void disable() {
        // Iterate through all players
        for (Map.Entry<UUID, PlayerLoginModel> entry : players.entrySet())
        {
            Player player = Bukkit.getPlayer(entry.getValue().getPlayer().getUniqueId());

            // Check if player fetch was successful
            if (player != null) {
                // Increase timer
                updatePlayerLoginTimer(player);
            } else {
                ConsoleUtil.logError(SuitePluginManager.Statistics.Name.full,
                        "Failed to disable Statistics Manager. Report to developer!");
            }
        }
        players.clear();
    }
}
