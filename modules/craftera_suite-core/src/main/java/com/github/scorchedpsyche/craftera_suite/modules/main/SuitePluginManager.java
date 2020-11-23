package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.Bukkit;

public class SuitePluginManager {
    public static class BabyEntities
    {
        public static class Name
        {
            public static final String compact = "CES - Baby Entities";
            public static final String full = "CraftEra Suite - Baby Entities";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled("craftera_suite-baby_entities");
        }
    }

    public static class Hud
    {
        public static class Name
        {
            public static final String compact = "CES - HUD";
            public static final String full = "CraftEra Suite - HUD";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled("craftera_suite-hud");
        }
    }

    public static class WanderingTrades
    {
        public static class Name
        {
            public static final String compact = "CES - Wandering Trades";
            public static final String full = "CraftEra Suite - Wandering Trades";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled("craftera_suite-wandering_trades");
        }
    }
}