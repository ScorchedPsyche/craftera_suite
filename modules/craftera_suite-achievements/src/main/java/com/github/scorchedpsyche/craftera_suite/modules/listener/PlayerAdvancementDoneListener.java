package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.main.AchievementsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementDoneListener implements Listener
{
    public PlayerAdvancementDoneListener(AchievementsDatabaseApi achievementsDatabaseApi) {
        this.achievementsDatabaseApi = achievementsDatabaseApi;
    }

    AchievementsDatabaseApi achievementsDatabaseApi;

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
    {
        String advNamespacedKey = event.getAdvancement().getKey().toString();

        // Check if it's not a recipe
        if( !advNamespacedKey.startsWith("minecraft:recipes/") )
        {
            achievementsDatabaseApi.addAchievementForPlayerIfNotExists(
                    new AchievementModel(
                            event.getPlayer().getUniqueId().toString(),
                            advNamespacedKey,
                            DateUtil.Time.getUnixNow()
                    ));

//            for ( String criteria : event.getAdvancement().getCriteria() )
//            {
//                if( criteria.substring(0, 3).equals("has") )
//                {
//                    System.out.println(event.getAdvancement().getKey().toString());
//                    System.out.println(criteria);
//                } else {
//                    ConsoleUtils.logSuccess(event.getAdvancement().getKey().toString());
//                    ConsoleUtils.logSuccess(criteria);
//                }
//            }
        }
    }
}
