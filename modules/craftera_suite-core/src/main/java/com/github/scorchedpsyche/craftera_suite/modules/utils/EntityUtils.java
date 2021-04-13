package com.github.scorchedpsyche.craftera_suite.modules.utils;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityUtils {

    /**
     * Notify nearby players' of entity's update so that their client updates the entity
     * visually – entity size, for example, won't be changed for nearby players otherwise.
     * @param targetEntity Entity that was modified
     */
    public static void notifyNearbyPlayersOfEntityUpdate(Entity targetEntity)
    {
        // Create packet to notify players of the conversion
        DataWatcher watcher = ((CraftEntity) targetEntity).getHandle().getDataWatcher();
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(
                targetEntity.getEntityId(), // Entity ID
                watcher, // Data watcher which you can get by accessing a method in a NMS Entity class
                false // Send All
        );

        // Notify nearby players
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check if in the same dimension
            if( targetEntity.getWorld().getUID() == player.getWorld().getUID() )
            {
                if( isPlayerBetweenViewDistanceOfEntity(targetEntity, player) )
                {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
    }

    /**
     * Check if the player is between view distance of the entity.
     * @param entity The entity to be checked against
     * @param player The player to be check against
     * @return True if the player is inside entity's view distance
     */
    public static boolean isPlayerBetweenViewDistanceOfEntity(Entity entity, Player player)
    {
        int entityChunkX = (int)(entity.getLocation().getX() / 16);
        int entityChunkZ = (int)(entity.getLocation().getZ() / 16);

        int playerChunkX = (int)(player.getLocation().getX() / 16);
        int playerChunkZ = (int)(player.getLocation().getZ() / 16);

        double distance = Math.hypot(entityChunkX - playerChunkX,
                entityChunkZ - playerChunkZ);

        return distance <= Bukkit.getViewDistance();
    }
}
