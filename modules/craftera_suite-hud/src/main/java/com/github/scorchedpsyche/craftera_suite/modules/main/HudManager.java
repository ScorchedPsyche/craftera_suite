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
                DatabaseTables.Hud.player_preferences,
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
                DatabaseTables.Hud.player_preferences,
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



//            if( preferences.isDisplayModeExtended() )
//            {
//                // DISPLAY MODE: EXPANDED
//
//                // Player XYZ coordinates
//                if( preferences.showCoordinates() )
//                {
//                    hudText += formatPlayerCoordinatesExpanded(
//                            preferences.colorizeCoordinates(),
//                            playerUtils.getCoordinateRoundedX(player),
//                            playerUtils.getCoordinateRoundedY(player),
//                            playerUtils.getCoordinateRoundedZ(player) );
//                }
//
//                if( preferences.showNetherPortalCoordinates() )
//                {
//                    hudText += " " + formatNetherPortalCoordinatesExpanded(
//                            preferences.colorizeNetherPortalCoordinates(),
//                            playerUtils.getCoordinateRoundedX(player),
//                            playerUtils.getCoordinateRoundedZ(player),
//                            playerUtils.getEnvironment(player) );
//                }
//
//                // Player orientation
//                if( preferences.showOrientation() )
//                {
//                    hudText += " " + formatPlayerOrientation(
//                            preferences.colorizePlayerOrientation(),
//                            player.getLocation().getYaw());
//                }
//
//                if( preferences.showToolDurability() )
//                {
//                    String mainHandDurability = formatToolDurabilityExpanded(
//                            preferences.colorizeToolDurability(),
//                            "Main",
//                            player.getInventory().getItemInMainHand());
//                    String offHandDurability = formatToolDurabilityExpanded(
//                            preferences.colorizeToolDurability(),
//                            "Off",
//                            player.getInventory().getItemInOffHand());
//
//                    if ( mainHandDurability != null )
//                    {
//                        hudText += " " + mainHandDurability;
//                    }
//                    if ( offHandDurability != null )
//                    {
//                        hudText += " " + offHandDurability;
//                    }
//                }
//
//                if( preferences.showServerTPS() )
//                {
//                    hudText += " " + getServerTpsExpanded(preferences.colorizeServerTps());
//                }
//            } else {
//                // DISPLAY MODE: COMPACT
//
//                // Player XYZ coordinates
//                if( preferences.showCoordinates() )
//                {
//                    hudText += formatPlayerCoordinatesCompact(
//                            preferences.colorizeToolDurability(),
//                            playerUtils.getCoordinateRoundedX(player),
//                            playerUtils.getCoordinateRoundedY(player),
//                            playerUtils.getCoordinateRoundedZ(player) );
//                }
//
//                if( preferences.showNetherPortalCoordinates() )
//                {
//                    hudText += " " + formatNetherPortalCoordinatesCompact(
//                            preferences.colorizeToolDurability(),
//                            playerUtils.getCoordinateRoundedX(player),
//                            playerUtils.getCoordinateRoundedZ(player),
//                            playerUtils.getEnvironment(player) );
//                }
//
//                // Player orientation
//                if( preferences.showOrientation() )
//                {
//                    hudText += " " + formatPlayerOrientation(
//                            preferences.colorizePlayerOrientation(),
//                            player.getLocation().getYaw());
//                }
//
//                if( preferences.showToolDurability() )
//                {
//                    String mainHandDurability = formatToolDurabilityCompact(
//                            preferences.colorizeToolDurability(), player.getInventory().getItemInMainHand());
//                    String offHandDurability = formatToolDurabilityCompact(
//                            preferences.colorizeToolDurability(), player.getInventory().getItemInOffHand());
//
//                    if ( mainHandDurability != null )
//                    {
//                        hudText += " " + mainHandDurability;
//                    }
//                    if ( offHandDurability != null )
//                    {
//                        hudText += " " + offHandDurability;
//                    }
//                }
//
//                if( preferences.showServerTPS() )
//                {
//                    hudText += " " + getServerTpsCompact(preferences.colorizeServerTps());
//                }
//            }
//
//            if( preferences.showServerTime() )
//            {
//                hudText += " " + getServerTime();
//            }
//
//            if( preferences.showWorldTime() )
//            {
//                hudText += " " + getWorldTime(preferences.colorizeWorldTime());
//            }
        }
    }

//    private String formatPlayerCoordinatesCompact(boolean colorize, int x, int y, int z)
//    {
//        if(colorize)
//        {
//            return  x + "" + ChatColor.GOLD + "x " + ChatColor.RESET +
//                    y + "" + ChatColor.GOLD + "y " + ChatColor.RESET +
//                    z + "" + ChatColor.GOLD + "z" + ChatColor.RESET;
//        }
//
//        return  x + "x " + y + "y " + z + "z";
//    }
//
//    private String formatPlayerCoordinatesExpanded(boolean colorize, int x, int y, int z)
//    {
//        if(colorize)
//        {
//            return ChatColor.GOLD + "XYZ: " + ChatColor.RESET + x + " " + y + " " + z + ChatColor.RESET;
//        }
//        return "XYZ: " + x + " " + y + " " + z;
//    }
//
//    private String formatPlayerOrientation(boolean colorize, float yaw)
//    {
//        String orientation = (colorize) ? ChatColor.AQUA.toString() : "";
//
//        double rotation = (yaw - 180) % 360;
//        if (rotation < 0) {
//            rotation += 360.0;
//        }
//
//        if( rotation > 22.5 && rotation <= 67.5 )
//        {
//            orientation += "NE";
//        } else if ( rotation > 67.5 && rotation <= 112.5 )
//        {
//            orientation += "E";
//        } else if ( rotation > 112.5 && rotation <= 157.5 )
//        {
//            orientation += "SE";
//        } else if ( rotation > 157.5 && rotation <= 202.5 )
//        {
//            orientation += "S";
//        } else if ( rotation > 202.5 && rotation <= 247.5 )
//        {
//            orientation += "SW";
//        } else if ( rotation > 247.5 && rotation <= 292.5 )
//        {
//            orientation += "W";
//        } else if ( rotation > 292.5 && rotation <= 337.5 )
//        {
//            orientation += "NW";
//        } else {
//            orientation += "N";
//        }
//
//        return (colorize) ? orientation + ChatColor.RESET : orientation;
//    }
//
//    private String formatNetherPortalCoordinatesCompact(boolean colorize, int x, int z, World.Environment environment)
//    {
//        if( environment.equals(World.Environment.NETHER) )
//        {
//            if(colorize)
//            {
//                return  x * 8 + "" + ChatColor.RED + "x " + ChatColor.RESET +
//                        z * 8 + "" + ChatColor.RED + "z" + ChatColor.RESET;
//            }
//            return  x * 8 + "x " + z * 8 + "z";
//        } else if ( environment.equals(World.Environment.NORMAL) ) {
//            if(colorize)
//            {
//                return  x / 8 + "" + ChatColor.RED + "x " + ChatColor.RESET +
//                        z / 8 + "" + ChatColor.RED + "z" + ChatColor.RESET;
//            }
//            return  x / 8 + "x " + z / 8 + "z";
//        }
//
//        return "–";
//    }
//
//    private String formatNetherPortalCoordinatesExpanded(boolean colorize, int x, int z, World.Environment environment)
//    {
//        String portal = (colorize) ?
//                ChatColor.GOLD + "Portal XZ: " + ChatColor.RESET : "Portal XZ: ";
//
//        if( environment.equals(World.Environment.NETHER) )
//        {
//            portal += x * 8 + " " + z * 8;
//        } else if ( environment.equals(World.Environment.NORMAL) ) {
//            portal += x / 8 + " " + z / 8;
//        } else {
//            portal += "-";
//        }
//
//        return portal;
//    }
//
//    private String getServerTime()
//    {
//        Date d1 = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
//        return df.format(d1);
//    }
//
//    private String getServerTpsCompact(boolean colorize)
//    {
//        if(colorize)
//        {
//            return  colorizeServerTps( (short) MinecraftServer.getServer().recentTps[0] );
//        }
//
//        return  (short) MinecraftServer.getServer().recentTps[0] + "" ;
//    }
//
//    private String getServerTpsExpanded(boolean colorize)
//    {
//        String tps = (colorize) ? ChatColor.GOLD + "TPS: " + ChatColor.RESET : "TPS: "  ;
//
//        return tps + getServerTpsCompact(colorize);
//    }
//
//    private String colorizeServerTps(short tps)
//    {
//        if( tps >= 20 )
//        {
//            return ChatColor.GREEN + "20" + ChatColor.RESET;
//        } else if ( tps < 20 && tps >= 15 ){
//            return ChatColor.YELLOW + Short.toString(tps) + ChatColor.RESET;
//        }
//
//        return ChatColor.RED + Short.toString(tps) + ChatColor.RESET;
//    }
//
//    @Nullable
//    private String formatToolDurabilityCompact(boolean colorize, ItemStack item)
//    {
//        Integer itemRemainingDurability = itemStackUtils.getItemRemainingDurability(item);
//
//        if ( itemRemainingDurability != null )
//        {
//            String durability = "";
//
//            if(colorize)
//            {
//                if( itemRemainingDurability < 25 )
//                {
//                    durability += ChatColor.RED + Integer.toString(itemRemainingDurability) + ChatColor.RESET;
//                } else if ( itemRemainingDurability < 50  ) {
//                    durability += ChatColor.YELLOW + Integer.toString(itemRemainingDurability) + ChatColor.RESET;
//                } else {
//                    durability += Integer.toString(itemRemainingDurability);
//                }
//            } else {
//                durability = Integer.toString(itemRemainingDurability);
//            }
//
//            durability += "/" + item.getType().getMaxDurability();
//
//            return durability;
//        }
//
//        return null;
//    }
//
//    @Nullable
//    private String formatToolDurabilityExpanded(boolean colorize, String slot, ItemStack item)
//    {
//        String toolDurabilityCompact = formatToolDurabilityCompact(colorize, item);
//
//        if(toolDurabilityCompact != null)
//        {
//            String durability = "";
//
//            if(colorize)
//            {
//                durability += ChatColor.GOLD + slot + ": " + ChatColor.RESET;
//            } else {
//                durability += slot + ": ";
//            }
//
//            return durability + formatToolDurabilityCompact(colorize, item);
//        }
//
//        return null;
//    }
//
//    private String getWorldTime(boolean colorized)
//    {
//        World overworld = Bukkit.getWorld("world");
//
//        String worldTime = Long.toString(overworld.getTime());
//
//        if( colorized )
//        {
//             if ( overworld.getTime() >= 2000 && overworld.getTime() <= 9000 )
//            {
//                // Villager work hours
//                worldTime = ChatColor.GREEN + worldTime + ChatColor.RESET;
//            } else if( overworld.hasStorm() )
//            {
//                // Weather not clear
//                if ( overworld.getTime() >= 12969 && overworld.getTime() <= 23031 )
//                {
//                    // Monsters are spawning
//                    worldTime = ChatColor.RED + worldTime + ChatColor.RESET;
//                } else if ( overworld.getTime() >= 12010 && overworld.getTime() < 12969 )
//                {
//                    // Beds can be used
//                    worldTime = ChatColor.YELLOW + worldTime + ChatColor.RESET;
//                }
//            } else if( !overworld.hasStorm() )
//            {
//                // Weather clear
//                if ( overworld.getTime() >= 13188 && overworld.getTime() <= 22812 )
//                {
//                    // Monsters are spawning
//                    worldTime = ChatColor.RED + worldTime + ChatColor.RESET;
//                } else if ( overworld.getTime() >= 12542 && overworld.getTime() < 13188 )
//                {
//                    // Beds can be used
//                    worldTime = ChatColor.YELLOW + worldTime + ChatColor.RESET;
//                }
//            }
//        }
//
//        return worldTime;
//    }
}
