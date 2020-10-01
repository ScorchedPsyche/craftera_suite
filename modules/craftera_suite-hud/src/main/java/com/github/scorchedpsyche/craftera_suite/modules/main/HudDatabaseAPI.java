package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.DatabaseUtils;

import java.sql.*;

public class HudDatabaseAPI
{
    public HudDatabaseAPI(IDatabase database)
    {
        this.database = database;
        consoleUtils = new ConsoleUtils("CraftEra Suite - HUD");
        databaseUtils = new DatabaseUtils();
        setup();
    }

    private final String tablePrefix = "hud_";
    private IDatabase database;
    private ConsoleUtils consoleUtils;
    private DatabaseUtils databaseUtils;

    public HudPlayerPreferencesModel getPlayerPreferences(String playerUUID)
    {
        String sql = "SELECT * FROM " + tablePrefix + "player_preferences " +
                "WHERE player_uuid='" + playerUUID + "' LIMIT 1";

        try (Connection conn = DriverManager.getConnection(database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !databaseUtils.isResultSetEmpty(rs) )
            {
                return new HudPlayerPreferencesModel().loadPreferencesFromResultSet(rs);
            }
        } catch (SQLException e) {
            consoleUtils.logError(
                    "SQLite query failed: " + sql);
            consoleUtils.logError( e.getMessage() );
        }

        return null;
    }

    public void disableHudForPlayer(String playerUUID)
    {
        String sql = "INSERT INTO " + tablePrefix + "player_preferences (player_uuid, enabled)\n" +
                "  VALUES('" + playerUUID + "', 0) \n" +
                "  ON CONFLICT(player_uuid) \n" +
                "  DO UPDATE SET enabled=0;";
        database.executeSql(sql);
    }

    public void enableHudForPlayer(String playerUUID)
    {
        String sql = "INSERT INTO " + tablePrefix + "player_preferences (player_uuid, enabled)\n" +
                "  VALUES('" + playerUUID + "', 1) \n" +
                "  ON CONFLICT(player_uuid) \n" +
                "  DO UPDATE SET enabled=1;";
        database.executeSql(sql);
    }

    private void setup()
    {
        String playerPreferencesTableSql = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "player_preferences (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	player_uuid TEXT UNIQUE NOT NULL,\n"
                + "	enabled NUMERIC DEFAULT 0,\n"
                + "	coordinates NUMERIC DEFAULT 1,\n"
                + "	coordinates_nether_portal NUMERIC DEFAULT 0,\n"
                + "	durability NUMERIC DEFAULT 1,\n"
                + "	orientation NUMERIC DEFAULT 1,\n"
                + "	time_server NUMERIC DEFAULT 0,\n"
                + "	time_world NUMERIC DEFAULT 1,\n"
                + "	time_world_work_hours NUMERIC DEFAULT 1,\n"
                + "	plugin_spectator_range NUMERIC DEFAULT 1,\n"
                + "	commerce NUMERIC DEFAULT 1\n"
                + ");";

        if (database.executeSql(playerPreferencesTableSql))
        {
            consoleUtils.logSuccess("Table successfully created: hud_player_preferences");
        } else {
            consoleUtils.logError("Failed to create table: hud_player_preferences");
        }
    }
}
