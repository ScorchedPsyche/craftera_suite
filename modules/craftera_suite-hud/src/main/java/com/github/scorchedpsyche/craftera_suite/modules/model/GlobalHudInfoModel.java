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
    private HashMap<UUID, String> worldTimeIn24h = new HashMap<>();
    private HashMap<UUID, String> worldTimeIn24hColorized = new HashMap<>();
    private HashMap<UUID, String> worldTimeInTicks = new HashMap<>();
    private HashMap<UUID, String> worldTimeInTicksColorized = new HashMap<>();
    public String getServerTime()
    {
        return serverTime;
    }
    public String getWorldTimeIn24h(UUID worldUUID)
    {
        return worldTimeIn24h.get(worldUUID);
    }
    public String getWorldTimeIn24hColorized(UUID worldUUID)
    {
        return worldTimeIn24hColorized.get(worldUUID);
    }
    public String getWorldTimeInTicks(UUID worldUUID) {
        return worldTimeInTicks.get(worldUUID);
    }
    public String getWorldTimeInTicksColorized(UUID worldUUID) {
        return worldTimeInTicksColorized.get(worldUUID);
    }

    public void updateGlobalHudInfo()
    {
        // Server time in 24h format
        serverTime = this.formatServerTime();
        for( World world : Bukkit.getWorlds() )
        {
//            this.worldTimeInTicks.put(world.getUID(), String.valueOf(world.getTime()));
//            this.formatWorldTime(world);
            formatTimeForWorld(world);
        }
    }

    private void formatTimeForWorld(World world)
    {
        this.worldTimeIn24h.put(world.getUID(), getWorldTimeIn24h(world));
        this.worldTimeIn24hColorized.put(world.getUID(), getWorldTimeIn24h(world));
        this.worldTimeInTicks.put(world.getUID(), String.valueOf(world.getTime()));
        this.worldTimeInTicksColorized.put(world.getUID(), String.valueOf(world.getTime()));

        ChatColor color = getWorldTimeColor(world);
        colorizeTimeForWorld(world, color);
    }

    private void colorizeTimeForWorld(World world, ChatColor color)
    {
        this.worldTimeIn24hColorized.put(world.getUID(), new StringFormattedModel()
                .add(color).add(this.worldTimeIn24hColorized.get(world.getUID())).reset().toString());
        this.worldTimeInTicksColorized.put(world.getUID(), new StringFormattedModel()
                .add(color).add(this.worldTimeInTicksColorized.get(world.getUID())).reset().toString());
    }

    private ChatColor getWorldTimeColor(World world)
    {
        ChatColor color = ChatColor.WHITE;

        if ( world.getTime() >= 2000 && world.getTime() <= 9000 )
        {
            // Villager work hours
            color = ChatColor.GREEN;
        } else if( world.hasStorm() )
        {
            // Weather not clear
            if ( world.getTime() >= 12969 && world.getTime() <= 23031 )
            {
                // Monsters are spawning
                color = ChatColor.RED;
            } else if ( world.getTime() >= 12010 && world.getTime() < 12969 )
            {
                // Beds can be used
                color = ChatColor.YELLOW;
            }
        } else
        {
            // Weather clear
            if ( world.getTime() >= 13188 && world.getTime() <= 22812 )
            {
                // Monsters are spawning
                color = ChatColor.RED;
            } else if ( world.getTime() >= 12542 && world.getTime() < 13188 )
            {
                // Beds can be used
                color = ChatColor.YELLOW;
            }
        }

        return color;
    }

    private String getWorldTimeIn24h(World world)
    {
        int hours = (int) Math.floor(world.getTime()/1000.0) + 6;
        int minutes = (int) Math.floor((world.getTime() % 1000.0) / 1000 * 60);

        if( hours >= 24 )
        {
            hours = hours - 24;
        }

//        String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
        return new StringBuilder( String.format("%02d", hours) ).append(":").append(String.format("%02d", minutes)).toString();
    }

//    private void formatWorldTime(World world)
//    {
//        int hours = (int) Math.floor(world.getTime()/1000.0) + 6;
//        int minutes = (int) Math.floor((world.getTime() % 1000.0) / 1000 * 60);
//
//        if( hours >= 24 )
//        {
//            hours = hours - 24;
//        }
//
//        String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
//        StringBuilder worldTimeStrBuilder = new StringBuilder( time );
//        worldTimeIn24h.put(world.getUID(), worldTimeStrBuilder.toString());
//
//        if ( world.getTime() >= 2000 && world.getTime() <= 9000 )
//        {
//            // Villager work hours
//            worldTimeStrBuilder.insert( 0, ChatColor.GREEN );
//        } else if( world.hasStorm() )
//        {
//            // Weather not clear
//            if ( world.getTime() >= 12969 && world.getTime() <= 23031 )
//            {
//                // Monsters are spawning
//                worldTimeStrBuilder.insert( 0, ChatColor.RED );
//            } else if ( world.getTime() >= 12010 && world.getTime() < 12969 )
//            {
//                // Beds can be used
//                worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
//            }
//        } else
//        {
//            // Weather clear
//            if ( world.getTime() >= 13188 && world.getTime() <= 22812 )
//            {
//                // Monsters are spawning
//                worldTimeStrBuilder.insert( 0, ChatColor.RED );
//            } else if ( world.getTime() >= 12542 && world.getTime() < 13188 )
//            {
//                // Beds can be used
//                worldTimeStrBuilder.insert( 0, ChatColor.YELLOW );
//            }
//        }
//        worldTimeStrBuilder.append(ChatColor.RESET );
//
//        worldTimeIn24hColorized.put(world.getUID(), worldTimeStrBuilder.toString());
//    }

    private String formatServerTime()
    {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(d1);
    }
}
