package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteHud;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ItemStackUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HudManager {
    public HudManager(HudDatabaseAPI hudDatabaseAPI)
    {
        this.hudDatabaseAPI = hudDatabaseAPI;
        onlinePlayersWithHudEnabled = new HashMap<>();
        playerUtils = new PlayerUtils();
        itemStackUtils = new ItemStackUtils();

        setup();
    }

    private HashMap<Player, HudPlayerPreferencesModel> onlinePlayersWithHudEnabled;
    public HudDatabaseAPI hudDatabaseAPI;
    public PlayerUtils playerUtils;
    public ItemStackUtils itemStackUtils;

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


    public void showHudForPlayers()
    {
        for (Map.Entry<Player, HudPlayerPreferencesModel> entry : onlinePlayersWithHudEnabled.entrySet()) {
            Player player = entry.getKey();
            HudPlayerPreferencesModel preferences = entry.getValue();

            String hudText = "";

            if( preferences.isDisplayModeExpanded() )
            {
                // DISPLAY MODE: EXPANDED

                // Player XYZ coordinates
                if( preferences.showCoordinates() )
                {
                    hudText += formatPlayerCoordinatesExpanded(
                            playerUtils.getCoordinateRoundedX(player),
                            playerUtils.getCoordinateRoundedY(player),
                            playerUtils.getCoordinateRoundedZ(player) );
                }

                if( preferences.showNetherPortalCoordinates() )
                {
                    hudText += " " + formatNetherPortalCoordinatesExpanded(
                            playerUtils.getCoordinateRoundedX(player),
                            playerUtils.getCoordinateRoundedZ(player),
                            playerUtils.getEnvironment(player) );
                }

                // Player orientation
                if( preferences.showOrientation() )
                {
                    hudText += " " + formatPlayerOrientation(player.getLocation().getYaw());
                }

                if( preferences.showToolDurability() )
                {
                    hudText += " " + formatToolDurabilityExpanded("Main", player.getInventory().getItemInMainHand());
                    hudText += " " + formatToolDurabilityExpanded("Off", player.getInventory().getItemInOffHand());
                }

                if( preferences.showServerTPS() )
                {
                    hudText += " " + getServerTpsExpanded(preferences.colorizeServerTps());
                }
            } else {
                // DISPLAY MODE: COMPACT

                // Player XYZ coordinates
                if( preferences.showCoordinates() )
                {
                    hudText += formatPlayerCoordinatesCompact(
                            playerUtils.getCoordinateRoundedX(player),
                            playerUtils.getCoordinateRoundedY(player),
                            playerUtils.getCoordinateRoundedZ(player) );
                }

                if( preferences.showNetherPortalCoordinates() )
                {
                    hudText += " " + formatNetherPortalCoordinatesCompact(
                            playerUtils.getCoordinateRoundedX(player),
                            playerUtils.getCoordinateRoundedZ(player),
                            playerUtils.getEnvironment(player) );
                }

                // Player orientation
                if( preferences.showOrientation() )
                {
                    hudText += " " + formatPlayerOrientation(player.getLocation().getYaw());
                }

                if( preferences.showToolDurability() )
                {
                    hudText += " " + formatToolDurabilityCompact(player.getInventory().getItemInMainHand());
                    hudText += " " + formatToolDurabilityCompact(player.getInventory().getItemInOffHand());
                }

                if( preferences.showServerTPS() )
                {
                    hudText += " " + getServerTpsCompact(preferences.colorizeServerTps());
                }
            }

            if( preferences.showServerTime() )
            {
                hudText += " " + getServerTime();
            }

            if( preferences.showWorldTime() )
            {
                hudText += " " + getWorldTime(preferences.colorizeWorldTime());
            }

            player.spigot().sendMessage( ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText( hudText ) );
        }
    }

    private String formatPlayerCoordinatesCompact(int x, int y, int z)
    {
        return  x + "" + ChatColor.GOLD + "x " + ChatColor.RESET +
                y + "" + ChatColor.GOLD + "y " + ChatColor.RESET +
                z + "" + ChatColor.GOLD + "z " + ChatColor.RESET;
    }

    private String formatPlayerCoordinatesExpanded(int x, int y, int z)
    {
        return ChatColor.GOLD +  "XYZ: " + ChatColor.RESET + x + " " + y + " " + z + ChatColor.RESET ;
    }

    private String formatPlayerOrientation(float yaw)
    {
        String orientation = ChatColor.AQUA.toString();

        double rotation = (yaw - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        if( rotation > 22.5 && rotation <= 67.5 )
        {
            orientation += "NE";
        } else if ( rotation > 67.5 && rotation <= 112.5 )
        {
            orientation += "E";
        } else if ( rotation > 112.5 && rotation <= 157.5 )
        {
            orientation += "SE";
        } else if ( rotation > 157.5 && rotation <= 202.5 )
        {
            orientation += "S";
        } else if ( rotation > 202.5 && rotation <= 247.5 )
        {
            orientation += "SW";
        } else if ( rotation > 247.5 && rotation <= 292.5 )
        {
            orientation += "W";
        } else if ( rotation > 292.5 && rotation <= 337.5 )
        {
            orientation += "NW";
        } else {
            orientation += "N";
        }

        return orientation + ChatColor.RESET;
    }

    private String formatNetherPortalCoordinatesCompact(int x, int z, World.Environment environment)
    {
        if( environment.equals(World.Environment.NETHER) )
        {
            return  x * 8 + "" + ChatColor.RED + "x " + ChatColor.RESET +
                    z * 8 + "" + ChatColor.RED + "z" + ChatColor.RESET  ;
        } else if ( environment.equals(World.Environment.NORMAL) ) {
            return  x / 8 + "" + ChatColor.RED + "x " + ChatColor.RESET +
                    z / 8 + "" + ChatColor.RED + "z" + ChatColor.RESET  ;
        }

        return "–";
    }

    private String formatNetherPortalCoordinatesExpanded(int x, int z, World.Environment environment)
    {
        String portal =  ChatColor.GOLD + "Portal XZ: " + ChatColor.RESET;

        if( environment.equals(World.Environment.NETHER) )
        {
            portal += x * 8 + " " + z * 8;
        } else if ( environment.equals(World.Environment.NORMAL) ) {
            portal += x / 8 + " " + z / 8;
        } else {
            portal += "-";
        }

        return portal;
    }

    private String getServerTime()
    {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(d1);
    }

    private String getServerTpsCompact(boolean colorize)
    {
        if(colorize)
        {
            return  colorizeServerTps( (short) MinecraftServer.getServer().recentTps[0] ) + "/" +
                    colorizeServerTps( (short) MinecraftServer.getServer().recentTps[1] )  + "/" +
                    colorizeServerTps( (short) MinecraftServer.getServer().recentTps[2] ) ;
        }

        return  (short) MinecraftServer.getServer().recentTps[0] + "/" +
                (short) MinecraftServer.getServer().recentTps[1]  + "/" +
                (short) MinecraftServer.getServer().recentTps[2] ;
    }

    private String getServerTpsExpanded(boolean colorize)
    {
        return  ChatColor.GOLD + "TPS: " + ChatColor.RESET + getServerTpsCompact(colorize) ;
    }

    private String colorizeServerTps(short tps)
    {
        if( tps >= 20 )
        {
            return ChatColor.GREEN + "20" + ChatColor.RESET;
        } else if ( tps < 20 && tps >= 15 ){
            return ChatColor.YELLOW + Short.toString(tps) + ChatColor.RESET;
        }

        return ChatColor.RED + Short.toString(tps) + ChatColor.RESET;
    }

    private String formatToolDurabilityCompact(ItemStack item)
    {
        Integer itemRemainingDurability = itemStackUtils.getItemRemainingDurability(item);

        if ( itemRemainingDurability != null )
        {
            String durability = "";

            if( itemRemainingDurability < 50 )
            {
                durability += ChatColor.RED + Integer.toString(itemRemainingDurability) + ChatColor.RESET;
            } else if ( itemRemainingDurability < 100  ) {
                durability += ChatColor.YELLOW + Integer.toString(itemRemainingDurability) + ChatColor.RESET;
            } else {
                durability += Integer.toString(itemRemainingDurability);
            }

            durability += "/" + item.getType().getMaxDurability();

            return durability;
        } else {
            return "–";
        }
    }

    private String formatToolDurabilityExpanded(String slot, ItemStack item)
    {
        return ChatColor.GOLD + slot + ": " + ChatColor.RESET
                + formatToolDurabilityCompact(item);
    }

    private String getWorldTime(boolean colorized)
    {
        World overworld = Bukkit.getWorld("world");

        String worldTime = Long.toString(overworld.getTime());

        if( colorized )
        {
             if ( overworld.getTime() >= 2000 && overworld.getTime() <= 9000 )
            {
                // Villager work hours
                worldTime = ChatColor.GREEN + worldTime + ChatColor.RESET;
            } else if( overworld.hasStorm() )
            {
                // Weather not clear
                if ( overworld.getTime() >= 12969 && overworld.getTime() <= 23031 )
                {
                    // Monsters are spawning
                    worldTime = ChatColor.RED + worldTime + ChatColor.RESET;
                } else if ( overworld.getTime() >= 12010 && overworld.getTime() < 12969 )
                {
                    // Beds can be used
                    worldTime = ChatColor.YELLOW + worldTime + ChatColor.RESET;
                }
            } else if( !overworld.hasStorm() )
            {
                // Weather clear
                if ( overworld.getTime() >= 13188 && overworld.getTime() <= 22812 )
                {
                    // Monsters are spawning
                    worldTime = ChatColor.RED + worldTime + ChatColor.RESET;
                } else if ( overworld.getTime() >= 12542 && overworld.getTime() < 13188 )
                {
                    // Beds can be used
                    worldTime = ChatColor.YELLOW + worldTime + ChatColor.RESET;
                }
            }
        }

        return worldTime;
    }
}
