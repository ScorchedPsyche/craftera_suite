package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.SeasonModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class RewardsDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if ClaimOnce table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Rewards.table_name) )
        {
            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(
                    "CREATE TABLE " + DatabaseTables.Rewards.table_name + "(\n"
                            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                            + "	" + DatabaseTables.Rewards.Claim.active + " INTEGER DEFAULT 1 NOT NULL,\n"
                            + "	" + DatabaseTables.Rewards.Claim.type + " INTEGER NOT NULL,\n"
                            + "	" + DatabaseTables.Rewards.Claim.source + " INTEGER NOT NULL,\n"
                            + "	" + DatabaseTables.Rewards.Claim.reward_type + " INTEGER NOT NULL,\n"
                            + "	" + DatabaseTables.Rewards.Claim.reward_nbt + " TEXT,\n"
                            + "	" + DatabaseTables.Rewards.Claim.date_start + " INTEGER DEFAULT 0,\n"
                            + "	" + DatabaseTables.Rewards.Claim.date_end + " INTEGER DEFAULT 0,\n"
                            + "	" + DatabaseTables.Rewards.Claim.condition + " INTEGER NOT NULL,\n"
                            + "	" + DatabaseTables.Rewards.Claim.condition_modifier + " TEXT,\n"
                            + ");") )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Rewards.Name.full,
                                        "Table successfully created: " + DatabaseTables.Rewards.table_name);
            } else {
                // If we got here table creation failed
                ConsoleUtil.logError( SuitePluginManager.Rewards.Name.full,
                        "Failed to create table: " + DatabaseTables.Rewards.table_name);

                return false;
            }
        }

        // If we got here tables exist or were successfully created
        return true;
    }
}