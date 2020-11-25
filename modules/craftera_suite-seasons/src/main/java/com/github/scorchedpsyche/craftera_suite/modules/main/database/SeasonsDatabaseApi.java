package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

public class SeasonsDatabaseApi
{
    private boolean setupAndVerifySqlTable()
    {
        // Check if table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Hud.player_preferences_TABLENAME ) )
        {
            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSql(
                    "CREATE TABLE " + DatabaseTables.Seasons.list_TABLENAME + "(\n"
                            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                            + "	" + DatabaseTables.Seasons.ListTable.number + " NUMERIC DEFAULT 1 UNIQUE NOT NULL,\n"
                            + "	" + DatabaseTables.Seasons.ListTable.title + " TEXT,\n"
                            + "	" + DatabaseTables.Seasons.ListTable.subtitle + " TEXT,\n"
                            + "	" + DatabaseTables.Seasons.ListTable.status + " NUMERIC DEFAULT 0,\n"
                            + ");") )
            {
                // Successfully created table
                ConsoleUtils.logMessage(SuitePluginManager.Seasons.Name.full,
                                        "Table successfully created: " + DatabaseTables.Seasons.list_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtils.logError( SuitePluginManager.Seasons.Name.full,
                                   "Failed to create table: " + DatabaseTables.Seasons.list_TABLENAME);

            return false;
        }

        // If we got here table exists
        return true;
    }
}
