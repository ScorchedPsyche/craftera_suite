package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import org.bukkit.Bukkit;

public class SuitePluginManager {
    /*public static String getPluginNameFromPomXmlName(String pomXmlName, Boolean fullNameInsteadOfCompact)
    {
        ConsoleUtils.logError(pomXmlName);
        switch( pomXmlName )
        {
            case BabyEntities.Name.pomXml:
                ConsoleUtils.logError("1");
                if( fullNameInsteadOfCompact )
                {
                    return BabyEntities.Name.full;
                }
                return BabyEntities.Name.compact;

            case Hud.Name.pomXml:
                ConsoleUtils.logError("2");
                if( fullNameInsteadOfCompact )
                {
                    return Hud.Name.full;
                }
                return Hud.Name.compact;

            case Seasons.Name.pomXml:
                ConsoleUtils.logError("3");
                if( fullNameInsteadOfCompact )
                {
                    return Seasons.Name.full;
                }
                return Seasons.Name.compact;

            case WanderingTrades.Name.pomXml:
                ConsoleUtils.logError("4");
                if( fullNameInsteadOfCompact )
                {
                    return WanderingTrades.Name.full;
                }
                return WanderingTrades.Name.compact;
        }

        return "CraftEra Suite";
    }
    public static String getPluginNameFromPomXmlName(String pomXmlName)
    {
        return getPluginNameFromPomXmlName(pomXmlName, true);
    }*/

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