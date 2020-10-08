package com.github.scorchedpsyche.craftera_suite.modules.utils;

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
}
