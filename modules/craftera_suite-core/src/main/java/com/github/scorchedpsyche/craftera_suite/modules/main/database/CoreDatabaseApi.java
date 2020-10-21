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

    }
}
