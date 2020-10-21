package com.github.scorchedpsyche.craftera_suite.modules.utils;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerUtils
{
    public int getCoordinateRoundedX(Player player)
    {
        return (int) player.getLocation().getX();
    }

    public int getCoordinateRoundedY(Player player)
    {
        return (int) player.getLocation().getY();
    }

    public int getCoordinateRoundedZ(Player player)
    {
        return (int) player.getLocation().getZ();
    }

    public World.Environment getEnvironment(Player player)
    {
        return player.getWorld().getEnvironment();
    }

    /**
     * Sends a message to the player with the plugin prefix.
     * @param player The player to send the message to
     * @param pluginPrefix The plugin prefix to the message
     * @param message Message to be sent
     */
    public void sendMessageWithPluginPrefix(Player player, String pluginPrefix, String message)
    {
        player.sendMessage(ChatColor.GOLD + "[" + pluginPrefix + "] " + ChatColor.RESET + message);
    }
}