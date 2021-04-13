package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.hud_settings.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.DatabaseUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;

import java.sql.*;

public class HudDatabaseAPI
{
    public HudPlayerPreferencesModel getPlayerPreferences(String playerUUID)
    {
        String sql = "SELECT * FROM " + DatabaseTables.Hud.player_preferences_TABLENAME +
                " WHERE player_uuid='" + playerUUID + "' LIMIT 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetNullOrEmpty(rs) )
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

            DatabaseManager.database.executeSql(sql);
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

            DatabaseManager.database.executeSql(sql);
        } else {
            ConsoleUtils.logError( SuitePluginManager.Hud.Name.full,
                    "Failed to update table on function 'setBooleanForPlayer'. Report this to the developer.");
        }
    }

    public boolean setupAndVerifySqlTable()
    {
        // Check if table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Hud.player_preferences_TABLENAME ) )
        {
            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSql(
                    "CREATE TABLE " + DatabaseTables.Hud.player_preferences_TABLENAME + "(\n"
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
                            + ");") )
            {
                // Successfully created table
                ConsoleUtils.logMessage(SuitePluginManager.Hud.Name.full,
                                        "Table successfully created: " + DatabaseTables.Hud.player_preferences_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtils.logError( SuitePluginManager.Hud.Name.full,
                                   "Failed to create table: " + DatabaseTables.Hud.player_preferences_TABLENAME);

            return false;
        }

        // If we got here table exists
        return true;
    }
}
