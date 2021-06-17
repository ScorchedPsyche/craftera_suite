package craftera_suite.craftera_suite_statistics.listeners;

import craftera_suite.craftera_suite_statistics.main.StatisticsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitStatisticsListener implements Listener {
    private final StatisticsManager statisticsManager;

    public PlayerQuitStatisticsListener(StatisticsManager statisticsManager)
    {
        this.statisticsManager = statisticsManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        statisticsManager.playerLogout(e.getPlayer());
    }
}