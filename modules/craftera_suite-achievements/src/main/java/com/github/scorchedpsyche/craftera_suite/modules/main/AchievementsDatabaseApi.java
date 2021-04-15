package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.AchievementModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;

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

//        String sql = "SELECT * FROM " + DatabaseTables.Achievements.achievements_TABLENAME + " WHERE "
//                + ;
    }
}
