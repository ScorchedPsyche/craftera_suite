package com.github.scorchedpsyche.craftera_suite.modules.util;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ServerUtil {
    public static void broadcast(TextComponent message) {
        Bukkit.getServer().spigot().broadcast(message);
    }
    public static void broadcastWithPluginPrefix(@NotNull String prefix, TextComponent message) {
        message.setText(ChatColor.GOLD + "[" + prefix + "] " + ChatColor.RESET + message.getText());
        Bukkit.getServer().spigot().broadcast(message);
    }


    @Nullable
    public static Player getPlayerByUUID(UUID playerUUID)
    {
        return Bukkit.getPlayer(playerUUID);
    }

    public static void playSoundForPlayer(Player player, Sound sound)
    {
        player.playSound(
            player.getLocation(),
            sound,
            1,
            0
        );
    }

    public static void playSoundForAllPlayers(Sound sound)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            playSoundForPlayer(player, sound);
        }
    }
}
