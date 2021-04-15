package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteSeasons;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;

import java.sql.*;

public class AchievementsDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if Achievements table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Achievements.achievements_TABLENAME ) )
        {
            String sql = "CREATE TABLE "
                    + DatabaseTables.Achievements.achievements_TABLENAME + "(\n"
                    + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                    + "	" + DatabaseTables.Achievements.Table.player_uuid + " TEXT NOT NULL,\n"
                    + "	" + DatabaseTables.Achievements.Table.achievement + " TEXT NOT NULL,\n"
                    + "	" + DatabaseTables.Achievements.Table.date + " INTEGER";

            if( SuitePluginManager.Seasons.isEnabled() )
            {
                sql += ",\n"
                    + "	" + DatabaseTables.Achievements.Table.season + " INTEGER DEFAULT 0";
            }

            sql += "\n);";

            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSql(sql) )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Achievements.Name.full,
                                        "Table successfully created: " + DatabaseTables.Achievements.achievements_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtil.logError( SuitePluginManager.Achievements.Name.full,
                                   "Failed to create table: " + DatabaseTables.Achievements.achievements_TABLENAME);

            return false;
        }

        // If we got here table exists
        return true;
    }

    public void addAchievementForPlayerIfNotExists(AchievementModel achievement)
    {
        String sql = "SELECT * FROM " + DatabaseTables.Achievements.achievements_TABLENAME + " WHERE "
                + DatabaseTables.Achievements.Table.player_uuid + " = '" + achievement.playerUUid + "' AND \n"
                + DatabaseTables.Achievements.Table.achievement + " = '" + achievement.achievement + "' AND \n"
                + DatabaseTables.Achievements.Table.date + " = " + achievement.date + "\n";

        if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
        {
            sql += " AND \n"
                    + DatabaseTables.Achievements.Table.season + " = " + CraftEraSuiteSeasons.seasonManager.current.getId() + "\n";
        }

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            // Check if the achievement is already added
            if( DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                // Not added. Must add it
                sql = "INSERT INTO " + DatabaseTables.Achievements.achievements_TABLENAME + " (\n"
                        + DatabaseTables.Achievements.Table.player_uuid + ",\n"
                        + DatabaseTables.Achievements.Table.achievement + ",\n"
                        + DatabaseTables.Achievements.Table.date;

                if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
                {
                    sql += ",\n"
                            + DatabaseTables.Achievements.Table.season + "\n";
                }

                sql += ") VALUES (\n"
                        + "'" + achievement.playerUUid + "',\n"
                        + "'" + achievement.achievement + "',\n"
                        + achievement.date;

                if( SuitePluginManager.Seasons.isEnabled() && CraftEraSuiteSeasons.seasonManager.current != null )
                {
                    sql += ",\n"
                            + CraftEraSuiteSeasons.seasonManager.current.getId() + "\n";
                }

                sql += ");";

                // Try to add the achievement for the player
                if ( !DatabaseManager.database.executeSql(sql) )
                {
                    ConsoleUtil.logError( SuitePluginManager.Achievements.Name.full,
                            "Failed to add achievement for player.");
                }
            }
        } catch (SQLException e) {
            ConsoleUtil.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "SQLite query failed for 'fetchCurrentSeason': " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }
    }
}
