package com.github.scorchedpsyche.craftera_suite.modules.sleep.model;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.sleep.CraftEraSuiteSleep;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorldNightManager {
    public WorldNightManager(@NotNull World world) {
        this.world = world;
        this.reservations = new ArrayList<>();
        this.playersInBed = new ArrayList<>();
    }

    @NotNull
    private final World world;
    private List<Player> reservations;
    private List<Player> playersInBed;
    private boolean skippingTheNight = false;

    @NotNull
    public World getWorld()
    {
        return this.world;
    }
    public List<Player> getReservations()
    {
        return reservations;
    }

    public boolean isSkippingTheNight() {
//        ConsoleUtil.debugMessage("setSkippingTheNight: " + skippingTheNight);
        return skippingTheNight;
    }
    public void setSkippingTheNight(boolean value)
    {
//        ConsoleUtil.debugMessage("setSkippingTheNight: " + value);
        this.skippingTheNight = value;
    }

    /**
     * Attempts to add a night reservation for the player.
     * @param player The player that wishes to reserve the night
     * @return True if the night was reserved for the player. False if the player already had a reservation
     */
    public boolean addNightReservationIfPossible(Player player)
    {
        if( reservations.contains(player) )
        {
            return false;
        }

        reservations.add(player);
        return true;
    }

    /**
     * Attempts to remove the player's night reservation.
     * @param player The player to attempt to remove the reservation for
     * @return True if the night reservation for the player was removed. False if the player didn't have a reservation
     */
    public boolean removeNightReservationIfExists(Player player)
    {
        return reservations.remove(player);
    }

    public void resetReservations()
    {
        reservations.clear();
//        playersWhoSlept.clear();
    }

    /**
     * Checks if the world has any night reservations.
     * @return True if there is any night reservation
     */
    public boolean hasReservations()
    {
        return !reservations.isEmpty();
    }

    /**
     * Checks if the player has a night reservation for the world.
     * @return True if there is a world night reservation for the player
     */
    public boolean playerHasReservation(Player player)
    {
        return reservations.contains(player);
    }

    /**
     * Checks if players can sleep at the moment:
     * 1 - thundering;
     * 2 - if players can sleep through the day to skip it OR if chance to clear weather is 0.
     * @return True if players can sleep in this world at the moment
     */
    public boolean canSleepThroughThunderstorms()
    {
        return world.isThundering()
                && CraftEraSuiteSleep.config.getBoolean("can_players_skip_thunderstorms_by_sleeping_during_the_day", true);
    }
    /**
     * Checks if players can sleep at the moment:
     * 1 - thundering;
     * 2 - if players can sleep through the day to skip it OR if chance to clear weather is 0.
     * @return True if players can sleep in this world at the moment
     */
    public boolean canSleepThroughWeather()
    {
        return CraftEraSuiteSleep.config.getInt("chance_to_clear_weather_after_players_sleep_through_the_night", 100) != 0;
    }

    public StringFormattedModel getStringOfPlayersWithReservationInWorld()
    {
        if( !reservations.isEmpty() )
        {
            StringFormattedModel listOfPlayers = new StringFormattedModel();

            for(int i = 0; i < reservations.size(); i++)
            {
                listOfPlayers.aquaR(reservations.get(i).getDisplayName());
                if( i != reservations.size() - 1)
                {
                    listOfPlayers.add(", ");
                } else {
                    listOfPlayers.add(".");
                }
            }

            return listOfPlayers;
        }

        return new StringFormattedModel();
    }

    public StringFormattedModel getStringOfPlayersInBed()
    {
        if( !playersInBed.isEmpty() )
        {
            StringFormattedModel listOfPlayers = new StringFormattedModel();

            for(int i = 0; i < playersInBed.size(); i++)
            {
                listOfPlayers.aquaR(playersInBed.get(i).getDisplayName());
                if( i != playersInBed.size() - 1)
                {
                    listOfPlayers.add(", ");
                } else {
                    listOfPlayers.add(".");
                }
            }

            return listOfPlayers;
        }

        return new StringFormattedModel();
    }

    public void addPlayerInBed(Player player)
    {
        if( !playersInBed.contains(player) )
        {
//            ConsoleUtil.debugMessage("addPlayerInBed");
            playersInBed.add(player);
        }
    }

    public void removePlayerInBed(Player player)
    {
//        ConsoleUtil.debugMessage("removePlayerInBed");
        playersInBed.remove(player);

        // After player removal from bed check if there are still players in bed
        if( playersInBed.isEmpty() )
        {
            // No players in bed. Then we must set skipping the night as false
            this.skippingTheNight = false;
        }
    }

    public boolean isThereAtLeastOnePlayerInBed()
    {
        return !this.playersInBed.isEmpty();
    }

    private void sendMessageToPlayer(Player player, String message)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Sleep.Name.compact, message);
    }
}
