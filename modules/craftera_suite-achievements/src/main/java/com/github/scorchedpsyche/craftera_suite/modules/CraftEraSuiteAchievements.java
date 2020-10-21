package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerAdvancementDoneListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftEraSuiteAchievements extends JavaPlugin
{
    @Override
    public void onEnable()
    {

        getServer().getPluginManager().registerEvents(new PlayerAdvancementDoneListener(), this);
    }

    @Override
    public void onDisable()
    {

    }
}
