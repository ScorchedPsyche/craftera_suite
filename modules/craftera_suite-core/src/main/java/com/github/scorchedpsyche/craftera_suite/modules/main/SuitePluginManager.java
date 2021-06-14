package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.Bukkit;

public class SuitePluginManager {
    private static final String permission_prefix = "craftera_suite";

    public static class Achievements
    {
        public static class Name
        {
            public static final String compact = "CES - Achievements";
            public static final String full = "CraftEra Suite - Achievements";
            public static final String pomXml = "craftera_suite-achievements";
        }

        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }
    }

    public static class BabyEntities
    {
        public static class Name
        {
            public static final String compact = "CES - Baby Entities";
            public static final String full = "CraftEra Suite - Baby Entities";
            public static final String pomXml = "craftera_suite-baby_entities";
        }

        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }
    }

    public static class Core
    {
        public static class Name
        {
            public static final String compact = "CES - Core";
            public static final String full = "CraftEra Suite - Core";
            public static final String pomXml = "craftera_suite-core";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }

        public static class Task {
            public static class TitleAndSubtitleSendToPlayer {
                public static final long period = 5L;
            }
        }
    }

    public static class Hud
    {
        public static class Name
        {
            public static final String compact = "CES - HUD";
            public static final String full = "CraftEra Suite - HUD";
            public static final String pomXml = "craftera_suite-hud";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }
    }

    public static class Seasons
    {
        public static class Name
        {
            public static final String compact = "CES - Seasons";
            public static final String full = "CraftEra Suite - Seasons";
            public static final String pomXml = "craftera_suite-seasons";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }

        public static enum Status
        {
            Active,
            Archived,
            Inactive,
            Started,
            Finished
//            public static final String open = "open";
//            public static final String active = "active";
//            public static final String finished = "finished";
//            public static final String archived = "archived";
        }

        public static class Permissions {
            public static final String seasons = permission_prefix + ".seasons";
        }
    }

    public static class SpectatorMode
    {
        public static class Name
        {
            public static final String compact = "CES - Spectator Mode";
            public static final String full = "CraftEra Suite - Spectator Mode";
            public static final String pomXml = "craftera_suite-spectator_mode";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }

        public static class Task
        {
            public static class ProcessPlayersInSpectator
            {
                public static final long period = 5L;
            }
        }
    }

    public static class WanderingTrades
    {
        public static class Name
        {
            public static final String compact = "CES - Wandering Trades";
            public static final String full = "CraftEra Suite - Wandering Trades";
            public static final String pomXml = "craftera_suite-wandering_trades";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }
    }
}