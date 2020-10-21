package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteHud;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ItemStackUtils;
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
        playerUtils = new PlayerUtils();
        playerHudManager = new PlayerHudManager();
        itemStackUtils = new ItemStackUtils();
        stringUtils = new StringUtils();

        setup();
    }

    private HashMap<Player, HudPlayerPreferencesModel> onlinePlayersWithHudEnabled;
    public HudDatabaseAPI hudDatabaseAPI;
    public PlayerUtils playerUtils;
    private PlayerHudManager playerHudManager;
    public ItemStackUtils itemStackUtils;
    private StringUtils stringUtils;

    public void setup()
    {
        Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
            Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                for ( Player player : Bukkit.getOnlinePlayers() )
                {
                    setPlayerAsOnline(player);
                }
            });
        });
    }

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

        player.sendMessage("CES HUD – Toggled preference: " + preference);

        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            onlinePlayersWithHudEnabled.get(player).togglePreference(preference);
        } else {
            player.sendMessage("CES HUD – Your HUD is not enabled, use '/ces hud' to display it");
        }
    }

    public void setPreferenceForPlayer(Player player, String preference, boolean value)
    {
        hudDatabaseAPI.setBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                preference,
                value);

        player.sendMessage("CES HUD – set " + preference + " with " + value);

        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            onlinePlayersWithHudEnabled.get(player).setPreference(preference, value);
        } else {
            player.sendMessage("CES HUD – Your HUD is not enabled, use '/ces hud' to display it");
        }
    }

    public void toggleHudForPlayer(Player player)
    {
        // Check if the executing player has the HUD enabled
        if( onlinePlayersWithHudEnabled.containsKey(player) )
        {
            // Has the HUD enabled, then
            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                    //                    hudDatabaseAPI.disableHudForPlayer( player.getUniqueId().toString() );
                    onlinePlayersWithHudEnabled.remove(player);
                    player.sendMessage("CES HUD – OFF");
                });
            });
        } else {
            // Add
            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                    //                    hudDatabaseAPI.enableHudForPlayer( player.getUniqueId().toString() );
                    setPlayerAsOnline(player);
                    player.sendMessage("CES HUD – ON");
                });
            });
        }

        hudDatabaseAPI.toggleBooleanForPlayer(
                DatabaseTables.Hud.player_preferences_TABLENAME,
                player.getUniqueId().toString(),
                DatabaseTables.Hud.PlayerPreferencesTable.enabled);
    }

    public void setPlayerAsOffline(Player player)
    {
        onlinePlayersWithHudEnabled.remove(player);
    }

    public void setPlayerAsOnline(Player player)
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

            if( stringUtils.isStringBuilderNullOrEmpty(hudText) )
            {
                hudText.append("HUD empty! Use " + ChatColor.YELLOW + ChatColor.BOLD + "/ces hud" + ChatColor.RESET +
                                       " to hide the HUD or " + ChatColor.YELLOW + ChatColor.BOLD + "/ces hud help");
            }

            entry.getKey().spigot().sendMessage( ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText( hudText.toString() ) );
        }
    }
}
