package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HudManager {
    public void toggleHudForPlayer(Player player)
    {
        Bukkit.getConsoleSender().sendMessage(player.getName());
    }
}
