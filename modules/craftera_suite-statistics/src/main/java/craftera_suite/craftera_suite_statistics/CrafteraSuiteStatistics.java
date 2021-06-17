package craftera_suite.craftera_suite_statistics;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import craftera_suite.craftera_suite_statistics.listeners.PlayerJoinStatisticsListener;
import craftera_suite.craftera_suite_statistics.listeners.PlayerQuitStatisticsListener;
import craftera_suite.craftera_suite_statistics.main.StatisticsDatabaseApi;
import craftera_suite.craftera_suite_statistics.main.StatisticsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrafteraSuiteStatistics extends JavaPlugin {
    StatisticsManager statisticsManager;
    StatisticsDatabaseApi statisticsDatabaseApi;
    private Integer updatePlayersLoginState;

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            statisticsDatabaseApi = new StatisticsDatabaseApi();

            // Setup and verify DB tables
            if( statisticsDatabaseApi.setupAndVerifySqlTable() )
            {
                statisticsManager = new StatisticsManager(statisticsDatabaseApi);

                // Add online players to statistics manager
                for (Player player  : Bukkit.getOnlinePlayers() )
                {
                    // Start their login timer (when plugin is reloaded a new entry is added)
                    statisticsManager.startLoginTimerForPlayer(player);
                }

                // Set up repeating task to update AFK state for players
                updatePlayersLoginState = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        this, () -> statisticsManager.updatePlayersLoginTimer(), 0L, 5 * 60 * 20);

                // Listeners
                getServer().getPluginManager().registerEvents(new PlayerJoinStatisticsListener(statisticsManager), this);
                getServer().getPluginManager().registerEvents(new PlayerQuitStatisticsListener(statisticsManager), this);
            } else {
                // Failed to create database tables! Display error and disable plugin
                ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if ( updatePlayersLoginState != null )
        {
            Bukkit.getScheduler().cancelTask(updatePlayersLoginState);
            updatePlayersLoginState = null;
        }
        statisticsManager.disable();

        statisticsManager = null;
        statisticsDatabaseApi = null;
    }
}
