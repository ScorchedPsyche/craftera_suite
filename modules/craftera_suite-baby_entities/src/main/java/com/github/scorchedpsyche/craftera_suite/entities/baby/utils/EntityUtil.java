package com.github.scorchedpsyche.craftera_suite.entities.baby.utils;

import com.github.scorchedpsyche.craftera_suite.entities.baby.CraftEraSuiteBabyEntities;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ParticleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class EntityUtil
{
    /**
     * Checks if the item is a valid plugin Name Tag named "ces_adult/baby".
     *
     * @param item Item to be checked
     * @return True if the item is a valid plugin Name Tag
     */
    public boolean playerHoldsValidNameTag(ItemStack item)
    {
        if( item != null && item.getType() == Material.NAME_TAG )
        {
            // Checks if player is holding a name tag named "ces_adult/baby"
            return item.getItemMeta() != null && item.getItemMeta().hasDisplayName() &&
                    item.getItemMeta().getDisplayName().equals("ces_adult/baby");
        }

        return false;
    }

    /**
     * Checks if the entity is ageable and messages the player if entity is not.
     *
     * @param sourcePlayer Source player trying to convert the entity
     * @param entity       Entity to be converted
     */
    public void babyAdultToggleConversion(Player sourcePlayer, Entity entity)
    {
        // Is entity valid for conversion? Must be ageable
        if (entity instanceof Ageable && entity.getType() != EntityType.PIGLIN_BRUTE)
        {
            // Entity is valid to be converted. Do conversion
            convertToBabyOrAdultAndAddTag(sourcePlayer, (Ageable) entity);

            // Check if entity is Breedable (if it ages). If so, toggle Age Lock
            if (entity instanceof Breedable)
            {
                toggleAgeLock((Breedable) entity);
            }
        } else
        {
            // Entity is invalid for conversion
            PlayerUtils.sendMessageWithPluginPrefix(
                    sourcePlayer, SuitePluginManager.BabyEntities.Name.compact,
                    ChatColor.RED + entity.getName() + ChatColor.RESET + " can't be a baby.");

            ParticleUtils.spawnParticleAtEntity(entity, Particle.EXPLOSION_NORMAL, 3, 0.01);
        }
    }

    /**
     * Attempts to toggle the conversion of the ageable entity between adult/baby, unless it's a natural baby.
     *
     * @param sourcePlayer  Source player trying to convert the entity
     * @param ageableEntity The ageable entity to be converted
     */
    private void convertToBabyOrAdultAndAddTag(Player sourcePlayer, Ageable ageableEntity)
    {
        // Check if it's adult
        if (ageableEntity.isAdult())
        {
            // Is adult. Convert to baby
            ageableEntity.setBaby();
            ageableEntity.setMetadata("ces_adult/baby",
                                      new FixedMetadataValue(
                                              CraftEraSuiteBabyEntities.getPlugin(CraftEraSuiteBabyEntities.class),
                                              "ces_adult/baby"));

            // Notify nearby players' clients of conversion so that they visually update the entity
            notifyNearbyPlayersOfEntityConversion(ageableEntity);

            // Particles
            ParticleUtils.spawnParticleAtEntity(ageableEntity, Particle.HEART, 10, 0.0001);
        } else
        {
            // Is baby. Must check if this is a natural baby to prevent growth abuse
            if (ageableEntity instanceof Breedable && !ageableEntity.hasMetadata("ces_adult/baby"))
            {
                // Natural baby, warn player
                PlayerUtils.sendMessageWithPluginPrefix(
                        sourcePlayer, SuitePluginManager.BabyEntities.Name.compact,
                        ChatColor.RED + ageableEntity.getName() + ChatColor.RESET + " is a natural baby!"
                                + ChatColor.RESET + " You can't convert Vanilla entities to Adult.");
            } else
            {
                // Everything OK. Convert to adult
                ageableEntity.setAdult();
                ageableEntity.removeMetadata("ces_adult/baby",
                                             CraftEraSuiteBabyEntities.getPlugin(CraftEraSuiteBabyEntities.class));

                // Notify nearby players' clients of conversion so that they visually update the entity
                notifyNearbyPlayersOfEntityConversion(ageableEntity);

                // Particles
                ParticleUtils.spawnParticleAtEntity(ageableEntity, Particle.DAMAGE_INDICATOR, 15);
            }
        }
    }

    /**
     * Toggles the age lock (true/false) of a breedable entity
     * @param breedableEntity The target breedable entity to toggle the age lock for
     */
    private void toggleAgeLock(Breedable breedableEntity)
    {
        // Check if entity is Age Locked and removes/adds lock accordingly
        breedableEntity.setAgeLock(!breedableEntity.getAgeLock());
    }

    /**
     * Notify nearby players' client of the entity conversion so that their client updates
     * the entity visually – entity size won't be changed for nearby players otherwise.
     * @param targetEntity Entity that was converted
     */
    private void notifyNearbyPlayersOfEntityConversion(Entity targetEntity)
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
    public boolean isPlayerBetweenViewDistanceOfEntity(Entity entity, Player player)
    {
        int entityChunkX = (int)(entity.getLocation().getX() / 16);
        int entityChunkZ = (int)(entity.getLocation().getZ() / 16);

        int playerChunkX = (int)(player.getLocation().getX() / 16);
        int playerChunkZ = (int)(player.getLocation().getZ() / 16);

        double distance = Math.hypot(entityChunkX - playerChunkX,
                                     entityChunkZ - playerChunkZ);

        return distance <= Bukkit.getViewDistance();
    }

    public Vector randomBoundedXYZ()
    {
        return new Vector(
                // TODO: Refactor code
        );
    }
}
