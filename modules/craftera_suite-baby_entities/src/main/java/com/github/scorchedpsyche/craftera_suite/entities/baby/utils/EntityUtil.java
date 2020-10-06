package com.github.scorchedpsyche.craftera_suite.entities.baby.utils;

import org.bukkit.*;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class EntityUtil
{
    public boolean IsPlayerBetweenViewDistanceOfEntity(Entity entity, Player player)
    {
        int entityChunkX = (int)(entity.getLocation().getX() / 16);
        int entityChunkZ = (int)(entity.getLocation().getZ() / 16);

        int playerChunkX = (int)(player.getLocation().getX() / 16);
        int playerChunkZ = (int)(player.getLocation().getZ() / 16);

        double distance = Math.hypot(entityChunkX - playerChunkX,
                                     entityChunkZ - playerChunkZ);

        if(distance <= Bukkit.getViewDistance())
        {
            return true;
        }

        return false;
    }

    public boolean PlayerHoldsValidNameTag(ItemStack item)
    {
        // Checks if player is holding a name tag named "ces_baby"
        if(     item != null &&
                item.getType() == Material.NAME_TAG &&
                item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().equals("ces_baby/adult")
        )
        {
            return true;
        }

        // Not a valid item
        return false;
    }

    /**
     * Checks if entity is Ageable and Breedable
     * @param entity Target entity to be converted to baby.
     * @return True if the entity is both Ageable and Breedable
     */
    public boolean IsAgeableAndBreedable(Entity entity)
    {
        if( entity instanceof Ageable && entity instanceof Breedable )
        {
            // Entity is valid to become a baby
            return true;
        }

        // Entity is invalid for conversion
        return false;
    }

    public void SpawnParticleAtEntity(Entity entity, Particle particle, int totalParticles)
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

    public void SpawnParticleAtEntity(Entity entity, Particle particle, int totalParticles, double extra)
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

    public Vector RandomBoundedXYZ()
    {

        return new Vector(

        );
    }
}
