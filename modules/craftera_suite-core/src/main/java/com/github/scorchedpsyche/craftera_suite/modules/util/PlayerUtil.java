package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
    public static void sendMessageWithPluginPrefix(Player player, String pluginPrefix, String message)
    {
        player.sendMessage(ChatColor.GOLD + "[" + pluginPrefix + "] " + ChatColor.RESET + message);
    }

    public static double getDistanceToLocation(Player player, Location location )
    {
        return Math.sqrt(
                Math.pow(location.getX() - player.getLocation().getX(), 2) +
                Math.pow(location.getY() - player.getLocation().getY(), 2) +
                Math.pow(location.getZ() - player.getLocation().getZ(), 2));
    }
}
