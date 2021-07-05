package com.github.scorchedpsyche.craftera_suite.modules.util;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.games.GamesCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.MessageModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerUtil
{
    public static int getCoordinateRoundedX(Player player)
    {
        return (int) player.getLocation().getX();
    }

    public static int getCoordinateRoundedY(Player player)
    {
        return (int) player.getLocation().getY();
    }

    public static int getCoordinateRoundedZ(Player player)
    {
        return (int) player.getLocation().getZ();
    }

    public static World.Environment getEnvironment(Player player)
    {
        return player.getWorld().getEnvironment();
    }

    /**
     * Sends a message to the player with the plugin prefix.
     * @param player The player to send the message to
     * @param pluginPrefix The plugin prefix to the message
     * @param message Message to be sent
     */
    public static void sendMessageWithPluginPrefix(@Nullable Player player, String pluginPrefix, String message)
    {
        if( player != null )
        {
            player.sendMessage(ChatColor.GOLD + "[" + pluginPrefix + "] " + ChatColor.RESET + message);
        }
    }

    public static void sendMessagesWithPluginPrefixOnePerLine(@Nullable Player player, String pluginPrefix, List<MessageModel> messages)
    {
        if( player != null && !messages.isEmpty() )
        {
            StringFormattedModel finalMessage =
                    new StringFormattedModel().gold("[").add(pluginPrefix).add("] ").nl()
                            .aquaR("===== Messages =====").nl();

            for(MessageModel message : messages)
            {
                if( message.getDate_end() != 0 )
                {
                    finalMessage.redR(" -> ").add(message.getMessage_cached());
                } else {
                    finalMessage.redR(" -> ").add(message.getMessage());
                }
                finalMessage.nl();
            }
            finalMessage.aquaR("===== Messages =====");
            player.sendMessage(finalMessage.toString());
        }
    }

    public static double getDistanceToLocation(Player player, Location location )
    {
        return Math.sqrt(
                Math.pow(location.getX() - player.getLocation().getX(), 2) +
                Math.pow(location.getY() - player.getLocation().getY(), 2) +
                Math.pow(location.getZ() - player.getLocation().getZ(), 2));
    }

    /**
     * Check's if the player has the permission with LuckPerm
     * @param player The player to check the permission for
     * @param permission The permission to be checked against LuckPerms
     * @return Boolean that indicates if the user has the permission or not
     */
    public static boolean hasPermission(@Nullable Player player, String permission)
    {
        // Check if valid player
        if( player != null )
        {
            User user = CraftEraSuiteCore.luckPerms.getUserManager().getUser(player.getUniqueId());

            // Check if valid LuckPerm user
            if( user != null )
            {
                // Valid user. Must check permissions

                // If string is null or empty then no permission is required and any user has the permission
                if( StringUtil.isNullOrEmpty(permission) || user.getCachedData().getPermissionData().checkPermission(permission).asBoolean() )
                {
                    return true;
                }
            } else {
                // Error fetching a valid user from LuckPerm
                ConsoleUtil.logError(SuitePluginManager.Core.Name.full,
                        "Failed to get user's permission from Luck Perms.");
                PlayerUtil.sendMessageWithPluginPrefix(player.getPlayer(),
                        SuitePluginManager.Core.Name.compact,
                        "Failed to get user's permission from Luck Perms. Let your server admin know!!!");
                return false;
            }

            // If we got here, player has no permission
            PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Name.full,
                    "Unauthorized: " + permission);
        }

        return false;
    }
}
