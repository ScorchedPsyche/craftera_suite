package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.Bukkit;

public class SuitePluginManager {
    private static final String permission_prefix = "craftera_suite";
    public static class Name
    {
        public static final String compact = "CES";
        public static final String full = "CraftEra Suite";
        public static final String pomXml = "craftera_suite";
    }

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

    public static class AFK
    {
        public static class Name
        {
            public static final String compact = "CES - AFK";
            public static final String full = "CraftEra Suite - AFK";
            public static final String pomXml = "craftera_suite-afk";
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

        public static class Messages {
            public enum Type
            {
                ServerMessageToAllPlayers,
                MessageForSpecificPlayer
            }
        }

        public static class Task {
            public static class TitleAndSubtitleSendToPlayer {
                public static final long period = 5L;
            }
        }

        public static class Permissions {
            public static final String core = permission_prefix + ".core";
        }
    }

    public static class Games
    {
        public static class Name
        {
            public static final String compact = "CES - Games";
            public static final String full = "CraftEra Suite - Games";
            public static final String pomXml = "craftera_suite-games";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }

        public enum Type
        {
            Raid__EnderDragon,
            Raid__EnderDragon_Chaotic
        }

        public enum Stage
        {
            Preparing,
            Subscribing,
            Running,
            Paused,
            Finished,
            Archived
        }

        public static class Permissions {
            public static final String games = permission_prefix + ".games";
            public static final String prepare = games + ".prepare";
            public static final String cancel = games + ".cancel";
            public static final String open_subscriptions = games + ".open_subscriptions";
            public static final String announce = games + ".announce";
            public static final String join = games + ".join";
            public static final String start = games + ".start";
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


    public static class Rewards
    {
        public static class Name
        {
            public static final String compact = "CES - Rewards";
            public static final String full = "CraftEra Suite - Rewards";
            public static final String pomXml = "craftera_suite-rewards";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
        }

        public enum Type
        {
            Points,
            Effect, // Buff/debuff
            Entity,
            Experience,
            Item
        }

        public enum Source
        {
            Achievement,
            Event,
            PlayTime,
            Season
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

        public enum Status
        {
            Inactive,
            Active,
            Started,
            Finished,
            Archived
        }

        public static class Permissions {
            public static final String seasons = permission_prefix + ".seasons";
        }
    }

    public static class Sleep
    {
        public static class Name
        {
            public static final String compact = "CES - Sleep";
            public static final String full = "CraftEra Suite - Sleep";
            public static final String pomXml = "craftera_suite-sleep";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
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

    public static class Statistics
    {
        public static class Name
        {
            public static final String compact = "CES - Statistics";
            public static final String full = "CraftEra Suite - Statistics";
            public static final String pomXml = "craftera_suite-statistics";
        }
        public static boolean isEnabled ()
        {
            return Bukkit.getPluginManager().isPluginEnabled(Name.pomXml);
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