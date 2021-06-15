package com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteSeasons;
import com.github.scorchedpsyche.craftera_suite.modules.craftera_suite_afk.model.PlayerAFKModel;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class AFKDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if AFK table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.AFK.afk_TABLENAME ) )
        {
            String sql = "CREATE TABLE "
                    + DatabaseTables.AFK.afk_TABLENAME + "(\n"
                    + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                    + "	" + DatabaseTables.AFK.Table.player_uuid + " TEXT NOT NULL,\n"
                    + "	" + DatabaseTables.AFK.Table.afk_time_start + " INTEGER NOT NULL,\n"
                    + "	" + DatabaseTables.AFK.Table.afk_time_end + " INTEGER,\n"
                    + "	" + DatabaseTables.AFK.Table.afk_time_total + " INTEGER";

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
                                        "Table successfully created: " + DatabaseTables.AFK.afk_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtil.logError( SuitePluginManager.Achievements.Name.full,
                                   "Failed to create table: " + DatabaseTables.AFK.afk_TABLENAME);

            return false;
        }

        // If we got here table exists
        return true;
    }

    public void addAFKForPlayerIfNotExists(PlayerAFKModel playerAFKModel)
    {
        // Not added. Must add it
        String sql = "INSERT INTO " + DatabaseTables.AFK.afk_TABLENAME + " (\n"
                + DatabaseTables.AFK.Table.player_uuid + ",\n"
                + DatabaseTables.AFK.Table.afk_time_start + ",\n"
                + DatabaseTables.AFK.Table.afk_time_end + ",\n"
                + DatabaseTables.AFK.Table.afk_time_total;

        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-seasons") && CraftEraSuiteSeasons.seasonManager.current != null )
        {
            sql += ",\n"
                    + DatabaseTables.Achievements.Table.season + "\n";
        }

        sql += ") VALUES (\n"
                + "'" + playerAFKModel.getPlayer().getUniqueId().toString() + "',\n"
                + playerAFKModel.afk_time_start + ",\n"
                + playerAFKModel.afk_time_end + ",\n"
                + playerAFKModel.afk_time_total;

        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-seasons") && CraftEraSuiteSeasons.seasonManager.current != null )
        {
            sql += ",\n"
                    + CraftEraSuiteSeasons.seasonManager.current.getId() + "\n";
        }

        sql += ");";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);

            sql = "select last_insert_rowid();";

            ResultSet rs = stmt.executeQuery(sql);
            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                rs.next();

                playerAFKModel.id_from_DB = rs.getInt(1);
            }
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }
    }

    public boolean updateAFKTimerForPlayer(PlayerAFKModel player)
    {
        String sql = "UPDATE " + DatabaseTables.AFK.afk_TABLENAME + " SET "
                + DatabaseTables.AFK.Table.afk_time_end + " = " + player.afk_time_end + ", "
                + DatabaseTables.AFK.Table.afk_time_total + " = " + player.afk_time_total +
                " WHERE id = " + player.id_from_DB;

        return DatabaseManager.database.executeSql(sql);
    }
}
