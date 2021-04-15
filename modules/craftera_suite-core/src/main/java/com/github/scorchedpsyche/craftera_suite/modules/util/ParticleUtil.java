package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Random;

public class ParticleUtil
{
    public static void spawnParticleAtEntity(Entity entity, Particle particle, int totalParticles)
    {
        Random r = new Random();
        World world = entity.getWorld();

        double xzOffset = 0.1;

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

    public static void spawnParticleAtEntity(Entity entity, Particle particle, int totalParticles, double extra)
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
}
