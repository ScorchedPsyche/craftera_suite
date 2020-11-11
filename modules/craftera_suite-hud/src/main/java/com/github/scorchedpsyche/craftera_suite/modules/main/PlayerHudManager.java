package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ItemStackUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.StringUtilsHud;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerHudManager
{
//    private final PlayerUtils playerUtils = new PlayerUtils();
//    private final ItemStackUtils itemStackUtils = new ItemStackUtils();

    public StringBuilder getPlayerHudText(Player player, HudPlayerPreferencesModel preferences)
    {
        StringBuilder hudText = new StringBuilder();

        if( preferences.showCoordinates() )
        {
            hudText.append( formatPlayerCoordinates(player, preferences) );
        }

        if( preferences.showOrientation() )
        {
            hudText.append( " " );
            hudText.append( formatPlayerOrientation(player, preferences) );
        }

        if( preferences.showNetherPortalCoordinates() )
        {
            hudText.append( formatNetherPortalCoordinates(player, PlayerUtils.getEnvironment(player), preferences) );
        }

        if( preferences.showToolDurability() )
        {
            hudText.append( formatToolDurability( preferences, "Main", player.getInventory().getItemInMainHand() ));
            hudText.append( formatToolDurability( preferences, "Off", player.getInventory().getItemInOffHand() ));
        }

        if( preferences.showWorldTime() )
        {
            hudText.append( " " );
            hudText.append( formatWorldTime( preferences ) );
        }

        if( preferences.showServerTPS() )
        {
            hudText.append( " " );
            hudText.append( formatServerTps( preferences ) );
        }

        if( preferences.showServerTime() )
        {
            hudText.append( " " );
            hudText.append( formatServerTime() );
        }

        return hudText;
    }

    private StringBuilder formatPlayerCoordinates(Player player, HudPlayerPreferencesModel preferences)
    {
        StringBuilder strBuilder = new StringBuilder();

        if( preferences.isDisplayModeExtended() )
        {
            // Extended
            if( preferences.colorizeCoordinates() )
            {
                // Colorized
                strBuilder.append(StringUtilsHud.playerCoordinatesExtendedColorized);
            } else {
                // Not colorized
                strBuilder.append(StringUtilsHud.playerCoordinatesExtended);
            }

            strBuilder.append( PlayerUtils.getCoordinateRoundedX(player) ); // x
            strBuilder.append( " " );
            strBuilder.append( PlayerUtils.getCoordinateRoundedY(player) ); // y
            strBuilder.append( " " );
            strBuilder.append( PlayerUtils.getCoordinateRoundedZ(player) ); // z
        } else {
            // Compact
            if( preferences.colorizeCoordinates() )
            {
                // Colorized
                strBuilder.append(StringUtilsHud.playerCoordinatesCompactColorized);
            } else {
                // Not colorized
                strBuilder.append(StringUtilsHud.playerCoordinatesCompact);
            }

            strBuilder.insert( 12, PlayerUtils.getCoordinateRoundedZ(player) ); // z
            strBuilder.insert( 6, PlayerUtils.getCoordinateRoundedY(player) ); // y
            strBuilder.insert( 0, PlayerUtils.getCoordinateRoundedX(player) ); // x
        }

        return strBuilder;
    }

    private StringBuilder formatPlayerOrientation(Player player, HudPlayerPreferencesModel preferences)
    {
        StringBuilder orientation = new StringBuilder();

        double rotation = (player.getLocation().getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        if( rotation > 22.5 && rotation <= 67.5 )
        {
            orientation.append("NE");
        } else if ( rotation > 67.5 && rotation <= 112.5 )
        {
            orientation.append("E");
        } else if ( rotation > 112.5 && rotation <= 157.5 )
        {
            orientation.append("SE");
        } else if ( rotation > 157.5 && rotation <= 202.5 )
        {
            orientation.append("S");
        } else if ( rotation > 202.5 && rotation <= 247.5 )
        {
            orientation.append("SW");
        } else if ( rotation > 247.5 && rotation <= 292.5 )
        {
            orientation.append("W");
        } else if ( rotation > 292.5 && rotation <= 337.5 )
        {
            orientation.append("NW");
        } else {
            orientation.append("NE");
        }

        if( preferences.colorizePlayerOrientation() )
        {
            orientation.append( ChatColor.RESET );
            orientation.insert( 0, ChatColor.AQUA );
        }

        return orientation;
    }

    private StringBuilder formatNetherPortalCoordinates(
            Player player, World.Environment environment, HudPlayerPreferencesModel preferences)
    {
        StringBuilder strBuilder = new StringBuilder();

        if( !environment.equals(World.Environment.THE_END) )
        {
            int portalX = PlayerUtils.getCoordinateRoundedX(player);
            int portalZ = PlayerUtils.getCoordinateRoundedZ(player);

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
                    strBuilder.append(StringUtilsHud.netherPortalCoordinatesExtendedColorized);
                } else {
                    // Not colorized
                    strBuilder.append(StringUtilsHud.netherPortalCoordinatesExtended);
                }

                strBuilder.append( portalX ); // x
                strBuilder.append( " " );
                strBuilder.append( portalZ ); // z
            } else {
                // Compact
                if( preferences.colorizeNetherPortalCoordinates() )
                {
                    // Colorized
                    strBuilder.append(StringUtilsHud.netherPortalCoordinatesCompactColorized);

                    strBuilder.insert( 6, portalZ ); // z
                } else {
                    // Not colorized
                    strBuilder.append(StringUtilsHud.netherPortalCoordinatesCompact);

                    strBuilder.insert( 2, portalZ ); // z
                }

                strBuilder.insert( 0, portalX ); // x
            }
            strBuilder.insert( 0, " " );
        }

        return strBuilder;
    }

    private String formatServerTime()
    {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(d1);
    }

    private StringBuilder formatServerTps(HudPlayerPreferencesModel preferences)
    {
        StringBuilder tpsStrBuilder = new StringBuilder();

        short tps = (short) MinecraftServer.getServer().recentTps[0] ;
        if( tps > 20 )
        {
            tps = 20;
        }

        tpsStrBuilder.append( tps );

        if(preferences.colorizeServerTps() )
        {
            if( tps >= 20 )
            {
                tps = 20;
                tpsStrBuilder.insert( 0,ChatColor.GREEN );
            } else if ( tps < 20 && tps >= 15 ){
                tpsStrBuilder.insert( 0,ChatColor.YELLOW );
            } else {
                tpsStrBuilder.insert( 0,ChatColor.RED );
            }

            tpsStrBuilder.append( ChatColor.RESET );
        }

        if( preferences.isDisplayModeExtended() )
        {
            // Extended
            if( preferences.colorizeServerTps() )
            {
                tpsStrBuilder.insert( 0, ChatColor.RESET );
                tpsStrBuilder.insert( 0, "TPS: ");
                tpsStrBuilder.insert( 0, ChatColor.GOLD );
            } else {
                tpsStrBuilder.insert( 0, "TPS: ");
            }
        }


        return tpsStrBuilder;
    }

    private StringBuilder formatToolDurability(HudPlayerPreferencesModel preferences, String slotText, ItemStack item)
    {
        StringBuilder durabilityStrBuilder = new StringBuilder();
        Integer itemRemainingDurability = ItemStackUtils.getItemRemainingDurability(item);

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

    private StringBuilder formatWorldTime(HudPlayerPreferencesModel preferences)
    {
        World overworld = Bukkit.getWorld("world");

        StringBuilder worldTimeStrBuilder = new StringBuilder( Long.toString(overworld.getTime()) );

        if( preferences.colorizeWorldTime() )
        {
            if ( overworld.getTime() >= 2000 && overworld.getTime() <= 9000 )
            {
                // Villager work hours
                worldTimeStrBuilder.insert( 0, ChatColor.GREEN );
            } else if( overworld.hasStorm() )
            {
                // Weather not clear
                if ( overworld.getTime() >= 12969 && overworld.getTime() <= 23031 )
                {
                    // Monsters are spawning
                    worldTimeStrBuilder.insert( 0, ChatColor.RED );
                } else if ( overworld.getTime() >= 12010 && overworld.getTime() < 12969 )
                {
                    // Beds can be used
                    worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
                }
            } else
            {
                // Weather clear
                if ( overworld.getTime() >= 13188 && overworld.getTime() <= 22812 )
                {
                    // Monsters are spawning
                    worldTimeStrBuilder.insert( 0, ChatColor.RED );
                } else if ( overworld.getTime() >= 12542 && overworld.getTime() < 13188 )
                {
                    // Beds can be used
                    worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
                }
            }
            worldTimeStrBuilder.append(ChatColor.RESET );
        }

        return worldTimeStrBuilder;
    }
}
