package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerAdvancementDoneListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.AchievementsDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import net.minecraft.server.v1_16_R3.Advancement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftEraSuiteAchievements extends JavaPlugin
{
    private AchievementsDatabaseApi achievementsDatabaseApi;

    @Override
    public void onEnable()
    {
        // Check if Core dependency was loaded
        if( SuitePluginManager.Core.isEnabled() )
        {
            // Attempt to setup the rest of the plugin
            achievementsDatabaseApi = new AchievementsDatabaseApi();

            // Setup and verify DB tables
            if( achievementsDatabaseApi.setupAndVerifySqlTable() )
            {
                getServer().getPluginManager().registerEvents(new PlayerAdvancementDoneListener(achievementsDatabaseApi), this);
            } else {
                // Failed to create database tables! Display error and disable plugin
                ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            // Core dependency missing! Display error and disable plugin
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getDescription().getPrefix() + "] ERROR: CraftEra Suite Core MISSING! Download the dependency and RELOAD/RESTART the server.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

//        Advancement ironOre = new Advancement(
//                new MinecraftKey("ces", "advancements.ores.iron"),
//                null,
//                new AdvancementDisplay(
//                        CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_ORE)),
//
//                        ),
//
//        )
    }

    @Override
    public void onDisable()
    {
        achievementsDatabaseApi = null;

        super.onDisable();
    }
}
