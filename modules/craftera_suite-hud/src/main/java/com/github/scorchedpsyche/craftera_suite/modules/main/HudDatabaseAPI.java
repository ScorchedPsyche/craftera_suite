package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.DatabaseUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;

import java.sql.*;

public class HudDatabaseAPI
{

    public HudDatabaseAPI(IDatabase database)
    {
        this.database = database;
        consoleUtils = new ConsoleUtils("CraftEra Suite - HUD");
        stringUtils = new StringUtils();
        databaseUtils = new DatabaseUtils();
        setup();
    }

    private final String tablePrefix = "hud_";
    private IDatabase database;
    private ConsoleUtils consoleUtils;
    private StringUtils stringUtils;
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

    public void toggleBoolean(String table, String[] columns)
    {
//        if( !stringUtils.isEmpty(table) && columns != null && !stringUtils.isEmpty(columns[0]) )
//        {
//            String sql =
//        } else {
//            consoleUtils.logError("Failed to update table: " + table);
//        }
    }

    public void toggleBooleanForPlayer(String table, String playerUUID, String column)
    {
        if( !stringUtils.isEmpty(table) && column != null && !stringUtils.isEmpty(column) )
        {
            String sql = "INSERT INTO " + table +
                    " (" + DatabaseTables.Hud.PlayerPreferences.player_uuid + ", " + column + ") \n" +
                        "VALUES('" + playerUUID + "', 1) \n" +
                        "ON CONFLICT(" + DatabaseTables.Hud.PlayerPreferences.player_uuid + ") DO \n" +
                        "UPDATE SET " + column + " = CASE WHEN " + column + " = 1 THEN 0 ELSE 1 END";

            database.executeSql(sql);
        } else {
            consoleUtils.logError(
                    "Failed to update table on function 'toggleBooleanForPlayer'. Report this to the developer.");
        }
    }

    private void setup()
    {
        String playerPreferencesTableSql = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "player_preferences (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.player_uuid + " TEXT UNIQUE NOT NULL,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.enabled + " NUMERIC DEFAULT 0,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.coordinates + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.nether_portal_coordinates + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.player_orientation + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.plugin_commerce + " NUMERIC DEFAULT 0,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.plugin_spectator + " NUMERIC DEFAULT 0,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.server_time + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.server_tps + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.tool_durability + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.world_time + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferences.world_time_colorized + " NUMERIC DEFAULT 1\n"
                + ");";

        if (database.executeSql(playerPreferencesTableSql))
        {
            consoleUtils.logSuccess("Table successfully created: hud_player_preferences");
        } else {
            consoleUtils.logError("Failed to create table: hud_player_preferences");
        }
    }
}
