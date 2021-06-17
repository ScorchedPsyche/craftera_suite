package craftera_suite.craftera_suite_statistics.listeners;

import craftera_suite.craftera_suite_statistics.main.StatisticsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinStatisticsListener implements Listener {
    private final StatisticsManager statisticsManager;

    public PlayerJoinStatisticsListener(StatisticsManager statisticsManager)
    {
        this.statisticsManager = statisticsManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        statisticsManager.startLoginTimerForPlayer(e.getPlayer());
    }
}