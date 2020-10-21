package com.github.scorchedpsyche.craftera_suite.modules.main.database;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

public class SeasonsDatabaseApi
{
    public SeasonsDatabaseApi()
    {
        consoleUtils = new ConsoleUtils("CraftEra Suite - Core");
        setup();
    }

    private final String tablePrefix = "seasons_";
    private ConsoleUtils consoleUtils;

    private void setup()
    {
        String seasonsListTableSql = "CREATE TABLE IF NOT EXISTS " + DatabaseTables.Seasons.list_TABLENAME + "(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	" + DatabaseTables.Seasons.ListTable.number + " NUMERIC DEFAULT 1 UNIQUE NOT NULL,\n"
                + "	" + DatabaseTables.Seasons.ListTable.title + " TEXT,\n"
                + "	" + DatabaseTables.Seasons.ListTable.subtitle + " TEXT,\n"
                + "	" + DatabaseTables.Seasons.ListTable.status + " NUMERIC DEFAULT 0,\n"
                + ");";
    }
}
