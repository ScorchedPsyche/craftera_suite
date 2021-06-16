package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementDbModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AchievementManager
{
    private AchievementsDatabaseApi achievementsDatabaseApi;
    private File pluginRootFolder;
    public HashMap<String, AchievementModel> achievements;

    public AchievementManager(AchievementsDatabaseApi achievementsDatabaseApi, File pluginRootFolder)
    {
        this.achievementsDatabaseApi = achievementsDatabaseApi;
        this.pluginRootFolder = pluginRootFolder;
    }

    public boolean setupManager()
    {
        File file = new File(pluginRootFolder, "advancements/vanilla/1.17/en-US.json");
        try {
            String jsonObj = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            HashMap<String, Object> vanillaAdvancements = new Gson().fromJson(jsonObj, HashMap.class);
            achievements = new HashMap<>();

            // Loop through achievement entries
            for ( Map.Entry<String, Object> entry : vanillaAdvancements.entrySet() )
            {
                AchievementModel achievementModel = new AchievementModel();
                achievementModel.path = "minecraft:";

                // Verify if current entry is a title or description
                if( entry.getKey().endsWith(".title") )
                {
                    // Title
                    achievementModel.path += entry.getKey().substring(0, entry.getKey().indexOf(".title"));
                    achievementModel.title = entry.getValue().toString();
                } else {
                    // Description
                    achievementModel.path += entry.getKey().substring(0, entry.getKey().indexOf(".description"));
                    achievementModel.description = entry.getValue().toString();
                }

                achievementModel.path = achievementModel.path.replace("advancements.", "");
                achievementModel.path = achievementModel.path.replaceAll("\\.", "/");

                // Check if already added
                if( achievements.containsKey(achievementModel.path) )
                {
                    // Already on map, then we're just missing either title or description
                    if( achievementModel.title != null )
                    {
                        // Update title
                        achievements.get(achievementModel.path).title = achievementModel.title;
                    } else {
                        // Update description
                        achievements.get(achievementModel.path).description = achievementModel.description;
                    }
                } else {
                    // Not yet added to map
                    achievementModel.can_be_server_or_season_first =
                            achievementsDatabaseApi.canAchievementBeServerOrSeasonFirst(achievementModel.path);
                    achievements.put(achievementModel.path, achievementModel);
                }
            }
        } catch ( IOException e )
        {
            ConsoleUtil.logError(SuitePluginManager.Achievements.Name.full,
                    "Failed to read file: " + file.getAbsolutePath());

            return false;
        }

        return true;
    }

    public void addAdvancementForPlayer(UUID playerUUID, String advancementPath)
    {
        if( achievements.containsKey(advancementPath) )
        {
            achievementsDatabaseApi.addAchievementForPlayerIfNotExists(
                    playerUUID,
                    achievements.get(advancementPath)
            );
        } else {
            ConsoleUtil.logError(SuitePluginManager.Achievements.Name.full,
                    "Something very wrong happened on 'addAdvancementForPlayer'. Contact developer");
        }
    }
}
