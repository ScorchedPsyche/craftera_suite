package com.github.scorchedpsyche.craftera_suite.modules.sleep.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.sleep.CraftEraSuiteSleep;
import com.github.scorchedpsyche.craftera_suite.modules.sleep.model.WorldNightManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SleepManager {
    public SleepManager()
    {
        // Populate worlds
        for( World world : Bukkit.getWorlds() )
        {
            worlds.put(world, new WorldNightManager(world));
        }
    }

    private HashMap<World, WorldNightManager> worlds = new HashMap<>();

    /**
     * Processes any event of a player trying to sleep, whether it's valid or not (E.g.: trying to sleep in the Nether/End).
     * @param event The event of the player that is trying to sleep which must be processed by the plugin
     */
    public void playerIsTryingToSleep(PlayerBedEnterEvent event)
    {
        // Check if it's OK for the player to enter the bed right now by Vanilla standards
        if( event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK )
        {
            // Sleep is Vanilla-valid, which is either:
            // 1 - It's night;
            // 2 - It's day and thundering

            // Add the world to Night Manager if it doesn't exists
            WorldNightManager worldNight = worlds.putIfAbsent(
                event.getPlayer().getWorld(), new WorldNightManager(event.getPlayer().getWorld()));

            // Add player to the playersInBed list
            worldNight.addPlayerInBed(event.getPlayer());

            // Remove player reservation if they have one
            assert worldNight != null;
            this.removeNightReservationIfExistsAndWarnPlayers(event.getPlayer(), worldNight);

            // Check if the night is still reserved
            if( !worldNight.hasReservations() )
            {
                // No reservations. Check if it's Night time
                if( WorldUtil.canBedsBeUsed(worldNight.getWorld()) )
                {
                    // Night Time
                    this.initiateTimeSkipIfNotAlreadyStarted(worldNight);
//                    worldNight.addPlayerWhoSlept( event.getPlayer() );
                } else {
                    // Day Time. Since the sleep attempt is Vanilla valid, then it must be thundering.
                    // Check if the plugin allows sleep during thunderstorm
                    if( CraftEraSuiteSleep.config.getInt("chance_to_clear_weather_after_players_sleep", 100) > 0
                            && CraftEraSuiteSleep.config.getBoolean("can_player_skip_weather_by_sleeping_during_the_day", true) )
                    {
                        // It's OK to sleep to skip thunderstorms
                        this.initiateTimeSkipIfNotAlreadyStarted(worldNight);
//                        worldNight.addPlayerWhoSlept( event.getPlayer() );
                    } else {
                        // Cannot skip thunderstorms because of server configuration
                        event.setCancelled(true);
                        sendMessageToPlayer(event.getPlayer(), new StringFormattedModel()
                            .add("Server is configured so that ").redR("you cannot skip weather")
                            .add(" during the day!").toString());
                    }
                }
            } else {
                // Night still reserved. Warn player
                event.setCancelled(true);
                sendMessageToPlayer(event.getPlayer(), new StringFormattedModel()
                        .add("Night is ").redR("reserved").add(" by:").nl()
                        .add(worldNight.getStringOfPlayersWithReservationInWorld()).toString());
            }
        }
    }

    private void initiateTimeSkipIfNotAlreadyStarted(@NotNull WorldNightManager worldNight)
    {
        // Check if time is already being skipped
        if( !worldNight.isSkippingTheNight() )
        {
            // World is not skipping the night. Initiate it
            worldNight.setSkippingTheNight(true);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    CraftEraSuiteSleep.getPlugin(CraftEraSuiteSleep.class), new Runnable(){
                        @Override
                        public void run(){
                            doNightSkip(worldNight);
                        }
                    }, 100L);
        }
    }

    private void doNightSkip(@NotNull WorldNightManager worldNight)
    {
        // Check if night was being skipped
        if( worldNight.isSkippingTheNight() )
        {
            // Check if any players in world are asleep
            if( worldNight.isThereAtLeastOnePlayerInBed() )
            {
                // At least one player asleep. Attempt to skip the night
                if( WorldUtil.skipNightUntilBedsCannotBeUsed(worldNight.getWorld()) )
                {
                    // Not yet daylight. Schedule another night skip
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                            CraftEraSuiteSleep.getPlugin(CraftEraSuiteSleep.class), new Runnable(){
                                @Override
                                public void run(){
                                    doNightSkip(worldNight);
                                    attemptToEndNightSkip(worldNight);
                                }
                            }, 1L);
                }
            }
        }


//        // Check if any players in world are asleep
//        if( WorldUtil.isThereAtLeastOnePlayerSleepingInWorld(worldNight.getWorld()) )
//        {
//            ConsoleUtil.debugMessage("0");
//            // At least one player asleep. Attempt to skip the night
//            if( WorldUtil.attemptTimeSkipIfNotSunrise(worldNight.getWorld()) )
//            {
//                ConsoleUtil.debugMessage("1");
//                // Not yet daylight. Schedule another night skip
//                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
//                        CraftEraSuiteSleep.getPlugin(CraftEraSuiteSleep.class), new Runnable(){
//                            @Override
//                            public void run(){
//                                doNightSkip(worldNight);
//                            }
//                        }, 1L);
//            }
//        } else {
//            // Already day. Check if night was being skipped
//            if( worldNight.isSkippingTheNight() )
//            {
//                // Night was being skipped. Reset everything and let other players know who skipped the night
//                ConsoleUtil.debugMessage("2");
//                WorldUtil.attemptToClearWeatherDependingOnChance(
//                        worldNight.getWorld(),
//                        CraftEraSuiteSleep.config.getInt("chance_to_clear_weather_after_players_sleep_through_the_night", 100));
//                worldNight.setSkippingTheNight(false);
//                sendMessageToAllPlayersInWorld(worldNight.getWorld(), new StringFormattedModel()
//                        .add("Sleepy ones:").nl()
//                        .add(worldNight.getStringOfPlayersWhoSlept()));
//                worldNight.resetReservations();
//                WorldUtil.wakeAllPlayers(worldNight.getWorld());
//            }
//        }
    }

    private void attemptToEndNightSkip(@NotNull WorldNightManager worldNight)
    {
        // There are no asleep players. Check if the world is at beds can be used end time
        if( WorldUtil.isWorldAtBedsCanBeUsedEndTime(worldNight.getWorld()) )
        {
            // World is one tick after beds can be used ALONG with night was being skipped.
            // This means we should reset everything and let other players know who skipped the night
            WorldUtil.attemptToClearWeatherDependingOnChance(
                    worldNight.getWorld(),
                    CraftEraSuiteSleep.config.getInt("chance_to_clear_weather_after_players_sleep_through_the_night", 100));
            sendMessageToAllPlayersInWorld(worldNight.getWorld(), new StringFormattedModel()
                    .add("Sleepy ones: ").add(worldNight.getStringOfPlayersInBed()));
            worldNight.resetReservations();
            WorldUtil.wakeAllPlayers(worldNight.getWorld());
            worldNight.setSkippingTheNight(false);
        }
    }

    public void toggleNightReservationForPlayer(Player player)
    {

        // Check if world has daylight cycle
        if( player.getWorld().getEnvironment() != World.Environment.THE_END
            && player.getWorld().getEnvironment() != World.Environment.NETHER)
        {
            WorldNightManager worldNight = worlds.get( player.getWorld() );

            // Attempt to add a night reservation for player
            if( !worldNight.playerHasReservation(player) )
            {
                this.addNightReservationIfPossibleAndWarnPlayers(player, worldNight);
            } else {
                this.removeNightReservationIfExistsAndWarnPlayers(player, worldNight);
            }
        } else {
            sendMessageToPlayer(player, new StringFormattedModel().add("This world ").redR("doesn't have")
                .add(" a daylight cycle.").toString());
        }
    }

    public void addNightReservationIfPossibleAndWarnPlayers(Player player, WorldNightManager worldNight)
    {
        // Attempt to remove night reservation for player
        if( worldNight.addNightReservationIfPossible(player) )
        {
            // Didn't have a night reservation. Let players know
            StringFormattedModel message = new StringFormattedModel().aquaR(player.getDisplayName()).add(" has ")
                    .greenR("reserved").add(" the night!");
            sendMessageToAllPlayersInWorld(worldNight.getWorld(), message);
        }
    }

    public void removeNightReservationIfExistsAndWarnPlayers(Player player, WorldNightManager worldNight)
    {
        // Attempt to remove night reservation for player
        if( worldNight.removeNightReservationIfExists(player) )
        {
            // Didn't have a night reservation. Let players know
            StringFormattedModel message = new StringFormattedModel().aquaR(player.getDisplayName()).add(" has ")
                .redR("unreserved").add(" the night!");
            sendMessageToAllPlayersInWorld(worldNight.getWorld(), message);
        }
    }

    public void playerChangedWorld(Player player, World fromWorld)
    {
        // Add the world to Night Manager if it doesn't exists
        WorldNightManager worldNight = worlds.putIfAbsent( fromWorld, new WorldNightManager(fromWorld));

        assert worldNight != null;
        removeNightReservationIfExistsAndWarnPlayers(player, worldNight);
    }

    public void playerLeftBed(Player player)
    {
        // Add the world to Night Manager if it doesn't exists
        WorldNightManager worldNight = worlds.putIfAbsent( player.getWorld(), new WorldNightManager(player.getWorld()));

        assert worldNight != null;
        worldNight.removePlayerInBed(player);
    }

    public void playerLeftTheGame(Player player)
    {
        // Add the world to Night Manager if it doesn't exists
        WorldNightManager worldNight = worlds.putIfAbsent( player.getWorld(), new WorldNightManager(player.getWorld()));

        assert worldNight != null;
        worldNight.removePlayerInBed(player);
        this.removeNightReservationIfExistsAndWarnPlayers(player, worldNight);
    }

    private void sendMessageToAllPlayersInWorld(World world, StringFormattedModel message)
    {
        for( Player playerInWorld : world.getPlayers() )
        {
            this.sendMessageToPlayer(playerInWorld, message.toString());
        }
    }

    private void sendMessageToPlayer(Player player, String message)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Sleep.Name.compact, message);
    }
}
