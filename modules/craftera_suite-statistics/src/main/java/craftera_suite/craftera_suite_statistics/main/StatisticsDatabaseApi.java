package craftera_suite.craftera_suite_statistics.main;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteSeasons;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;
import craftera_suite.craftera_suite_statistics.model.PlayerLoginModel;
import org.bukkit.Bukkit;

import java.sql.*;

public class StatisticsDatabaseApi
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if Statistics table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.Statistics.table_name) )
        {
            String sql = "CREATE TABLE "
                    + DatabaseTables.Statistics.table_name + "(\n"
                    + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                    + "	" + DatabaseTables.Statistics.Table.player_uuid + " TEXT NOT NULL,\n"
                    + "	" + DatabaseTables.Statistics.Table.date_login + " INTEGER NOT NULL,\n"
                    + "	" + DatabaseTables.Statistics.Table.date_logout + " INTEGER,\n"
                    + "	" + DatabaseTables.Statistics.Table.time_spent_online + " INTEGER";

            if( SuitePluginManager.Seasons.isEnabled() )
            {
                sql += ",\n"
                    + "	" + DatabaseTables.Achievements.Table.season + " INTEGER DEFAULT 0";
            }

            sql += "\n);";

            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(sql) )
            {
                // Successfully created table
                ConsoleUtil.logMessage(SuitePluginManager.Achievements.Name.full,
                                        "Table successfully created: " + DatabaseTables.Statistics.table_name);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtil.logError( SuitePluginManager.Achievements.Name.full,
                                   "Failed to create table: " + DatabaseTables.Statistics.table_name);

            return false;
        }

        // If we got here table exists
        return true;
    }

    public void addLoginForPlayerIfNotExists(PlayerLoginModel playerLoginModel)
    {
        // Not added. Must add it
        String sql = "INSERT INTO " + DatabaseTables.Statistics.table_name + " (\n"
                + DatabaseTables.Statistics.Table.player_uuid + ",\n"
                + DatabaseTables.Statistics.Table.date_login + ",\n"
                + DatabaseTables.Statistics.Table.date_logout + ",\n"
                + DatabaseTables.Statistics.Table.time_spent_online;

        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-seasons") && CraftEraSuiteSeasons.seasonManager.current != null )
        {
            sql += ",\n"
                    + DatabaseTables.Achievements.Table.season + "\n";
        }

        sql += ") VALUES (\n"
                + "'" + playerLoginModel.getPlayer().getUniqueId().toString() + "',\n"
                + playerLoginModel.login_time_start + ",\n"
                + playerLoginModel.login_time_end + ",\n"
                + playerLoginModel.login_time_total;

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

                playerLoginModel.id_from_DB = rs.getInt(1);
            }
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }
    }

    public boolean updateLoginTimerForPlayer(PlayerLoginModel playerLoginModel)
    {
        String sql = "UPDATE " + DatabaseTables.Statistics.table_name + " SET "
                + DatabaseTables.Statistics.Table.date_logout + " = " + playerLoginModel.login_time_end + ", "
                + DatabaseTables.Statistics.Table.time_spent_online + " = " + playerLoginModel.login_time_total +
                " WHERE id = " + playerLoginModel.id_from_DB;

        return DatabaseManager.database.executeSqlAndDisplayErrorIfNeeded(sql);
    }
}
