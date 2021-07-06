package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.HudPlayerPreferencesModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;

import java.sql.*;

public class HudDatabaseAPI
{
    /**
     * Sets up and verify SQL tables.
     * @return True if successful
     */
    public boolean setupAndVerifySqlTable()
    {
        // Check if table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Hud.player_preferences_TABLENAME ) )
        {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder
                .append("CREATE TABLE ").append(DatabaseTables.Hud.player_preferences_TABLENAME).append("(\n")
                .append(" id integer PRIMARY KEY AUTOINCREMENT,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.player_uuid).append(" TEXT UNIQUE NOT NULL,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.enabled).append(" NUMERIC DEFAULT 0,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.display_mode).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.colorize_coordinates).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.colorize_nether_portal_coordinates).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.colorize_player_orientation).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.colorize_tool_durability).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.colorize_world_time).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.format_world_time).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.coordinates).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.nether_portal_coordinates).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.player_orientation).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.plugin_commerce).append(" NUMERIC DEFAULT 0,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.plugin_spectator).append(" NUMERIC DEFAULT 0,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.server_time).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.tool_durability).append(" NUMERIC DEFAULT 1,\n ")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.world_time).append(" NUMERIC DEFAULT 1\n")
                .append(");");

            if ( DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded( sqlBuilder.toString() ) )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Hud.Name.full,
                        "Table successfully created: " + DatabaseTables.Hud.player_preferences_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            return false;
        }

        // If we got here table exists
        return true;
    }

    /**
     * Gets all player preferences.
     * @param playerUUID The player's UUID to get the preferences for
     * @return The preferences if successful, null otherwise
     */
    public HudPlayerPreferencesModel getPlayerPreferences(String playerUUID)
    {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ").append(DatabaseTables.Hud.player_preferences_TABLENAME)
            .append(" WHERE player_uuid='").append(playerUUID).append("' LIMIT 1");

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlBuilder.toString());

            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                return new HudPlayerPreferencesModel().loadPreferencesFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtil.logErrorSQLWithPluginPrefix(SuitePluginManager.Hud.Name.full, "getPlayerPreferences",
                sqlBuilder.toString(), e.getMessage());
        }

        return null;
    }

    /**
     * Toggles the preference for the player. In other words, inverts it's value.
     * @param table The table where the preference is located at
     * @param playerUUID The player's UUID to toggle the preference for
     * @param column The column in the table where the preference inversion should happen
     */
    public void toggleBooleanForPlayer(String table, String playerUUID, String column)
    {
        if( !StringUtil.isNullOrEmpty(table) && column != null && !StringUtil.isNullOrEmpty(column) )
        {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO ").append(table).append(" (")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.player_uuid).append(", ").append(column).append(") \n")
                .append("VALUES('").append(playerUUID).append("', 1) \n")
                .append("ON CONFLICT(").append(DatabaseTables.Hud.PlayerPreferencesTable.player_uuid).append(") DO \n")
                .append("UPDATE SET ").append(column).append(" = CASE WHEN ").append(column).append(" = 1 THEN 0 ELSE 1 END");

            DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(sqlBuilder.toString());
        }
    }

    /**
     * Sets a specific value for the player's preference.
     * @param table The table where the preference is located at
     * @param playerUUID The player's UUID to toggle the preference for
     * @param column The column in the table where the preference should be set
     * @param value The specific value to set for the preference
     */
    public void setBooleanForPlayer(String table, String playerUUID, String column, boolean value)
    {
        if( !StringUtil.isNullOrEmpty(table) && column != null && !StringUtil.isNullOrEmpty(column) )
        {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO ").append(table).append(" (")
                .append(DatabaseTables.Hud.PlayerPreferencesTable.player_uuid).append(", ").append(column).append(") \n")
                .append("VALUES('").append(playerUUID).append("', ").append(value).append(") \n")
                .append("ON CONFLICT(").append(DatabaseTables.Hud.PlayerPreferencesTable.player_uuid).append(") DO \n")
                .append("UPDATE SET ").append(column).append(" = ").append(value);

            DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(sqlBuilder.toString());
        }
    }
}
