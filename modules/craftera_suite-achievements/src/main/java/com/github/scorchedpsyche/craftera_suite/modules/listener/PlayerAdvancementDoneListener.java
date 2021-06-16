package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.main.AchievementManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.AchievementsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementDbModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementDoneListener implements Listener
{
    public PlayerAdvancementDoneListener(AchievementManager achievementManager) {
        this.achievementManager = achievementManager;
    }

    AchievementManager achievementManager;

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
    {
        String advNamespacedKey = event.getAdvancement().getKey().toString();

        if( achievementManager.achievements.containsKey(advNamespacedKey) )
        {
            achievementManager.addAdvancementForPlayer(event.getPlayer().getUniqueId(), advNamespacedKey);
        }
    }
}
