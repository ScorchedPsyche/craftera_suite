package com.github.scorchedpsyche.craftera_suite.entities.baby.listeners;

import com.github.scorchedpsyche.craftera_suite.entities.baby.utils.EntityUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityNamingListener implements Listener {
    EntityUtil entityUtil = new EntityUtil();

    /**
     * Listens to player right-click interaction and checks if the target entity is renamed to "ces_adult/baby".
     * @param event Player interaction event.
     * **/
    @EventHandler
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event)
    {
        Player sourcePlayer = event.getPlayer();
        ItemStack mainHandItem =    sourcePlayer.getEquipment() != null ?
                                    sourcePlayer.getEquipment().getItemInMainHand() : null;
        ItemStack offHandItem =    sourcePlayer.getEquipment() != null ?
                sourcePlayer.getEquipment().getItemInOffHand() : null;

        // Checks if player is holding a name tag named "ces_baby/adult"
        if( ( mainHandItem != null && entityUtil.playerHoldsValidNameTag(mainHandItem) ) ||
            ( offHandItem != null && entityUtil.playerHoldsValidNameTag(offHandItem) )

        )
        {
            // Player holds a named name tag. Must cancel the default Right-Click event
            // as to not spend the Name Tag and not rename the entity
            event.setCancelled(true);

            if( sourcePlayer.getCooldown(Material.NAME_TAG) == 0 )
            {
                // Add cooldown to Name Tag as to avoid spam
                sourcePlayer.setCooldown(Material.NAME_TAG, 5);

                entityUtil.babyAdultToggleConversion(sourcePlayer, event.getRightClicked() );
            }
        }
    }
}
