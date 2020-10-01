package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.interfaces.IDatabase;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

public class HudDatabaseAPI
{
    private final String tablePrefix = "hud_";
    private IDatabase database;
    private ConsoleUtils consoleUtils;

    public HudDatabaseAPI(IDatabase database)
    {
        this.database = database;
        consoleUtils = new ConsoleUtils("CraftEra Suite - HUD");
        setup();
    }

    private void setup()
    {
        String playerPreferencesTableSql = "CREATE TABLE IF NOT EXISTS hud_player_preferences (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	player_uuid TEXT NOT NULL,\n"
                + "	enabled NUMERIC DEFAULT 0,\n"
                + "	coordinates NUMERIC DEFAULT 1,\n"
                + "	coordinates_nether_portal NUMERIC DEFAULT 0,\n"
                + "	durability NUMERIC DEFAULT 1,\n"
                + "	orientation NUMERIC DEFAULT 1,\n"
                + "	time_server NUMERIC DEFAULT 0,\n"
                + "	time_world NUMERIC DEFAULT 1,\n"
                + "	time_world_work_hours NUMERIC DEFAULT 1,\n"
                + "	plugin_spectator_range NUMERIC DEFAULT 1,\n"
                + "	commerce NUMERIC DEFAULT 1\n"
                + ");";

        if (database.executeSql(playerPreferencesTableSql))
        {
            consoleUtils.logSuccess("Table successfully created: hud_player_preferences");
        } else {
            consoleUtils.logError("Failed to create table: hud_player_preferences");
        }
    }
}
