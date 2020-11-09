package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.Bukkit;

public class SuitePluginManager {
    public static class Hud
    {
        public static final String name = "CraftEra Suite – HUD";
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled("craftera_suite-hud");
        }
    }
}