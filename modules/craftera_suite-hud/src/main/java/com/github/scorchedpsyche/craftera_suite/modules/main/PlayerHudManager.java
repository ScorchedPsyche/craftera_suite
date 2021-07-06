package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.model.GlobalHudInfoModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ItemStackUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.StringUtilsHud;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerHudManager
{
    /**
     * Formats the action bar's String based on the player's HUD preferences.
     * @param player The player to get the HUD text for
     * @param preferences The player's preferences
     * @param globalHudInfoModel The global info shared by all players such as World Time and Server Time
     * @return The formatted string for the player's Action Bar
     */
    public StringFormattedModel getPlayerHudText(
        Player player, HudPlayerPreferencesModel preferences, GlobalHudInfoModel globalHudInfoModel )
    {
        StringFormattedModel hudText = new StringFormattedModel();

        // Coordinates
        if( preferences.showCoordinates() )
        {
            hudText.add( formatPlayerCoordinates(player, preferences) );
        }

        // Orientation (N, S, E, W, etc)
        if( preferences.showOrientation() )
        {
            hudText.add( " " );
            hudText.add( formatPlayerOrientation(player, preferences) );
        }

        // Nether Portal Coordinates in the opposing dimension
        if( preferences.showNetherPortalCoordinates() )
        {
            hudText.add( formatNetherPortalCoordinates(player, PlayerUtil.getEnvironment(player), preferences) );
        }

        // Main/Off Hand tool durability
        if( preferences.showToolDurability() )
        {
            hudText.add( formatToolDurability( preferences, "Main", player.getInventory().getItemInMainHand() ));
            hudText.add( formatToolDurability( preferences, "Off", player.getInventory().getItemInOffHand() ));
        }

        // World Time for the world the player is currently in
        if( preferences.showWorldTime() )
        {
            hudText.add( " " );
            if( preferences.formatWorldTime() )
            {
                // 24 hour format
                if( preferences.colorizeWorldTime() )
                {
                    hudText.add( globalHudInfoModel.getWorldTimeIn24hColorized( player.getWorld().getUID() ) );
                } else {
                    hudText.add( globalHudInfoModel.getWorldTimeIn24h( player.getWorld().getUID() ) );
                }
            } else {
                // Tick format
                if( preferences.colorizeWorldTime() )
                {
                    hudText.add( globalHudInfoModel.getWorldTimeInTicksColorized( player.getWorld().getUID() ) );
                } else {
                    hudText.add( globalHudInfoModel.getWorldTimeInTicks( player.getWorld().getUID() ) );
                }
            }
        }

        // Server real-world time
        if( preferences.showServerTime() )
        {
            hudText.add( " " );
            hudText.add( globalHudInfoModel.getServerTime() );
        }

        return hudText;
    }

    /**
     * Formatting of the player's World coordinates based on their preference.
     * @param player The player to format the World coordinates for
     * @param preferences The player's HUD preferences
     * @return A string with the formatted World time
     */
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

    /**
     * Formatting of the player's Orientation (N, S, E, W, etc) based on their preference.
     * @param player The player to format the Orientation for
     * @param preferences The player's HUD preferences
     * @return A string with the formatted player orientation
     */
    private StringFormattedModel formatPlayerOrientation(Player player, HudPlayerPreferencesModel preferences)
    {
        StringFormattedModel orientation = new StringFormattedModel();

        // Gets player's YAW and normalize it to 360 degrees
        double rotation = (player.getLocation().getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        // Find the orientation based on the player's normalized YAW
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

        // Colorizes it if needed
        if( preferences.colorizePlayerOrientation() )
        {
            orientation.reset();
            orientation.insert( 0, ChatColor.AQUA );
        }

        return orientation;
    }

    /**
     * Formatting of the player's Nether Portal Coordinates on the opposing dimension based on their preference.
     * @param player The player to format the Nether Portal Coordinates for
     * @param preferences The player's HUD preferences
     * @return A string with the formatted Nether Portal Coordinates
     */
    private StringFormattedModel formatNetherPortalCoordinates(
            Player player, World.Environment environment, HudPlayerPreferencesModel preferences)
    {
        StringFormattedModel strBuilder = new StringFormattedModel();

        // Check if World is not The End
        if( !environment.equals(World.Environment.THE_END) )
        {
            // Not The End. Get player current coordinates
            int portalX = PlayerUtil.getCoordinateRoundedX(player);
            int portalZ = PlayerUtil.getCoordinateRoundedZ(player);

            // Check if it's Nether or Overworld and adjust the value to the opposing dimension
            if( environment.equals(World.Environment.NETHER) )
            {
                portalX *= 8;
                portalZ *= 8;
            } else {
                portalX /= 8;
                portalZ /= 8;
            }

            // Check if it's Extended display mode
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

    /**
     * Formatting of the player's Main/Off hand tool durability based on their preference.
     * @param preferences The player's HUD preferences
     * @param slotText The text that should be displayed before the durability. E.g.: Main 100/200
     * @param item The item we should parse the durability for
     * @return A string with the formatted tool durability for the desired item/slot
     */
    private StringBuilder formatToolDurability(HudPlayerPreferencesModel preferences, String slotText, ItemStack item)
    {
        StringBuilder durabilityStrBuilder = new StringBuilder();

        // Gets the remaining durability for the item
        Integer itemRemainingDurability = ItemStackUtil.getItemRemainingDurability(item);

        // Check if the player is holding an item with a valid durability
        if( itemRemainingDurability != null )
        {
            // Check if we should colorize the tool durability text
            if ( preferences.colorizeToolDurability() )
            {
                durabilityStrBuilder.append( ChatColor.RESET );
                float percentageDurabilityRemaining =
                        (float) itemRemainingDurability / (float) item.getType().getMaxDurability();

                // Append the color based on the percentage remaining for the tool durability
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

            // Append the max durability
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
}
