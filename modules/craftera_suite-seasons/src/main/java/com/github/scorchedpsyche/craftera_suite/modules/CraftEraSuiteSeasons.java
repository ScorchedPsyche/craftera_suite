package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.listeners.SeasonsCommandListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteSeasons extends JavaPlugin
{
    @Override
    public void onEnable()
    {

        getServer().getPluginManager().registerEvents(new SeasonsCommandListener(), this);
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }
}
