package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteHud;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HudManager {
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

//        Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class),
//            () -> Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
//                for ( Player player : Bukkit.getOnlinePlayers() )
//                {
//                    enableHudForPlayer(player);
//                }
//        }));
    }

    private final HashMap<Player, HudPlayerPreferencesModel> onlinePlayersWithHudEnabled;
    private HudDatabaseAPI hudDatabaseAPI;
    private final PlayerHudManager playerHudManager;

    /**
     * Toggles a specific HUD preference for the player.
     * @param player
     * @param preference
     */
    public void togglePreferenceForPlayer(Player player, String preference)
    {
        hudDatabaseAPI.toggleBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                preference );

        PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                "Toggled " + ChatColor.AQUA + preference);

        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            onlinePlayersWithHudEnabled.get(player).togglePreference(preference);
        } else {
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "Your HUD is not enabled, use '/ces hud' to display it");
        }
    }

    public void setPreferenceForPlayer(Player player, String preference, boolean value)
    {
        hudDatabaseAPI.setBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                preference,
                value);

        PlayerUtils.sendMessageWithPluginPrefix( player, SuitePluginManager.Hud.Name.compact,
                "Set preference " + ChatColor.AQUA + preference + ChatColor.RESET + " with " +
                        ChatColor.AQUA + value);

        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            onlinePlayersWithHudEnabled.get(player).setPreference(preference, value);
        } else {
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "Your HUD is not enabled, use '/ces hud' to display it");
        }
    }

    public void toggleHudForPlayer(Player player)
    {
        // Check if the executing player has the HUD enabled
        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            // Has the HUD enabled. Disable it!
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "is now " + ChatColor.RED + "OFF");

            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                onlinePlayersWithHudEnabled.remove(player);
            });

//            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
//                Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
//                    onlinePlayersWithHudEnabled.remove(player);
//                });
//            });
        } else {
            // Has the HUD disabled. Enable it!
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact,
                                                    "is now " + ChatColor.GREEN + "ON");

            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                enableHudForPlayer(player);
            });
//            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
//                Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
//                    enableHudForPlayer(player);
//                });
//            });
        }

        hudDatabaseAPI.toggleBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                DatabaseTables.Hud.PlayerPreferencesTable.enabled);
    }

    public void disableHudForPlayer(Player player)
    {
        onlinePlayersWithHudEnabled.remove(player);
    }

    public void enableHudForPlayer(Player player)
    {
        HudPlayerPreferencesModel hudPlayerPreferences =
                hudDatabaseAPI.getPlayerPreferences(player.getUniqueId().toString());

        if( hudPlayerPreferences != null && hudPlayerPreferences.isHudEnabled() )
        {
            onlinePlayersWithHudEnabled.putIfAbsent(player, hudPlayerPreferences);
        }
    }

    public void showHudForPlayers()
    {
        for (Map.Entry<Player, HudPlayerPreferencesModel> entry : onlinePlayersWithHudEnabled.entrySet()) {
            StringBuilder hudText = playerHudManager.getPlayerHudText(entry.getKey(), entry.getValue());

            if( StringUtils.isStringBuilderNullOrEmpty(hudText) )
            {
                hudText.append("HUD empty! Use ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("/ces hud")
                       .append(ChatColor.RESET).append(" to hide the HUD or ").append(ChatColor.YELLOW)
                       .append(ChatColor.BOLD).append("/ces hud help");
            }

            entry.getKey().spigot().sendMessage( ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText( hudText.toString() ) );
        }
    }
}
