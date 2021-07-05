package com.github.scorchedpsyche.craftera_suite.modules.listeners.games;

import com.github.scorchedpsyche.craftera_suite.modules.main.GamesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.GameModel;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EnderDragonDeathGamesListener implements Listener {
    public EnderDragonDeathGamesListener(GamesManager gamesManager) {
        this.gamesManager = gamesManager;
    }

    GamesManager gamesManager;

    @EventHandler
    public void onEnderDragonDeathGamesEvent(EntityDeathEvent event) {
        if( event.getEntityType() == EntityType.ENDER_DRAGON )
        {
            for(GameModel game : gamesManager.playerGames.values())
            {
                if( game.getType() == SuitePluginManager.Games.Type.Raid__EnderDragon
                    && game.getStage() == SuitePluginManager.Games.Stage.Running)
                {
                    game.finish();
                    gamesManager.playerGames.remove(game.owner.getUniqueId().toString());
                }
            }
        }
    }
}
