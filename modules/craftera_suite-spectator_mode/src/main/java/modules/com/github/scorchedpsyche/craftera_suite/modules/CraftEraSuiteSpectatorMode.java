package modules.com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.listeners.SpectatorModeCommandListener;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorDatabaseAPI;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteSpectatorMode extends JavaPlugin {
    private SpectatorDatabaseAPI spectatorDatabaseAPI;
    private SpectatorModeManager spectatorModeManager;

    @Override
    public void onEnable() {
        // Check if Core dependency was loaded
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-core") )
        {
            spectatorDatabaseAPI = new SpectatorDatabaseAPI();

            // Setup and verify DB tables
            if( spectatorDatabaseAPI.setupAndVerifySqlTable() )
            {
                spectatorModeManager = new SpectatorModeManager( spectatorDatabaseAPI );
                getServer().getPluginManager().registerEvents(new SpectatorModeCommandListener(spectatorModeManager), this);

//                // Set up repeating task to update HUD for players
//                showHudForPlayersTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
//                        this, () -> hudManager.showHudForPlayers(), 0L, 2);
            } else {
                // Failed to create database tables! Display error and disable plugin
                ConsoleUtils.logError(this.getName(), "Failed to create database tables. Disabling!");
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
        // Plugin shutdown logic
    }
}
