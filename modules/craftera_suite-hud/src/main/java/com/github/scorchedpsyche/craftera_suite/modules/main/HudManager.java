package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteHud;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HudManager {
    public HudManager(HudDatabaseAPI hudDatabaseAPI)
    {
        this.hudDatabaseAPI = hudDatabaseAPI;
        onlinePlayersWithHudEnabled = new HashMap<>();

        setup();
    }

    private HashMap<Player, HudPlayerPreferencesModel> onlinePlayersWithHudEnabled;
    public HudDatabaseAPI hudDatabaseAPI;

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
                DatabaseTables.Hud.player_preferences,
                player.getUniqueId().toString(),
                preference );
        player.sendMessage("CES HUD – Toggled preference: " + preference);

        if( !onlinePlayersWithHudEnabled.containsKey(player) )
        {
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
                DatabaseTables.Hud.player_preferences,
                player.getUniqueId().toString(),
                DatabaseTables.Hud.PlayerPreferences.enabled );
    }

    public void setPlayerAsOffline(Player player)
    {
        onlinePlayersWithHudEnabled.remove(player);
    }

    public void setPlayerAsOnline(Player player)
    {
        HudPlayerPreferencesModel hudPlayerPreferences =
                hudDatabaseAPI.getPlayerPreferences(player.getUniqueId().toString());

        if( hudPlayerPreferences != null )
        {
            onlinePlayersWithHudEnabled.putIfAbsent(player, hudPlayerPreferences);
        }
    }

    public void togglePreference(String table)
    {
//        hudDatabaseAPI.
    }

    public void showHudForPlayers()
    {
        for (Map.Entry<Player, HudPlayerPreferencesModel> entry : onlinePlayersWithHudEnabled.entrySet()) {
            Player player = entry.getKey();
            HudPlayerPreferencesModel preferences = entry.getValue();

            String hudText = "";

            if( preferences.showCoordinates() )
            {
                hudText += getPlayerCoordinates(player);
            }

            if( preferences.showNetherPortalCoordinates() )
            {
                hudText += getNetherPortalCoordinates(player);
            }

            if( preferences.showOrientation() )
            {
                hudText += getOrientation(player.getLocation().getYaw());
            }

            player.spigot().sendMessage( ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText( hudText ) );
        }
    }

    private String getPlayerCoordinates(Player player)
    {
        return (int) player.getLocation().getX() + "x " +
                (int) player.getLocation().getY() + "y " +
                (int) player.getLocation().getZ() + "z" ;
    }

    private String getNetherPortalCoordinates(Player player)
    {
        return ((int) player.getLocation().getX()) / 8 + "x " +
                ((int) player.getLocation().getZ()) / 8 + "z" ;
    }

    private String getOrientation(float yaw)
    {
        double rotation = (yaw - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }


        if( rotation > 22.5 && rotation <= 67.5 )
        {
            return "NE";
        } else if ( rotation > 67.5 && rotation <= 112.5 )
        {
            return "E";
        } else if ( rotation > 112.5 && rotation <= 157.5 )
        {
            return "SE";
        } else if ( rotation > 157.5 && rotation <= 202.5 )
        {
            return "S";
        } else if ( rotation > 202.5 && rotation <= 247.5 )
        {
            return "SW";
        } else if ( rotation > 247.5 && rotation <= 292.5 )
        {
            return "W";
        } else if ( rotation > 292.5 && rotation <= 337.5 )
        {
            return "NW";
        }

        return "N";
    }
}
