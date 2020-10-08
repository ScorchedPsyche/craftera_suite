package com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HudPlayerPreferencesModel
{
    public HudPlayerPreferencesModel()
    {
        consoleUtils = new ConsoleUtils("CraftEra Suite - HUD");
    }

    public HudPlayerPreferencesModel loadPreferencesFromResultSet(ResultSet rs)
    {
        try {
            id = rs.getInt(1);
            player_uuid = rs.getString(2);
            enabled = rs.getBoolean(3);
            display_mode = rs.getBoolean(4);
            colorize_coordinates = rs.getBoolean(5);
            colorize_nether_portal_coordinates = rs.getBoolean(6);
            colorize_server_tps = rs.getBoolean(7);
            colorize_tool_durability = rs.getBoolean(8);
            colorize_world_time = rs.getBoolean(9);
            coordinates = rs.getBoolean(10);
            nether_portal_coordinates = rs.getBoolean(11);
            player_orientation = rs.getBoolean(12);
            plugin_commerce = rs.getBoolean(13);
            plugin_spectator = rs.getBoolean(14);
            server_time = rs.getBoolean(15);
            server_tps = rs.getBoolean(16);
            tool_durability = rs.getBoolean(17);
            world_time = rs.getBoolean(18);

            return this;
        } catch (SQLException e)
        {
            consoleUtils.logError("Failed to load player preferences from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public boolean isDisplayModeExpanded() { return display_mode; }
    public boolean colorizeCoordinates() { return colorize_coordinates; }
    public boolean colorizeNetherPortalCoordinates() { return colorize_nether_portal_coordinates; }
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

    private ConsoleUtils consoleUtils;

    private int id;
    private String player_uuid;
    private Boolean enabled;
    private Boolean display_mode;
    private Boolean colorize_coordinates;
    private Boolean colorize_nether_portal_coordinates;
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