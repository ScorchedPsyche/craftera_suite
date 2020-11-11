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
        setup();
    }

    private IDatabase database;

    public HudPlayerPreferencesModel getPlayerPreferences(String playerUUID)
    {
        String sql = "SELECT * FROM " + DatabaseTables.Hud.player_preferences_TABLENAME +
                " WHERE player_uuid='" + playerUUID + "' LIMIT 1";

        try (Connection conn = DriverManager.getConnection(database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetEmpty(rs) )
            {
                return new HudPlayerPreferencesModel().loadPreferencesFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }

    public void toggleBooleanForPlayer(String table, String playerUUID, String column)
    {
        if( !StringUtils.isNullOrEmpty(table) && column != null && !StringUtils.isNullOrEmpty(column) )
        {
            String sql = "INSERT INTO " + table +
                    " (" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + ", " + column + ") \n" +
                        "VALUES('" + playerUUID + "', 1) \n" +
                        "ON CONFLICT(" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + ") DO \n" +
                        "UPDATE SET " + column + " = CASE WHEN " + column + " = 1 THEN 0 ELSE 1 END";

            database.executeSql(sql);
        } else {
            ConsoleUtils.logError( SuitePluginManager.Hud.Name.full,
                    "Failed to update table on function 'toggleBooleanForPlayer'. Report this to the developer.");
        }
    }

    public void setBooleanForPlayer(String table, String playerUUID, String column, boolean value)
    {
        if( !StringUtils.isNullOrEmpty(table) && column != null && !StringUtils.isNullOrEmpty(column) )
        {
            String sql = "INSERT INTO " + table +
                    " (" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + ", " + column + ") \n" +
                    "VALUES('" + playerUUID + "', " + value +") \n" +
                    "ON CONFLICT(" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + ") DO \n" +
                    "UPDATE SET " + column + " = " + value;

            database.executeSql(sql);
        } else {
            ConsoleUtils.logError( SuitePluginManager.Hud.Name.full,
                    "Failed to update table on function 'setBooleanForPlayer'. Report this to the developer.");
        }
    }

    private void setup()
    {
        String playerPreferencesTableSql = "CREATE TABLE IF NOT EXISTS " + DatabaseTables.Hud.player_preferences_TABLENAME + "(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + " TEXT UNIQUE NOT NULL,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.enabled + " NUMERIC DEFAULT 0,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.display_mode + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.colorize_coordinates + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.colorize_nether_portal_coordinates + " NUMERIC DEFAULT " +
                "1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.colorize_player_orientation + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.colorize_server_tps + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.colorize_tool_durability + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.colorize_world_time + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.coordinates + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.nether_portal_coordinates + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.player_orientation + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.plugin_commerce + " NUMERIC DEFAULT 0,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.plugin_spectator + " NUMERIC DEFAULT 0,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.server_time + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.server_tps + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.tool_durability + " NUMERIC DEFAULT 1,\n"
                + "	" + DatabaseTables.Hud.PlayerPreferencesTable.world_time + " NUMERIC DEFAULT 1\n"
                + ");";

        if (database.executeSql(playerPreferencesTableSql))
        {
            ConsoleUtils.logSuccess( SuitePluginManager.Hud.Name.full,
                                     "Table successfully created: hud_player_preferences");
        } else {
            ConsoleUtils.logError( SuitePluginManager.Hud.Name.full,
                                   "Failed to create table: hud_player_preferences");
        }
    }
}
