package com.github.scorchedpsyche.craftera_suite.entities.baby.utils;

import com.github.scorchedpsyche.craftera_suite.entities.baby.CraftEraSuiteBabyEntities;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Random;

public class EntityUtil
{
    private final Plugin plugin = CraftEraSuiteBabyEntities.getPlugin(CraftEraSuiteBabyEntities.class);
    private final String Text_CraftEra_Suite = ChatColor.AQUA + "" + ChatColor.BOLD + "[CraftEra Suite] " +
            ChatColor.RESET;

//    public boolean IsPlayerBetweenViewDistanceOfEntity(Entity entity, Player player)
//    {
//        int entityChunkX = (int)(entity.getLocation().getX() / 16);
//        int entityChunkZ = (int)(entity.getLocation().getZ() / 16);
//
//        int playerChunkX = (int)(player.getLocation().getX() / 16);
//        int playerChunkZ = (int)(player.getLocation().getZ() / 16);
//
//        double distance = Math.hypot(entityChunkX - playerChunkX,
//                                     entityChunkZ - playerChunkZ);
//
//        if(distance <= Bukkit.getViewDistance())
//        {
//            return true;
//        }
//
//        return false;
//    }

    /**
     * Checks if the item is a valid plugin Name Tag.
     * @param item Item to be checked
     * @return True if the item is a valid plugin Name Tag
     */
    public boolean playerHoldsValidNameTag(ItemStack item)
    {
        // Checks if player is holding a name tag named "ces_baby"
        if(     item != null &&
                item.getType() == Material.NAME_TAG &&
                item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().equals("ces_adult/baby")
        )
        {
            return true;
        }

        // Not a valid item
        return false;
    }

    /**
     * Checks if the entity is ageable and messages the player if entity is not.
     * @param sourcePlayer Source player trying to convert the entity
     * @param entity Entity to be converted
     */
    public void babyAdultToggleConversion(Player sourcePlayer, Entity entity)
    {
        // Is entity valid for conversion? Must be ageable
        if ( entity instanceof Ageable && entity.getType() != EntityType.PIGLIN_BRUTE)
        {
            // Entity is valid to be converted. Do conversion
            convertToBabyOrAdultAndAddTag(sourcePlayer, (Ageable) entity);

            // Check if entity is Breedable (if it ages). If so, toggle Age Lock
            if ( entity instanceof Breedable)
            {
                toggleAgeLock((Breedable) entity);
            }
        } else {
            // Entity is invalid for conversion
            sourcePlayer.sendRawMessage(
                    Text_CraftEra_Suite + ChatColor.RED + entity.getName() + ChatColor.RESET + " can't be a baby." );

            spawnParticleAtEntity( entity, Particle.EXPLOSION_NORMAL,3, 0.01 );
        }
    }

    /**
     * Attempts to toggle the conversion of the ageable entity between adult/baby, unless it's a natural baby.
     * @param sourcePlayer Source player trying to convert the entity
     * @param ageableEntity The ageable entity to be converted
     */
    private void convertToBabyOrAdultAndAddTag(Player sourcePlayer, Ageable ageableEntity)
    {
        // Check if it's adult
        if( ageableEntity.isAdult() )
        {
            // Is adult. Convert to baby
            ageableEntity.setBaby();
            ageableEntity.setMetadata( "ces_adult/baby",
                                      new FixedMetadataValue(plugin, "ces_adult/baby" ));

            // Particles
            spawnParticleAtEntity( ageableEntity, Particle.HEART, 10, 0.0001 );
        } else {
            // Is baby. Must check if this is a natural baby to prevent growth abuse
            if( ageableEntity instanceof Breedable && !ageableEntity.hasMetadata( "ces_adult/baby" ) )
            {
                // Natural baby, warn player
                sourcePlayer.sendRawMessage(
                        Text_CraftEra_Suite + ChatColor.RED + "This is a natural baby!" + ChatColor.RESET +
                                " You can't convert Vanilla entities to Adult.");
            } else {
                // Everything OK. Convert to adult
                ageableEntity.setAdult();
                ageableEntity.removeMetadata("ces_adult/baby" , plugin);

                // Particles
                spawnParticleAtEntity( ageableEntity, Particle.DAMAGE_INDICATOR, 15);
            }
        }
    }

    /**
     * Toggles the age lock (true/false) of a breedable entity
     * @param breedableEntity The target breedable entity to toggle the age lock for
     */
    private void toggleAgeLock(Breedable breedableEntity)
    {
        // Check if entity is Age Locked
        if( breedableEntity.getAgeLock() )
        {
            // Is Age Locked. Must remove lock
            breedableEntity.setAgeLock(false);
        } else {
            // Not Age Locked. Must add lock
            breedableEntity.setAgeLock(true);
        }
    }

    public void spawnParticleAtEntity(Entity entity, Particle particle, int totalParticles)
    {
        Random r = new Random();
        World world = entity.getWorld();

        double xzOffset = 0.1;
        double yRange = 1;

        double xRangeLower = entity.getLocation().getX() - entity.getWidth() - xzOffset;
        double xRangeUpper = entity.getLocation().getX() + entity.getWidth() + xzOffset;

        double yRangeLower = entity.getLocation().getY();
        double yRangeUpper = entity.getLocation().getY() + entity.getHeight();

        double zRangeLower = entity.getLocation().getZ() - entity.getWidth() - xzOffset;
        double zRangeUpper = entity.getLocation().getZ() + entity.getWidth() + xzOffset;

        for (int i = 1; i <= totalParticles; i++)
        {
            double x = xRangeLower + (xRangeUpper - xRangeLower) * r.nextDouble();
            double y = yRangeLower + (yRangeUpper - yRangeLower) * r.nextDouble();
            double z = zRangeLower + (zRangeUpper - zRangeLower) * r.nextDouble();

            world.spawnParticle(
                    particle,
                    x, // x
                    y, // y
                    z, // z
                    1 // count
               );
        }
    }

    public void spawnParticleAtEntity(Entity entity, Particle particle, int totalParticles, double extra)
    {
        Random r = new Random();
        World world = entity.getWorld();

        double xzOffset = 0.1;
        double yRange = 1;

        double xRangeLower = entity.getLocation().getX() - entity.getWidth() - xzOffset;
        double xRangeUpper = entity.getLocation().getX() + entity.getWidth() + xzOffset;

        double yRangeLower = entity.getLocation().getY();
        double yRangeUpper = entity.getLocation().getY() + entity.getHeight();

        double zRangeLower = entity.getLocation().getZ() - entity.getWidth() - xzOffset;
        double zRangeUpper = entity.getLocation().getZ() + entity.getWidth() + xzOffset;

        for (int i = 1; i <= totalParticles; i++)
        {
            double x = xRangeLower + (xRangeUpper - xRangeLower) * r.nextDouble();
            double y = yRangeLower + (yRangeUpper - yRangeLower) * r.nextDouble();
            double z = zRangeLower + (zRangeUpper - zRangeLower) * r.nextDouble();

            world.spawnParticle(
                    particle,
                    x, // x
                    y, // y
                    z, // z
                    totalParticles, // count
                    0, // offsetX
                    0, // offsetY
                    0, // offsetZ
                    extra // Usually speed
                );
        }
    }

    public Vector randomBoundedXYZ()
    {

        return new Vector(
                // TODO: Refactor code
        );
    }
}
