package com.github.scorchedpsyche.craftera_suite.entities.baby.listeners;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener
{
    /**
     * Prevents experience abuse from converting entities to their baby variants
     * @param entityDeathEvent Entity death event
     */
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent entityDeathEvent)
    {
        Entity entity = entityDeathEvent.getEntity();

        // Check if entity has been converted by plugin
        if( entity.hasMetadata( "ces_adult/baby" ) &&
            (
                entity.getType() == EntityType.DROWNED ||
                entity.getType() == EntityType.HUSK ||
                entity.getType() == EntityType.PIGLIN ||
                entity.getType() == EntityType.ZOMBIE ||
                entity.getType() == EntityType.ZOMBIE_VILLAGER
            ) )
        {
            // Entity is ageable since it has the tag. Check if it's not an adult
            if( !((Ageable) entity).isAdult() )
            {
                // Not adult. Then set the experience to be dropped as if it was an adult
                entityDeathEvent.setDroppedExp( entityDeathEvent.getDroppedExp() - 7  );
            }
        }
    }
}
