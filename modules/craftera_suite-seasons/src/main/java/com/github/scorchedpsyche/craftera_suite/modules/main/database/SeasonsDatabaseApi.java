package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;

import java.sql.*;

public class SeasonsDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if Seasons table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Seasons.seasons_TABLENAME ) )
        {
            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSql(
                    "CREATE TABLE " + DatabaseTables.Seasons.seasons_TABLENAME + "(\n"
                            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                            + "	" + DatabaseTables.Seasons.Table.number + " NUMERIC DEFAULT 1 UNIQUE NOT NULL,\n"
                            + "	" + DatabaseTables.Seasons.Table.title + " TEXT,\n"
                            + "	" + DatabaseTables.Seasons.Table.subtitle + " TEXT,\n"
                            + "	" + DatabaseTables.Seasons.Table.status + " NUMERIC DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.date_start + " INTEGER DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.date_end + " INTEGER DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.minecraft_version_start + " TEXT DEFAULT 0,\n"
                            + "	" + DatabaseTables.Seasons.Table.minecraft_version_end + " TEXT DEFAULT 0\n"
                            + ");") )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Seasons.Name.full,
                                        "Table successfully created: " + DatabaseTables.Seasons.seasons_TABLENAME);

//                // If achievements is enabled, verify
//                verifyAchievementsTableIfPluginIsEnabled();

                return true;
            }

            // If we got here table creation failed
            ConsoleUtil.logError( SuitePluginManager.Seasons.Name.full,
                                   "Failed to create table: " + DatabaseTables.Seasons.seasons_TABLENAME);

            return false;
        }

//        // If achievements is enabled, verify
//        verifyAchievementsTableIfPluginIsEnabled();

        // If we got here table exists
        return true;
    }

//    private void verifyAchievementsTableIfPluginIsEnabled()
//    {
//        // If achievements is enabled, verify
//        if( SuitePluginManager.Achievements.isEnabled() )
//        {
//            // Check if Achievements table exists
//            if( DatabaseManager.database.tableExists( DatabaseTables.Achievements.achievements_TABLENAME ) )
//            {
//                String sql = "PRAGMA table_info (" + DatabaseTables.Achievements.achievements_TABLENAME + ")";
//
//                try (Connection conn = DriverManager.getConnection(
//                        DatabaseManager.database.getDatabaseUrl());
//                     Statement stmt = conn.createStatement())
//                {
//                    ResultSet rs = stmt.executeQuery(sql);
//
//                    if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
//                    {
//                        boolean seasonColumnFound = false;
//
//                        while(rs.next())
//                        {
//                            if( rs.getString(2).equals("season") )
//                            {
//                                seasonColumnFound = true;
//                            }
//                        }
//
//                        if( !seasonColumnFound )
//                        {
//                            // Season column not found. Attempt to create it
//                            if( DatabaseManager.database.executeSql("ALTER TABLE " + DatabaseTables.Achievements.achievements_TABLENAME + " ADD COLUMN "
//                                    + DatabaseTables.Achievements.Table.season + " INTEGER DEFAULT 0;") )
//                            {
//                                // Successfully created column
//                                ConsoleUtil.logMessage(SuitePluginManager.Achievements.Name.full,
//                                        "Column successfully created: "
//                                                + DatabaseTables.Achievements.achievements_TABLENAME + "."
//                                                + DatabaseTables.Achievements.Table.season);
//                            } else {
//                                ConsoleUtil.logError(SuitePluginManager.Achievements.Name.full,
//                                        "Failed to create column: "
//                                                + DatabaseTables.Achievements.achievements_TABLENAME + "."
//                                                + DatabaseTables.Achievements.Table.season);
//                            }
//                        }
//                    }
//                } catch (SQLException e) {
//                    ConsoleUtil.logError( SuitePluginManager.Seasons.Name.full,
//                            "Failed to fetch 'PRAGMA table_info': " + DatabaseTables.Seasons.seasons_TABLENAME);
//                    ConsoleUtil.logError( e.getMessage() );
//                }
//            }
//        }
//    }
}