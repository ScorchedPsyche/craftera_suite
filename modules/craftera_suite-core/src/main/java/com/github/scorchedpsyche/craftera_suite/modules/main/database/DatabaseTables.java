package com.github.scorchedpsyche.craftera_suite.modules.main.database;

public class DatabaseTables
{
    private static final String corePrefix = "core_";
    private static final String hudPrefix = "hud_";

    public static class Core {
        public static final String seasons = corePrefix + "seasons";
        public static final String settings = corePrefix + "settings";

        public static class Settings
        {

        }

        public static class Seasons
        {
            public static final String number = "number";
            public static final String title = "title";
            public static final String subtitle = "subtitle";
            public static final String status = "status";


            public enum Status {
                Open,
                Active,
                Finished,
                Archived
            }
        }
    }

    public static class Hud {
        public static final String player_preferences = hudPrefix + "player_preferences";

        public static class PlayerPreferences {
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
}
