package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteHud;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.GlobalHudInfoModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.CollectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HudManager {
    /**
     * Initializes the HUD Manager which controls how to handle players' HUDs.
     * @param hudDatabaseAPI A reference to the HUD Database API
     */
    public HudManager(HudDatabaseAPI hudDatabaseAPI)
    {
        this.hudDatabaseAPI = hudDatabaseAPI;

        onlinePlayersWithHudEnabled = new HashMap<>();
        playerHudManager = new PlayerHudManager();

        // Enables HUD for online players
        Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
            for ( Player player : Bukkit.getOnlinePlayers() )
            {
                enableHudForPlayer(player);
            }
        });

        // Initialize global HUD info shared by all players
        globalHudInfoModel = new GlobalHudInfoModel();
    }

    private final HashMap<Player, HudPlayerPreferencesModel> onlinePlayersWithHudEnabled;
    private HudDatabaseAPI hudDatabaseAPI;
    private final PlayerHudManager playerHudManager;

    // Global HUD display info
    private GlobalHudInfoModel globalHudInfoModel;

    /**
     * Toggles a specific HUD preference for the player.
     * @param player The player to toggle the preference for
     * @param preference The preference's string
     */
    public void togglePreferenceForPlayer(Player player, String preference)
    {
        // Change the preference on the Database
        hudDatabaseAPI.toggleBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                preference );

        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                "Toggled " + ChatColor.AQUA + preference);

        // Change the preference on the list of players with HUD enabled
        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            onlinePlayersWithHudEnabled.get(player).togglePreference(preference);
        } else {
            PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "Your HUD is not enabled, use '/ces hud' to display it");
        }
    }

    /**
     * Sets a specific HUD preference for the player.
     * @param player The player to set the preference for
     * @param preference The preference's string
     * @param value The value to be set for the preference
     */
    public void setPreferenceForPlayer(Player player, String preference, boolean value)
    {
        // Change the preference on the Database
        hudDatabaseAPI.setBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                preference,
                value);

        // Change the preference on the list of players with HUD enabled
        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            onlinePlayersWithHudEnabled.get(player).setPreference(preference, value);

            PlayerUtil.sendMessageWithPluginPrefix( player, SuitePluginManager.Hud.Name.compact,
                    "Set preference " + ChatColor.AQUA + preference + ChatColor.RESET + " with " +
                            ChatColor.AQUA + value);
        } else {
            PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "Your HUD is not enabled, use '/ces hud' to display it");
        }
    }

    /**
     * Shows/hide the HUD for a specific player.
     * @param player The player's UUID to show/hide the HUD for
     */
    public void toggleHudForPlayer(Player player)
    {
        // Check if the executing player has the HUD enabled
        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            // Has the HUD enabled. Disable it!
            PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "is now " + ChatColor.RED + "OFF");

            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), ()
                -> onlinePlayersWithHudEnabled.remove(player));
        } else {
            // Has the HUD disabled. Enable it!
            PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "is now " + ChatColor.GREEN + "ON");

            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), ()
                -> enableHudForPlayer(player));
        }

        hudDatabaseAPI.toggleBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                DatabaseTables.Hud.PlayerPreferencesTable.enabled);
    }

    /**
     * Hides the HUD for a specific player.
     * @param player The player's UUID to hide the HUD for
     */
    public void disableHudForPlayer(Player player)
    {
        onlinePlayersWithHudEnabled.remove(player);
    }

    /**
     * Shows the HUD for a specific player.
     * @param player The player's UUID to show the HUD for
     */
    public void enableHudForPlayer(Player player)
    {
        HudPlayerPreferencesModel hudPlayerPreferences =
                hudDatabaseAPI.getPlayerPreferences(player.getUniqueId().toString());

        if( hudPlayerPreferences != null && hudPlayerPreferences.isHudEnabled() )
        {
            onlinePlayersWithHudEnabled.putIfAbsent(player, hudPlayerPreferences);
        }
    }

    /**
     * Checks which players the plugin should show the HUD for. Also updates the global info shared by all players
     * such as World Time or Server Time.
     */
    public void showHudForPlayers()
    {
        if( !CollectionUtil.isNullOrEmpty(onlinePlayersWithHudEnabled) )
        {
            globalHudInfoModel.updateGlobalHudInfo();

            for (Map.Entry<Player, HudPlayerPreferencesModel> entry : onlinePlayersWithHudEnabled.entrySet()) {
                StringFormattedModel hudText =
                    playerHudManager.getPlayerHudText(entry.getKey(), entry.getValue(), globalHudInfoModel);

                if( hudText.isNullOrEmpty() )
                {
                    hudText.add("HUD empty! Use ").bold().yellowR("/ces hud").add(" to hide the HUD or ").bold()
                        .yellowR("/ces hud help");
                }

                PlayerManager playerManager = CraftEraSuiteCore.playerManagerList.get(entry.getKey().getUniqueId().toString());

                if( !playerManager.subtitle.isEmpty() )
                {
                    playerManager.subtitle.addToStart(" ");
                }

                playerManager.subtitle.addToStart( hudText.toString() );
            }
        }
    }

    /**
     * Checks if the HUD is enabled for a specific player and if they have it on Compact Display Mode.
     * @param player The player to check for
     * @return True if player has the HUD enabled and if it's on Compact Display Mode.
     */
    public boolean isHudEnabledAndDisplayModeCompact(Player player)
    {
        HudPlayerPreferencesModel hudPlayerPreferences =
                hudDatabaseAPI.getPlayerPreferences(player.getUniqueId().toString());

        return hudPlayerPreferences != null && hudPlayerPreferences.isHudEnabled() && !hudPlayerPreferences.isDisplayModeExtended();
    }
}
