package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteHud;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HudManager {
    public HudManager(HudDatabaseAPI hudDatabaseAPI)
    {
        this.hudDatabaseAPI = hudDatabaseAPI;
        onlinePlayersWithHudEnabled = new ArrayList<>();
    }

    private final List<Player> onlinePlayersWithHudEnabled;
    private HudDatabaseAPI hudDatabaseAPI;

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
            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                    hudDatabaseAPI.disableHudForPlayer( player.getUniqueId().toString() );
                });
            });
            onlinePlayersWithHudEnabled.remove(player);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                Bukkit.getScheduler().runTask(CraftEraSuiteHud.getPlugin(CraftEraSuiteHud.class), () -> {
                    hudDatabaseAPI.enableHudForPlayer( player.getUniqueId().toString() );
                });
            });
            onlinePlayersWithHudEnabled.add(player);
        }
    }

    public void setPlayerAsOffline(Player player)
    {
        onlinePlayersWithHudEnabled.remove(player);
    }
}
