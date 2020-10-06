package com.github.scorchedpsyche.craftera_suite.entities.baby.listeners;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

public class EntityDropItemListener implements Listener
{
    /**
     * Prevents Turtle growth abuse by preventing Scute from dropping if entity was converted into
     * baby with the plugin.
     * @param onEntityDropItem Entity drop item event
     */
    @EventHandler
    public void onEntityDropItemEvent(EntityDropItemEvent onEntityDropItem)
    {
        // Check is entity is ageable and breedable
        if( onEntityDropItem.getEntity() instanceof Ageable)
        {
            // Check for plugin metadata
            if( onEntityDropItem.getEntity().hasMetadata("ces_adult/baby") )
            {
                // Check if it's a turtle
                if(onEntityDropItem.getEntity().getType() == EntityType.TURTLE)
                {
                    // Cancel Scute drop
                    onEntityDropItem.setCancelled(true);
                }
            }
        }
    }
}
