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
            coordinates_nether_portal = rs.getBoolean(5);
            durability = rs.getBoolean(6);
            orientation = rs.getBoolean(7);
            time_server = rs.getBoolean(8);
            time_world = rs.getBoolean(9);
            time_world_work_hours = rs.getBoolean(10);
            plugin_spectator_range = rs.getBoolean(11);
            commerce = rs.getBoolean(12);

            return this;
        } catch (SQLException e)
        {
            consoleUtils.logError("Failed to load player preferences from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public boolean showCoordinates() { return coordinates; }

    private ConsoleUtils consoleUtils;

    private int id;
    private String player_uuid;
    private Boolean enabled;
    private Boolean coordinates;
    private Boolean coordinates_nether_portal;
    private Boolean durability;
    private Boolean orientation;
    private Boolean time_server;
    private Boolean time_world;
    private Boolean time_world_work_hours;
    private Boolean plugin_spectator_range;
    private Boolean commerce;
}