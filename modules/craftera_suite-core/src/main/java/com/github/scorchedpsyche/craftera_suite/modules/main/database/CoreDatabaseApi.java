package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

public class CoreDatabaseApi
{
    public CoreDatabaseApi()
    {
        consoleUtils = new ConsoleUtils("CraftEra Suite - Core");
    }

    private final String tablePrefix = "core_";
    private ConsoleUtils consoleUtils;

    private void setup()
    {
        String playerPreferencesTableSql = "CREATE TABLE IF NOT EXISTS " + DatabaseTables.Core.seasons + "(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	" + DatabaseTables.Core.Seasons.number + " NUMERIC DEFAULT 1 UNIQUE NOT NULL,\n"
                + "	" + DatabaseTables.Core.Seasons.title + " TEXT,\n"
                + "	" + DatabaseTables.Core.Seasons.subtitle + " TEXT,\n"
                + "	" + DatabaseTables.Core.Seasons.status + " NUMERIC DEFAULT 0,\n"
                + ");";
    }
}
