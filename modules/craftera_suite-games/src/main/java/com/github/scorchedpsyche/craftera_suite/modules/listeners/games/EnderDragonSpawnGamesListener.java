package com.github.scorchedpsyche.craftera_suite.modules.listeners.games;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.games.GamesCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.GamesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.GameModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnderDragonSpawnGamesListener implements Listener {
    public EnderDragonSpawnGamesListener(GamesManager gamesManager) {
        this.gamesManager = gamesManager;
    }

    GamesManager gamesManager;

    @EventHandler
    public void onEnderDragonSpawnGamesEvent(EntitySpawnEvent event) {
        if( event.getEntityType() == EntityType.ENDER_DRAGON )
        {
            EnderDragon enderDragon = (EnderDragon) event.getEntity();

            for(GameModel game : gamesManager.playerGames.values())
            {
                if( game.getType() == SuitePluginManager.Games.Type.Raid__EnderDragon
                        && game.getStage() == SuitePluginManager.Games.Stage.Running)
                {
                    AttributeInstance maxHealth = enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH);

                    if( maxHealth != null && enderDragon.getBossBar() != null)
                    {
                        ConsoleUtil.debugMessage(String.valueOf(enderDragon.getHealth()));
                        maxHealth.setBaseValue( maxHealth.getValue() * game.getParticipants().size() * 1.2
                        );
                        enderDragon.setHealth( enderDragon.getHealth() * game.getParticipants().size() * 1.2 );
                        ConsoleUtil.debugMessage(String.valueOf(enderDragon.getHealth()));
                        enderDragon.getBossBar().setTitle(game.getName());
                        enderDragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 99));
                    }
                }
            }
        }
    }
}
