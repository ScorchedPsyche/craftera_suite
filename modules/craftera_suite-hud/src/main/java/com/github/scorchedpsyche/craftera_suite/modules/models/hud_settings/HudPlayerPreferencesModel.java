package com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HudPlayerPreferencesModel
{
    public HudPlayerPreferencesModel loadPreferencesFromResultSet(ResultSet rs)
    {
        try {
            id = rs.getInt(1);
            player_uuid = rs.getString(2);
            enabled = rs.getBoolean(3);
            display_mode = rs.getBoolean(4);
            colorize_coordinates = rs.getBoolean(5);
            colorize_nether_portal_coordinates = rs.getBoolean(6);
            colorize_player_orientation = rs.getBoolean(7);
            colorize_server_tps = rs.getBoolean(8);
            colorize_tool_durability = rs.getBoolean(9);
            colorize_world_time = rs.getBoolean(10);
            coordinates = rs.getBoolean(11);
            nether_portal_coordinates = rs.getBoolean(12);
            player_orientation = rs.getBoolean(13);
            plugin_commerce = rs.getBoolean(14);
            plugin_spectator = rs.getBoolean(15);
            server_time = rs.getBoolean(16);
            server_tps = rs.getBoolean(17);
            tool_durability = rs.getBoolean(18);
            world_time = rs.getBoolean(19);

            return this;
        } catch (SQLException e)
        {
            consoleUtils.logError(
                    SuitePluginManager.Hud.name,
                    "Failed to load player preferences from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public boolean isHudEnabled() { return enabled; }
    public boolean isDisplayModeExtended() { return display_mode; }
    public boolean colorizeCoordinates() { return colorize_coordinates; }
    public boolean colorizeNetherPortalCoordinates() { return colorize_nether_portal_coordinates; }
    public boolean colorizePlayerOrientation() { return colorize_player_orientation; }
    public boolean colorizeServerTps() { return colorize_server_tps; }
    public boolean colorizeToolDurability() { return colorize_tool_durability; }
    public boolean colorizeWorldTime() { return colorize_world_time; }
    public boolean showCoordinates() { return coordinates; }
    public boolean showNetherPortalCoordinates() { return nether_portal_coordinates; }
    public boolean showOrientation() { return player_orientation; }
    public boolean showServerTime() { return server_time; }
    public boolean showServerTPS() { return server_tps; }
    public boolean showToolDurability() { return tool_durability; }
    public boolean showWorldTime() { return world_time; }
    public void setPreference(String preference, boolean value)
    {
        switch(preference)
        {
            case DatabaseTables.Hud.PlayerPreferencesTable.display_mode:
                display_mode = value;
                break;
        }
    }

    public void togglePreference(String preference)
    {
        switch(preference)
        {
            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_coordinates:
                colorize_coordinates = !colorize_coordinates;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_nether_portal_coordinates:
                colorize_nether_portal_coordinates = !colorize_nether_portal_coordinates;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_player_orientation:
                colorize_player_orientation = !colorize_player_orientation;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_server_tps:
                colorize_server_tps = !colorize_server_tps;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_tool_durability:
                colorize_tool_durability = !colorize_tool_durability;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_world_time:
                colorize_world_time = !colorize_world_time;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.coordinates:
                coordinates = !coordinates;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.nether_portal_coordinates:
                nether_portal_coordinates = !nether_portal_coordinates;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.player_orientation:
                player_orientation = !player_orientation;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.plugin_commerce:
                plugin_commerce = !plugin_commerce;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.plugin_spectator:
                plugin_spectator = !plugin_spectator;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.server_time:
                server_time = !server_time;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.server_tps:
                server_tps = !server_tps;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.tool_durability:
                tool_durability = !tool_durability;
                break;

            case DatabaseTables.Hud.PlayerPreferencesTable.world_time:
                world_time = !world_time;
                break;
        }
    }

    private ConsoleUtils consoleUtils;

    private int id;
    private String player_uuid;
    private Boolean enabled;
    private Boolean display_mode;
    private Boolean colorize_coordinates;
    private Boolean colorize_nether_portal_coordinates;
    private Boolean colorize_player_orientation;
    private Boolean colorize_server_tps;
    private Boolean colorize_tool_durability;
    private Boolean colorize_world_time;
    private Boolean coordinates;
    private Boolean nether_portal_coordinates;
    private Boolean player_orientation;
    private Boolean plugin_commerce;
    private Boolean plugin_spectator;
    private Boolean server_time;
    private Boolean server_tps;
    private Boolean tool_durability;
    private Boolean world_time;
}