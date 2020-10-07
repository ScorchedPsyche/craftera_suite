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
            coordinates = rs.getBoolean(4);
            nether_portal_coordinates = rs.getBoolean(5);
            player_orientation = rs.getBoolean(6);
            plugin_commerce = rs.getBoolean(7);
            plugin_spectator = rs.getBoolean(8);
            server_time = rs.getBoolean(9);
            server_tps = rs.getBoolean(10);
            tool_durability = rs.getBoolean(11);
            world_time = rs.getBoolean(12);
            world_time_with_work_hours = rs.getBoolean(13);

            return this;
        } catch (SQLException e)
        {
            consoleUtils.logError("Failed to load player preferences from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public boolean showCoordinates() { return coordinates; }
    public boolean showNetherPortalCoordinates() { return nether_portal_coordinates; }
    public boolean showOrientation() { return player_orientation; }
    public boolean showServerTime() { return server_time; }
    public boolean showServerTPS() { return server_tps; }
    public boolean showToolDurability() { return tool_durability; }
    public boolean showWorldTime() { return world_time; }
    public boolean showWorldTimeColorized() { return world_time_with_work_hours; }

    private ConsoleUtils consoleUtils;

    private int id;
    private String player_uuid;
    private Boolean enabled;
    private Boolean coordinates;
    private Boolean nether_portal_coordinates;
    private Boolean player_orientation;
    private Boolean plugin_commerce;
    private Boolean plugin_spectator;
    private Boolean server_time;
    private Boolean server_tps;
    private Boolean tool_durability;
    private Boolean world_time;
    private Boolean world_time_with_work_hours;
}