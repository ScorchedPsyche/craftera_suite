package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import org.bukkit.GameMode;

public class DatabaseTables
{
    private static final String corePrefix = "core_";
    private static final String hudPrefix = "hud_";
    private static final String seasonsPrefix = "seasons_";
    private static final String spectatorModePrefix = "spectator_mode_";

    public static class Achievements {
        public static final String achievements_TABLENAME = "achievements";

        public static class Table
        {
            public static final String player_uuid = "player_uuid";
            public static final String achievement = "achievement";
            public static final String date = "date";
            public static final String season = "season";
        }
    }

    public static class AFK {
        public static final String afk_TABLENAME = "afk";

        public static class Table
        {
            public static final String player_uuid = "player_uuid";
            public static final String afk_time_start = "afk_time_start";
            public static final String afk_time_end = "afk_time_end";
            public static final String afk_time_total = "afk_time_total";
            public static final String season = "season";
        }
    }

    public static class Core {
        public static final String settingsTableName = corePrefix + "settings";

        public static class SettingsTable
        {

        }
    }

    public static class Hud {
        public static final String player_preferences_TABLENAME = hudPrefix + "player_preferences";

        public static class PlayerPreferencesTable
        {
            public static final String player_uuid = "player_uuid";
            public static final String enabled = "enabled";
            public static final String display_mode = "display_mode";
            public static final String colorize_coordinates = "colorize_coordinates";
            public static final String colorize_nether_portal_coordinates = "colorize_nether_portal_coordinates";
            public static final String colorize_player_orientation = "colorize_player_orientation";
            public static final String colorize_server_tps = "colorize_server_tps";
            public static final String colorize_tool_durability = "colorize_tool_durability";
            public static final String colorize_world_time = "colorize_world_time";
            public static final String coordinates = "coordinates";
            public static final String nether_portal_coordinates = "nether_portal_coordinates";
            public static final String player_orientation = "player_orientation";
            public static final String plugin_commerce = "plugin_commerce";
            public static final String plugin_spectator = "plugin_spectator";
            public static final String server_time = "server_time";
            public static final String server_tps = "server_tps";
            public static final String tool_durability = "tool_durability";
            public static final String world_time = "world_time";

            public static class DisplayMode {
                public static final boolean compact = false;
                public static final boolean extended = true;
            }
        }
    }

    public static class Seasons {
        public static final String seasons_TABLENAME = "seasons";
        public static final String list_TABLENAME = seasonsPrefix + "list";
        public static final String status_TABLENAME = seasonsPrefix + "status";

        public static class Table
        {
            public static final String number = "number";
            public static final String title = "title";
            public static final String subtitle = "subtitle";
            public static final String status = "status";
            public static final String account = "account";
            public static final String date_start = "date_start";
            public static final String date_end = "date_end";
            public static final String minecraft_version_start = "minecraft_version_start";
            public static final String minecraft_version_end = "minecraft_version_end";

//            public static class List
//            {
//            }
//
//            public static class Status
//            {
//                public static final String description = "description";
//            }
        }

//        public enum Status {
//            Open,
//            Active,
//            Finished,
//            Archived
//        }
    }

    public static class SpectatorMode {
        public static final String player_data_TABLENAME = spectatorModePrefix + "player_data";

        public static class PlayerDataTable
        {
            public static final String player_uuid = "player_uuid";
            public static final String enabled = "enabled";
            public static final String game_mode = "game_mode";
            public static final String x = "x";
            public static final String y = "y";
            public static final String z = "z";
            public static final String world = "world";
            public static final String environment = "environment";
        }
    }
}
