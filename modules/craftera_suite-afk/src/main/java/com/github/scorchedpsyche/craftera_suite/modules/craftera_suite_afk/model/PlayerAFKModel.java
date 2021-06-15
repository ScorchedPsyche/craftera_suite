package com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.model;

import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerAFKModel {
    private Player player;
    private boolean isAFK;
    public Integer id_from_DB;
    public long afk_time_start;
    public long afk_time_end;
    public long afk_time_total;
    private double oldX;
    private double oldY;
    private double oldZ;

    public PlayerAFKModel(Player player) {
        this.player = player;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public PlayerAFKModel timerStartOrRestart()
    {
        if( id_from_DB != null )
        {
            id_from_DB = null;
            afk_time_end = 0;
            afk_time_total = 0;
            isAFK = false;
        }

        afk_time_start = DateUtil.Time.getUnixNow();
        oldX = player.getLocation().getX();
        oldY = player.getLocation().getY();
        oldZ = player.getLocation().getZ();

        return this;
    }

    public boolean hasMoved()
    {
        return oldX != player.getLocation().getX()
            || oldY != player.getLocation().getY()
            || oldZ != player.getLocation().getZ();
    }

    public boolean hasBeenAFKFor(long duration)
    {
        return (DateUtil.Time.getUnixNow() - afk_time_start) > duration - 1 ;
    }

    public boolean isAFK()
    {
        return isAFK;
    }

    public PlayerAFKModel markAsAFK(long timeItTakesToGoAFK)
    {
        afk_time_start -= timeItTakesToGoAFK;
        updateAFKTimer();
        player.setPlayerListName(ChatColor.GRAY + player.getDisplayName());
        isAFK = true;

        return this;
    }

    public void markAsNotAFK()
    {
        player.setPlayerListName(ChatColor.WHITE + player.getDisplayName());
        isAFK = false;
    }

    public PlayerAFKModel updateAFKTimer()
    {
        afk_time_end = DateUtil.Time.getUnixNow();
        afk_time_total = afk_time_end - afk_time_start;

        return this;
    }
}
