package com.github.scorchedpsyche.craftera_suite.modules.model;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class GlobalHudInfoModel {
//    public GlobalHudInfoModel()
//    {
//        // Popular world hashmaps
//        for( World world : Bukkit.getWorlds() )
//        {
//            worldTime.put(world.getUID(), "");
//            worldTimeColorized.put(world.getUID(), "");
//        }
//    }

    private String serverTime;
    private HashMap<UUID, String> worldTime = new HashMap<>();
    private HashMap<UUID, String> worldTimeColorized = new HashMap<>();
    public String getServerTime()
    {
        return serverTime;
    }
    public String getWorldTime(UUID worldUUID)
    {
        return worldTime.get(worldUUID);
    }
    public String getWorldTimeColorized(UUID worldUUID)
    {
        return worldTimeColorized.get(worldUUID);
    }

    public void updateGlobalHudInfo()
    {
        // Server time in 24h format
        serverTime = this.formatServerTime();
        for( World world : Bukkit.getWorlds() )
        {
            this.formatWorldTime(world);
        }
    }

    private void formatWorldTime(World world)
    {
        int hours = (int) Math.floor(world.getTime()/1000.0) + 6;
        int minutes = (int) Math.floor((world.getTime() % 1000.0) / 1000 * 60);

        if( hours >= 24 )
        {
            hours = hours - 24;
        }

        String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
        StringBuilder worldTimeStrBuilder = new StringBuilder( time );
        worldTime.put(world.getUID(), worldTimeStrBuilder.toString());

        if ( world.getTime() >= 2000 && world.getTime() <= 9000 )
        {
            // Villager work hours
            worldTimeStrBuilder.insert( 0, ChatColor.GREEN );
        } else if( world.hasStorm() )
        {
            // Weather not clear
            if ( world.getTime() >= 12969 && world.getTime() <= 23031 )
            {
                // Monsters are spawning
                worldTimeStrBuilder.insert( 0, ChatColor.RED );
            } else if ( world.getTime() >= 12010 && world.getTime() < 12969 )
            {
                // Beds can be used
                worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
            }
        } else
        {
            // Weather clear
            if ( world.getTime() >= 13188 && world.getTime() <= 22812 )
            {
                // Monsters are spawning
                worldTimeStrBuilder.insert( 0, ChatColor.RED );
            } else if ( world.getTime() >= 12542 && world.getTime() < 13188 )
            {
                // Beds can be used
                worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
            }
        }
        worldTimeStrBuilder.append(ChatColor.RESET );

        worldTimeColorized.put(world.getUID(), worldTimeStrBuilder.toString());
    }

    private String formatServerTime()
    {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(d1);
    }
}
