package com.github.scorchedpsyche.craftera_suite.modules.model;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Model for HUD info that is shared between players.
 */
public class GlobalHudInfoModel {
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

    /**
     * Updates the info on the model.
     */
    public void updateGlobalHudInfo()
    {
        // Server time in 24h format
        serverTime = this.formatServerTime();
        for( World world : Bukkit.getWorlds() )
        {
            formatTimeForWorld(world);
        }
    }

    /**
     * Formats World time for a specific world.
     * @param world The world to format the World Time for
     */
    private void formatTimeForWorld(World world)
    {
        // Populates the HashMaps with the base world time in ticks
        this.worldTimeIn24h.put(world.getUID(), getWorldTimeIn24h(world));
        this.worldTimeIn24hColorized.put(world.getUID(), getWorldTimeIn24h(world));
        this.worldTimeInTicks.put(world.getUID(), String.valueOf(world.getTime()));
        this.worldTimeInTicksColorized.put(world.getUID(), String.valueOf(world.getTime()));

        // Gets the world color based on the World Time in ticks and colorize the strings on the appropriate HashMaps
        ChatColor color = getWorldTimeColor(world);
        colorizeTimeForWorld(world, color);
    }

    /**
     * Colorizes the World Time for a specific world.
     * @param world The world to have it's World Time text colorized
     * @param color The color to set for the world time text
     */
    private void colorizeTimeForWorld(World world, ChatColor color)
    {
        // Gets the text inside the HashMap and replace it with a colorized one
        this.worldTimeIn24hColorized.put(world.getUID(), new StringFormattedModel()
                .add(color).add(this.worldTimeIn24hColorized.get(world.getUID())).reset().toString());
        this.worldTimeInTicksColorized.put(world.getUID(), new StringFormattedModel()
                .add(color).add(this.worldTimeInTicksColorized.get(world.getUID())).reset().toString());
    }

    /**
     * Get the colors that the World Time for a specific world should be.
     * @param world The world which will be processed to find out which color it should be colorized with
     * @return The ChatColor that the text for this world should use
     */
    private ChatColor getWorldTimeColor(World world)
    {
        ChatColor color = ChatColor.WHITE;

        // Check which color should we use based on the current World Time
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

    /**
     * Formats the World Time from ticks to a 24h format for a specific world.
     * @param world The world to get the 24h format for
     * @return A string with the World Time converted from Ticks to a 24h format
     */
    private String getWorldTimeIn24h(World world)
    {
        // Convert ticks to hours and minutes
        // NOTE: we must add 6 to hours so that the World Time in a 24h format makes sense.
        // NOTE: if we don't do this then the day begins at 1:00 (1 AM). Minecraft...
        int hours = (int) Math.floor(world.getTime()/1000.0) + 6;
        int minutes = (int) Math.floor((world.getTime() % 1000.0) / 1000 * 60);

        // Because of the offset of 6 we must correct the time to that it doesn't overflow the 24 hours in a day
        if( hours >= 24 )
        {
            hours = hours - 24;
        }

        return new StringBuilder( String.format("%02d", hours) ).append(":").append(String.format("%02d", minutes)).toString();
    }

    /**
     * Gets the server time and formats it to a 24h format.
     * @return The server time as a 24h format
     */
    private String formatServerTime()
    {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(d1);
    }
}
