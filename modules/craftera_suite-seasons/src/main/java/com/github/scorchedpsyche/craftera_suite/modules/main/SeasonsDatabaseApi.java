package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.SeasonModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class SeasonsDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if Seasons table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Seasons.table_name) )
        {
            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(
                    "CREATE TABLE " + DatabaseTables.Seasons.table_name + "(\n"
                            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                            + "	" + DatabaseTables.Seasons.Table.number + " NUMERIC DEFAULT 1 UNIQUE NOT NULL,\n"
                            + "	" + DatabaseTables.Seasons.Table.title + " TEXT,\n"
                            + "	" + DatabaseTables.Seasons.Table.subtitle + " TEXT,\n"
                            + "	" + DatabaseTables.Seasons.Table.status + " INTEGER DEFAULT " + SuitePluginManager.Seasons.Status.Inactive.ordinal() + ",\n"
                            + "	" + DatabaseTables.Seasons.Table.account + " NUMERIC DEFAULT 1,\n"
                            + "	" + DatabaseTables.Seasons.Table.date_start + " INTEGER DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.date_end + " INTEGER DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.minecraft_version_start + " TEXT DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.minecraft_version_end + " TEXT DEFAULT 0\n"
                            + ");") )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Seasons.Name.full,
                                        "Table successfully created: " + DatabaseTables.Seasons.table_name);

                String sql = "INSERT INTO " + DatabaseTables.Seasons.table_name + " ("
                        + DatabaseTables.Seasons.Table.number + ", "
                        + DatabaseTables.Seasons.Table.title + ", "
                        + DatabaseTables.Seasons.Table.subtitle + ", "
                        + DatabaseTables.Seasons.Table.status + ", "
                        + DatabaseTables.Seasons.Table.account + ", "
                        + DatabaseTables.Seasons.Table.date_start + ", "
                        + DatabaseTables.Seasons.Table.minecraft_version_start + ") \n" +
                        "VALUES("
                        + "1, "
                        + "'No Title', "
                        + "'No Subtitle', "
                        + "'" + SuitePluginManager.Seasons.Status.Active.ordinal() + "', "
                        + true + ", "
                        + 0 + ", "
                        + 0 + ")";

                if ( DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(sql) )
                {
                    ConsoleUtil.logMessage( SuitePluginManager.Seasons.Name.full,
                            "Created Season 1! Change the title and subtitle as desired.");
                } else {
                    ConsoleUtil.logError( SuitePluginManager.Seasons.Name.full,
                            "Failed to create first season.");
                }

                return true;
            }

            // If we got here table creation failed
            ConsoleUtil.logError( SuitePluginManager.Seasons.Name.full,
                                   "Failed to create table: " + DatabaseTables.Seasons.table_name);

            return false;
        }

        // If we got here table exists
        return true;
    }

    @Nullable
    public SeasonModel fetchCurrentSeason()
    {
        String sql = "SELECT * FROM " + DatabaseTables.Seasons.table_name + " WHERE "
                + DatabaseTables.Seasons.Table.status + " = " + SuitePluginManager.Seasons.Status.Started.ordinal()
                + " OR "
                + DatabaseTables.Seasons.Table.status + " = " + SuitePluginManager.Seasons.Status.Active.ordinal()
                + " LIMIT 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                return new SeasonModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtil.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "SQLite query failed for 'fetchCurrentSeason': " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }

        return null;
    }

    @Nullable
    public Integer fetchNextAvailableSeasonNumber()
    {
        String sql = "SELECT * FROM " + DatabaseTables.Seasons.table_name + " ORDER BY "
                + DatabaseTables.Seasons.Table.number + " DESC ";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                return new SeasonModel().loadDataFromResultSet(rs).getNumber() + 1;
            }
        } catch (SQLException e) {
            ConsoleUtil.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "SQLite query failed for 'fetchCurrentSeason': " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }

        return null;
    }
}