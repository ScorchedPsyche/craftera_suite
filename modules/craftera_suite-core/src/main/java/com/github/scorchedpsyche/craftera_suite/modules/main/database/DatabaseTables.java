package com.github.scorchedpsyche.craftera_suite.modules.main.database;

public class DatabaseTables
{
    private static final String hudPrefix = "hud_";

    public static class Hud {
        public static final String player_preferences = hudPrefix + "player_preferences";

        public static class PlayerPreferences {
            public static String player_uuid = "player_uuid";
            public static String enabled = "enabled";
            public static String display_mode = "display_mode";
            public static String colorize_coordinates = "colorize_coordinates";
            public static String colorize_nether_portal_coordinates = "colorize_nether_portal_coordinates";
            public static String colorize_server_tps = "colorize_server_tps";
            public static String colorize_tool_durability = "colorize_tool_durability";
            public static String colorize_world_time = "colorize_world_time";
            public static String coordinates = "coordinates";
            public static String nether_portal_coordinates = "nether_portal_coordinates";
            public static String player_orientation = "player_orientation";
            public static String plugin_commerce = "plugin_commerce";
            public static String plugin_spectator = "plugin_spectator";
            public static String server_time = "server_time";
            public static String server_tps = "server_tps";
            public static String tool_durability = "tool_durability";
            public static String world_time = "world_time";

            public static class DisplayMode {
                public static short compact = 0;
                public static short expanded = 1;
            }
        }
    }
}
