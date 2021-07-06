package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.model.GlobalHudInfoModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ItemStackUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.StringUtilsHud;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerHudManager
{
    public StringFormattedModel getPlayerHudText(
        Player player, HudPlayerPreferencesModel preferences, GlobalHudInfoModel globalHudInfoModel )
    {
        StringFormattedModel hudText = new StringFormattedModel();

        if( preferences.showCoordinates() )
        {
            hudText.add( formatPlayerCoordinates(player, preferences) );
        }

        if( preferences.showOrientation() )
        {
            hudText.add( " " );
            hudText.add( formatPlayerOrientation(player, preferences) );
        }

        if( preferences.showNetherPortalCoordinates() )
        {
            hudText.add( formatNetherPortalCoordinates(player, PlayerUtil.getEnvironment(player), preferences) );
        }

        if( preferences.showToolDurability() )
        {
            hudText.add( formatToolDurability( preferences, "Main", player.getInventory().getItemInMainHand() ));
            hudText.add( formatToolDurability( preferences, "Off", player.getInventory().getItemInOffHand() ));
        }

        if( preferences.showWorldTime() )
        {
            hudText.add( " " );
            if( preferences.colorizeWorldTime() )
            {
                hudText.add( globalHudInfoModel.getWorldTimeColorized( player.getWorld().getUID() ) );
            } else {
                hudText.add( globalHudInfoModel.getWorldTime( player.getWorld().getUID() ) );
            }
        }

        if( preferences.showServerTime() )
        {
            hudText.add( " " );
            hudText.add( globalHudInfoModel.getServerTime() );
        }

//        StringBuilder hudText = new StringBuilder();
//
//        if( preferences.showCoordinates() )
//        {
//            hudText.append( formatPlayerCoordinates(player, preferences) );
//        }
//
//        if( preferences.showOrientation() )
//        {
//            hudText.append( " " );
//            hudText.append( formatPlayerOrientation(player, preferences) );
//        }
//
//        if( preferences.showNetherPortalCoordinates() )
//        {
//            hudText.append( formatNetherPortalCoordinates(player, PlayerUtil.getEnvironment(player), preferences) );
//        }
//
//        if( preferences.showToolDurability() )
//        {
//            hudText.append( formatToolDurability( preferences, "Main", player.getInventory().getItemInMainHand() ));
//            hudText.append( formatToolDurability( preferences, "Off", player.getInventory().getItemInOffHand() ));
//        }
//
//        if( preferences.showWorldTime() )
//        {
//            hudText.append( " " );
//            if( preferences.colorizeWorldTime() )
//            {
//                hudText.append( globalHudInfoModel.getWorldTimeColorized( player.getWorld().getUID() ) );
//            } else {
//                hudText.append( globalHudInfoModel.getWorldTime( player.getWorld().getUID() ) );
//            }
//        }
//
//        if( preferences.showServerTime() )
//        {
//            hudText.append( " " );
//            hudText.append( globalHudInfoModel.getServerTime() );
//        }







//        if( preferences.showServerTPS() )
//        {
//            hudText.append( " " );
//            hudText.append( formatServerTps( preferences ) );
//        }


//        if( Bukkit.getServer().getPluginManager().isPluginEnabled(SuitePluginManager.SpectatorMode.Name.pomXml) &&
//                CraftEraSuiteSpectatorMode.spectatorModeManager.distanceFromSource.containsKey(player.getUniqueId().toString()))
//        {
//            hudText.append( " " );
//            hudText.append( formatSpectatorRange(
//                    CraftEraSuiteSpectatorMode.spectatorModeManager.distanceFromSource.get(player.getUniqueId().toString()),
//                    CraftEraSuiteSpectatorMode.spectatorModeManager.getRangeLimit(),
//                    preferences));
//        }

        return hudText;
    }

    private StringFormattedModel formatPlayerCoordinates(Player player, HudPlayerPreferencesModel preferences)
    {
        StringFormattedModel playerCoordinatesStr = new StringFormattedModel();

        if( preferences.isDisplayModeExtended() )
        {
            // Extended
            if( preferences.colorizeCoordinates() )
            {
                // Colorized
                playerCoordinatesStr.add(StringUtilsHud.playerCoordinatesExtendedColorized);
            } else {
                // Not colorized
                playerCoordinatesStr.add(StringUtilsHud.playerCoordinatesExtended);
            }

            playerCoordinatesStr.add( PlayerUtil.getCoordinateRoundedX(player) ); // x
            playerCoordinatesStr.add( " " );
            playerCoordinatesStr.add( PlayerUtil.getCoordinateRoundedY(player) ); // y
            playerCoordinatesStr.add( " " );
            playerCoordinatesStr.add( PlayerUtil.getCoordinateRoundedZ(player) ); // z
        } else {
            // Compact
            if( preferences.colorizeCoordinates() )
            {
                // Colorized
                playerCoordinatesStr.add(StringUtilsHud.playerCoordinatesCompactColorized);
            } else {
                // Not colorized
                playerCoordinatesStr.add(StringUtilsHud.playerCoordinatesCompact);
            }

            playerCoordinatesStr.insert( 12, PlayerUtil.getCoordinateRoundedZ(player) ); // z
            playerCoordinatesStr.insert( 6, PlayerUtil.getCoordinateRoundedY(player) ); // y
            playerCoordinatesStr.insert( 0, PlayerUtil.getCoordinateRoundedX(player) ); // x
        }

        return playerCoordinatesStr;
    }

    private StringFormattedModel formatPlayerOrientation(Player player, HudPlayerPreferencesModel preferences)
    {
        StringFormattedModel orientation = new StringFormattedModel();

        double rotation = (player.getLocation().getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        if( rotation < 22.5 )
        {
            orientation.add("N");
        } else if ( rotation < 67.5 )
        {
            orientation.add("NE");
        } else if ( rotation < 112.55 )
        {
            orientation.add("E");
        } else if ( rotation < 157.5 )
        {
            orientation.add("SE");
        } else if ( rotation < 202.5 )
        {
            orientation.add("S");
        } else if ( rotation < 247.5 )
        {
            orientation.add("SW");
        } else if ( rotation < 292.5 )
        {
            orientation.add("W");
        } else if ( rotation < 337.5 )
        {
            orientation.add("NW");
        } else
        {
            orientation.add("N");
        }

        if( preferences.colorizePlayerOrientation() )
        {
            orientation.reset();
            orientation.insert( 0, ChatColor.AQUA );
        }

        return orientation;
    }

    private StringFormattedModel formatNetherPortalCoordinates(
            Player player, World.Environment environment, HudPlayerPreferencesModel preferences)
    {
        StringFormattedModel strBuilder = new StringFormattedModel();

        if( !environment.equals(World.Environment.THE_END) )
        {
            int portalX = PlayerUtil.getCoordinateRoundedX(player);
            int portalZ = PlayerUtil.getCoordinateRoundedZ(player);

            if( environment.equals(World.Environment.NETHER) )
            {
                portalX *= 8;
                portalZ *= 8;
            } else {
                portalX /= 8;
                portalZ /= 8;
            }

            if( preferences.isDisplayModeExtended() )
            {
                // Extended
                if( preferences.colorizeNetherPortalCoordinates() )
                {
                    // Colorized
                    strBuilder.add(StringUtilsHud.netherPortalCoordinatesExtendedColorized);
                } else {
                    // Not colorized
                    strBuilder.add(StringUtilsHud.netherPortalCoordinatesExtended);
                }

                strBuilder.add( portalX ); // x
                strBuilder.add( " " );
                strBuilder.add( portalZ ); // z
            } else {
                // Compact
                if( preferences.colorizeNetherPortalCoordinates() )
                {
                    // Colorized
                    strBuilder.add(StringUtilsHud.netherPortalCoordinatesCompactColorized);

                    strBuilder.insert( 6, portalZ ); // z
                } else {
                    // Not colorized
                    strBuilder.add(StringUtilsHud.netherPortalCoordinatesCompact);

                    strBuilder.insert( 2, portalZ ); // z
                }

                strBuilder.insert( 0, portalX ); // x
            }
            strBuilder.insert( 0, " " );
        }

        return strBuilder;
    }

//    private String formatServerTime()
//    {
//        Date d1 = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
//        return df.format(d1);
//    }

//    private StringBuilder formatServerTps(HudPlayerPreferencesModel preferences)
//    {
//        StringBuilder tpsStrBuilder = new StringBuilder();
//
//        short tps = (short) MinecraftServer.getServer().recentTps[0] ;
//        if( tps > 20 )
//        {
//            tps = 20;
//        }
//
//        tpsStrBuilder.append( tps );
//
//        if(preferences.colorizeServerTps() )
//        {
//            if( tps >= 20 )
//            {
//                tps = 20;
//                tpsStrBuilder.insert( 0,ChatColor.GREEN );
//            } else if ( tps < 20 && tps >= 15 ){
//                tpsStrBuilder.insert( 0,ChatColor.YELLOW );
//            } else {
//                tpsStrBuilder.insert( 0,ChatColor.RED );
//            }
//
//            tpsStrBuilder.append( ChatColor.RESET );
//        }
//
//        if( preferences.isDisplayModeExtended() )
//        {
//            // Extended
//            if( preferences.colorizeServerTps() )
//            {
//                tpsStrBuilder.insert( 0, ChatColor.RESET );
//                tpsStrBuilder.insert( 0, "TPS: ");
//                tpsStrBuilder.insert( 0, ChatColor.GOLD );
//            } else {
//                tpsStrBuilder.insert( 0, "TPS: ");
//            }
//        }
//
//
//        return tpsStrBuilder;
//    }

    private StringBuilder formatToolDurability(HudPlayerPreferencesModel preferences, String slotText, ItemStack item)
    {
        StringBuilder durabilityStrBuilder = new StringBuilder();
        Integer itemRemainingDurability = ItemStackUtil.getItemRemainingDurability(item);

        if( itemRemainingDurability != null )
        {
            if ( preferences.colorizeToolDurability() )
            {
                durabilityStrBuilder.append( ChatColor.RESET );
                float percentageDurabilityRemaining =
                        (float) itemRemainingDurability / (float) item.getType().getMaxDurability();

                if( percentageDurabilityRemaining == 1 )
                {
                    durabilityStrBuilder.append( ChatColor.GREEN );
                } else if ( percentageDurabilityRemaining > .5 )
                {
                    durabilityStrBuilder.append( ChatColor.DARK_AQUA );
                } else if ( percentageDurabilityRemaining > .2 )
                {
                    durabilityStrBuilder.append( ChatColor.YELLOW );
                } else if ( percentageDurabilityRemaining > .1 )
                {
                    durabilityStrBuilder.append( ChatColor.RED );
                } else {
                    // 5%
                    durabilityStrBuilder.append( ChatColor.DARK_RED );
                }

                durabilityStrBuilder.append( ChatColor.BOLD );
                durabilityStrBuilder.append( itemRemainingDurability );
                durabilityStrBuilder.append( ChatColor.RESET );
            } else {
                durabilityStrBuilder.append( itemRemainingDurability );
            }

            durabilityStrBuilder.append("/");
            durabilityStrBuilder.append( item.getType().getMaxDurability() );

            if( preferences.isDisplayModeExtended() )
            {
                // Extended
                durabilityStrBuilder.insert( 0, ": " );
                durabilityStrBuilder.insert( 0, slotText );

                if ( preferences.colorizeToolDurability() )
                {
                    durabilityStrBuilder.insert( 0, ChatColor.GOLD );
                }
            }

            durabilityStrBuilder.insert( 0, " " );
        }

        return durabilityStrBuilder;
    }

//    private StringBuilder formatWorldTime(Player player, HudPlayerPreferencesModel preferences)
//    {
//        World world = player.getWorld();
//        long seconds = world.getTime() / 20L;
////        int hours = (int) Math.floor(world.getTime() / 1000.0);
////        int minute = (int) Math.floor(seconds / 60.0);
//        int hours = (int) Math.floor(world.getTime()/1000.0) + 6;
//        int minutes = (int) Math.floor((world.getTime() % 1000.0) / 1000 * 60);
//
//        if( hours >= 24 )
//        {
//            hours = hours - 24;
//        }
//
//        String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
//
////        LocalTime timeOfDay = LocalTime.ofSecondOfDay( world.getTime() / 20L );
////        timeOfDay.format(new DateTimeFormatter("HH:mm")).toString();
////        String time = timeOfDay.toString();
//
//
////        StringBuilder worldTimeStrBuilder = new StringBuilder( Long.toString(world.getTime()) );
//        StringBuilder worldTimeStrBuilder = new StringBuilder( time );
//
//        if( preferences.colorizeWorldTime() )
//        {
//            if ( world.getTime() >= 2000 && world.getTime() <= 9000 )
//            {
//                // Villager work hours
//                worldTimeStrBuilder.insert( 0, ChatColor.GREEN );
//            } else if( world.hasStorm() )
//            {
//                // Weather not clear
//                if ( world.getTime() >= 12969 && world.getTime() <= 23031 )
//                {
//                    // Monsters are spawning
//                    worldTimeStrBuilder.insert( 0, ChatColor.RED );
//                } else if ( world.getTime() >= 12010 && world.getTime() < 12969 )
//                {
//                    // Beds can be used
//                    worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
//                }
//            } else
//            {
//                // Weather clear
//                if ( world.getTime() >= 13188 && world.getTime() <= 22812 )
//                {
//                    // Monsters are spawning
//                    worldTimeStrBuilder.insert( 0, ChatColor.RED );
//                } else if ( world.getTime() >= 12542 && world.getTime() < 13188 )
//                {
//                    // Beds can be used
//                    worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
//                }
//            }
//            worldTimeStrBuilder.append(ChatColor.RESET );
//        }
//
//        return worldTimeStrBuilder;
//    }

//    private StringBuilder formatSpectatorRange(Double distance, int range, HudPlayerPreferencesModel preferences)
//    {
//        StringBuilder specRangeBuilder = new StringBuilder();
//
//        if( distance != null )
//        {
//            if( distance < range * 0.2 )
//            {
//                specRangeBuilder.append( ChatColor.GREEN );
//            } else if ( distance >= range * (0.2) && distance < range * (0.4) ) {
//                specRangeBuilder.append( ChatColor.DARK_GREEN );
//            } else if ( distance >= range * (0.4) && distance < range * (0.6) ) {
//                specRangeBuilder.append( ChatColor.YELLOW );
//            } else if ( distance >= range * (0.6) && distance < range * (0.8) ) {
//                specRangeBuilder.append( ChatColor.GOLD );
//            } else { // 0.8
//                specRangeBuilder.append( ChatColor.RED );
//            }
//
//            specRangeBuilder.append( String.format("%.1f",distance) );
//            specRangeBuilder.append( ChatColor.RESET );
//
//            specRangeBuilder.append( "/" );
//            specRangeBuilder.append( range );
//
//            if( preferences.isDisplayModeExtended() )
//            {
//                // EXTENDED
//
//                specRangeBuilder.insert( 0, "Spec: " );
//                specRangeBuilder.insert( 0, ChatColor.GOLD );
//            }
//        }
//        return specRangeBuilder;
//    }
}
