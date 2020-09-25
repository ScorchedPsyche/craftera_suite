package com.github.scorchedpsyche.craftera_suite.modules.main;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HudManager {
    private final List<Player> onlinePlayersWithHudEnabled;

    public HudManager()
    {
        onlinePlayersWithHudEnabled = new ArrayList<>();
    }

    public void showHudForPlayers()
    {
        for ( Player player : onlinePlayersWithHudEnabled)
        {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                (int) player.getLocation().getX() + "x " +
                (int) player.getLocation().getY() + "y " +
                (int) player.getLocation().getZ() + "z" ));
        }
    }

    public void toggleHudForPlayer(Player player)
    {
        if( onlinePlayersWithHudEnabled.contains(player) )
        {
            onlinePlayersWithHudEnabled.remove(player);
        } else {
            onlinePlayersWithHudEnabled.add(player);
        }
    }

    public void setPlayerAsOffline(Player player)
    {
        onlinePlayersWithHudEnabled.remove(player);
    }
}
