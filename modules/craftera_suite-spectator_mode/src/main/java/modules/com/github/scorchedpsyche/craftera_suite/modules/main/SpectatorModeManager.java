package modules.com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.*;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.CollectionUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteSpectatorMode;
import modules.com.github.scorchedpsyche.craftera_suite.modules.model.SpectatorPlayerDataModel;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SpectatorModeManager {
    public SpectatorModeManager(SpectatorDatabaseAPI spectatorDatabaseAPI) {
        this.spectatorDatabaseAPI = spectatorDatabaseAPI;

        FileConfiguration config = CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class).getConfig();

        if(config.getBoolean("use_server_view_distance_instead_of_custom_range_limit"))
        {
            rangeLimit = Bukkit.getViewDistance() * 16;
        } else {
            rangeLimit = config.getInt("custom_range_limit", 100);
        }
    }

    public HashMap<String, Double> distanceFromSource = new HashMap<>();
    public HashMap<String, SpectatorPlayerDataModel> playersInSpectator = new HashMap<>();

    private SpectatorDatabaseAPI spectatorDatabaseAPI;
    private final int rangeLimit;

    public int getRangeLimit()
    {
        return this.rangeLimit;
    }

    /**
     * Toggles Spectator Mode for the player.
     *
     * This function schedules a delayed task that receives the player's current health and location. After a 21 ticks
     * the next task is execute and these values are checked against the current health and location. If the player
     * moves or takes damage within this time, they won't be able to go into Spectator Mode.
     *
     * @param player Executing player
     * @param x Player's X position
     * @param y Player's Y position
     * @param z Player's Z position
     * @param health Player's health
     */
    public void toggleSpectatorModeForPlayer(Player player, double x, double y, double z, double health)
    {
        // Check if the player is in Spectator Mode
        if( player.getGameMode().equals(GameMode.SPECTATOR) )
        {
            // Is in Spectator. Disable it and go back to previous mode
            disableSpectatorModeForPlayer(player);
        } else {
            // Is not in Spectator. Enable it

            // Starts a task more than 1 second after the command was executed and send the location and
            // player health as value so that it can be checked if the player is moving or taking damage.
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                        CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),
                        () -> enableSpectatorModeForPlayer(
                                player,
                                x,
                                y,
                                z,
                                health),
                        21L
                    );
        }
    }

    /**
     * Enables Spectator Mode for the player.
     *
     * Checks player's previous health and location with the current ones.
     *
     * @param player Executing player
     * @param x Player's X position
     * @param y Player's Y position
     * @param z Player's Z position
     * @param health Player's health
     */
    private void enableSpectatorModeForPlayer(Player player, double x, double y, double z, double health)
    {
        // Checks player location to see if they moved
        if( player.getLocation().getX() == x &&
            player.getLocation().getY() == y &&
            player.getLocation().getZ() == z )
        {
            // Didn't move. Must check if they were damaged
            if( health == player.getHealth() )
            {
                SpectatorPlayerDataModel playerData = spectatorDatabaseAPI.enableSpectatorModeForPlayer(player);

                // They weren't damaged. Attempt to save player state in the database
                if ( playerData != null )
                {
                    // Player state saved. Summon Armor Stand, set player to Spectator Mode
                    playersInSpectator.put(player.getUniqueId().toString(), playerData);
                    player.setGameMode(GameMode.SPECTATOR);
                    spawnArmorStand(player);
                    EntityUtils.notifyNearbyPlayersOfEntityUpdate(player);
                    PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                            "Enabled");
                    CraftEraSuiteSpectatorMode.startRepeatingTaskIfNotRunningAndPlayersOnlineAndInSpectator();
                } else {
                    // Failed to save player state. Display error to player
                    PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                            "Unable to go into Spectator mode! Contact server's admin if this should've worked.");
                }
            } else {
                // They were damaged. Warn player
                PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                        "To prevent abuse you cannot go into Spectator mode if you're taking damage.");
            }
        } else {
            // They moved. Warn player
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "To prevent abuse you cannot go into Spectator mode if you're moving.");
        }
    }

    /**
     * Disabled spectator mode for the executing player.
     *
     * @param player Executing player
     */
    private void disableSpectatorModeForPlayer(Player player)
    {
        // Attempts to update player state in database
        SpectatorPlayerDataModel playerData = spectatorDatabaseAPI.disableSpectatorModeForPlayer(player);

        if( playerData != null )
        {
            // Player state updated. Return player to original executing location, set original game mode and
            // delete Armor Stand
            player.teleport(
                    new Location(
                            player.getWorld(),
                            playerData.getX(),
                            playerData.getY(),
                            playerData.getZ(),
                            player.getLocation().getYaw(),
                            player.getLocation().getPitch()));

            switch(playerData.getGameMode())
            {
                case "SURVIVAL":
                    player.setGameMode(GameMode.SURVIVAL);
                    break;

                case "ADVENTURE":
                    player.setGameMode(GameMode.ADVENTURE);
                    break;

                default: // CREATIVE
                    player.setGameMode(GameMode.CREATIVE);
                    break;
            }
            removeArmorStand(player);
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Disabled");
            playersInSpectator.remove(player.getUniqueId().toString());
            distanceFromSource.remove(player.getUniqueId().toString());
            CraftEraSuiteSpectatorMode.cancelRepeatingTaskIfNoPlayersOnlineOrNoneInSpectator();

//            JavaPlugin plugin = CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class);

            EntityUtils.notifyNearbyPlayersOfEntityUpdate(player);
        } else {
            // Failed to update player state. Display error to player
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Unable to get out of Spectator mode! Contact server's admin if this should've worked.");
        }
    }

    /**
     * Spawns Armor Stand at player location with the executing player's head equipped
     * @param player Executing player
     */
    private void spawnArmorStand(Player player)
    {
        ArmorStand as = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        as.getLocation().setYaw(player.getLocation().getYaw());
        as.setGravity(false);
        as.getEquipment().setHelmet(PlayerHeadUtils.playerHeadItemStackFromOfflinePlayer(1, player), true);
        as.setInvulnerable(true);
        as.setInvisible(true);

        // Tags the Armor Stand with executing player's UUID so that it can be matched when deleted
        as.getPersistentDataContainer().set(
                new NamespacedKey(CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class), "ces-sm_uuid"),
                PersistentDataType.STRING,
                player.getUniqueId().toString()
        );
    }

    /**
     * Removes the Armor Stand spawned when player executed the command
     * @param player Executing player
     */
    private void removeArmorStand(Player player)
    {
        // Gather all Armor Stands at player's original executing location
        Collection<Entity> armorStandsOnPlayerLocation = player.getWorld().getNearbyEntities(
                player.getLocation(),
                0.5,
                1,
                0.5,
                (entity) -> entity.getType() == EntityType.ARMOR_STAND);

        // Check if found any – could be admin on Spectator or someone deleted it with commands
        if( !armorStandsOnPlayerLocation.isEmpty() )
        {
            // Found Armor Stands.
            String playerUUIDasString = player.getUniqueId().toString();
            armorStandsOnPlayerLocation.forEach(
                    as -> {
                        NamespacedKey namespacedKey = new NamespacedKey(CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),"ces-sm_uuid");

                        if( as.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING).equals(playerUUIDasString) )
                        {
                            as.remove();
                        }
                    }
            );
        } else {
            // No Armor Stands. Warn player
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Unable to find your Armor Stand! If this isn't right, contact admin.");
        }
    }

    public void calculatePlayersDistanceToOriginalExecutingLocationAndGenerateHudText()
    {
        if(!CollectionUtils.isNullOrEmpty(playersInSpectator))
        {
            playersInSpectator.forEach((uuid, playerDataFromDb) ->
                    {
                        Player player = Bukkit.getServer().getPlayer(UUID.fromString(uuid));
                        if( player != null )
                        {
                            Location playerLocation = new Location(
                                    player.getWorld(),
                                    playerDataFromDb.getX(),
                                    playerDataFromDb.getY(),
                                    playerDataFromDb.getZ()
                            );

                            double distanceFromCastingLocation = PlayerUtils.getDistanceToLocation(
                                    player,
                                    playerLocation
                            );

                            if(distanceFromSource.putIfAbsent(uuid, distanceFromCastingLocation) != null )
                            {
                                distanceFromSource.replace(uuid, distanceFromCastingLocation);
                            }

                            if (distanceFromCastingLocation > rangeLimit)
                            {
                                player.teleport(new Location(
                                        player.getWorld(),
                                        playerDataFromDb.getX(),
                                        playerDataFromDb.getY(),
                                        playerDataFromDb.getZ(),
                                        playerLocation.getYaw(),
                                        playerLocation.getPitch()
                                ));
                            }
                        }
                    }
            );
        }
    }

    public void addOnlinePlayersToSpectator()
    {
        List<SpectatorPlayerDataModel> playersOnSpectatorOnDb = spectatorDatabaseAPI.fetchAllPlayersWithSpectatorModeEnabled();

        if ( !CollectionUtils.isNullOrEmpty(playersOnSpectatorOnDb) )
        {
            playersOnSpectatorOnDb.stream().forEach( playerOnDb ->
                    {
                        if( Bukkit.getOnlinePlayers().stream().anyMatch(
                                p -> p.getUniqueId().toString().equals(playerOnDb.getPlayerUUID())
                        ) )
                        {
                            playersInSpectator.put(playerOnDb.getPlayerUUID(), playerOnDb);
                        }
                    }

            );
        }
    }

    public void playerLogin(Player player)
    {
        SpectatorPlayerDataModel playerData = spectatorDatabaseAPI.fetchPlayer(player.getUniqueId().toString());

        if( playerData != null )
        {
            playersInSpectator.put(player.getUniqueId().toString(), playerData);
        }

        CraftEraSuiteSpectatorMode.startRepeatingTaskIfNotRunningAndPlayersOnlineAndInSpectator();
    }

    public void playerLogout(Player player)
    {
        playersInSpectator.remove(player.getUniqueId().toString());

        CraftEraSuiteSpectatorMode.cancelRepeatingTaskIfNoPlayersOnlineOrNoneInSpectator();
    }
}
