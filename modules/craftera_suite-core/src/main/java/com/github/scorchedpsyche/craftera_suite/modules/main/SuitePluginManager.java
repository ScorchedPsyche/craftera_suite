package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.Bukkit;

public class SuitePluginManager {
    public boolean isHudPluginEnabled ()
    {
        return Bukkit.getPluginManager().isPluginEnabled("craftera_suite-hud");
    }
}