package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;

public class HudDatabaseAPI
{
    private final String tablePrefix = "hud_";
    private DatabaseManager databaseManager;
    private ConsoleUtils consoleUtils;

    public HudDatabaseAPI(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
        consoleUtils = new ConsoleUtils("CraftEra Suite - HUD");
        setup();
    }

    private void setup()
    {
        String playerCfgTableSql = "CREATE TABLE IF NOT EXISTS hud_player_preferences (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	player_uuid text NOT NULL,\n"
                + "	enabled numeric NOT NULL,\n"
                + "	coordinates numeric NOT NULL,\n"
                + "	coordinates_nether_portal numeric NOT NULL,\n"
                + "	durability numeric NOT NULL,\n"
                + "	orientation numeric NOT NULL,\n"
                + "	time_server numeric NOT NULL,\n"
                + "	time_world numeric NOT NULL,\n"
                + "	time_world_work_hours numeric NOT NULL,\n"
                + "	plugin_spectator_range numeric NOT NULL,\n"
                + "	commerce numeric NOT NULL\n"
                + ");";

        if (databaseManager.database.executeSql(playerCfgTableSql))
        {
            consoleUtils.logSuccess("Table successfully created: hud_player_preferences");
        } else {
            consoleUtils.logError("Failed to create table: hud_player_preferences");
        }
    }
}
