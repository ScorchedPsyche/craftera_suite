package modules.com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.DatabaseUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.model.SpectatorPlayerDataModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpectatorDatabaseAPI
{
    public boolean setupAndVerifySqlTable()
    {
        // Check if table exists
        if( !DatabaseManager.database.tableExists( DatabaseTables.SpectatorMode.player_data_TABLENAME ) )
        {
            // Doesn't exists. Create it
            if ( DatabaseManager.database.executeSql(
                    "CREATE TABLE " + DatabaseTables.SpectatorMode.player_data_TABLENAME + "(\n"
                            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid + " TEXT UNIQUE NOT NULL,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + " NUMERIC DEFAULT 0,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.game_mode + " TEXT DEFAULT SURVIVAL,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.x + " REAL DEFAULT 0,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.y + " REAL DEFAULT 0,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.z + " REAL DEFAULT 0,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.world + " TEXT DEFAULT NONE,\n"
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.environment + " TEXT DEFAULT NORMAL\n"
                            + ");") )
            {
                // Successfully created table
                ConsoleUtils.logMessage(SuitePluginManager.SpectatorMode.Name.full,
                        "Table successfully created: " + DatabaseTables.SpectatorMode.player_data_TABLENAME);
                return true;
            }

            // If we got here table creation failed
            ConsoleUtils.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "Failed to create table: " + DatabaseTables.SpectatorMode.player_data_TABLENAME);

            return false;
        }

        // If we got here table exists
        return true;
    }

//    public SpectatorPlayerDataModel getPlayerData(String playerUUID)
//    {
//        String sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
//                " WHERE player_uuid='" + playerUUID + "' LIMIT 1";
//
//        try (Connection conn = DriverManager.getConnection(
//                DatabaseManager.database.getDatabaseUrl());
//             Statement stmt = conn.createStatement())
//        {
//            ResultSet rs = stmt.executeQuery(sql);
//
//            if( !DatabaseUtils.isResultSetNullOrEmpty(rs) )
//            {
//                return new SpectatorPlayerDataModel().loadDataFromResultSet(rs);
//            }
//        } catch (SQLException e) {
//            ConsoleUtils.logError(
//                    "SQLite query failed: " + sql);
//            ConsoleUtils.logError( e.getMessage() );
//        }
//
//        return null;
//    }

    public SpectatorPlayerDataModel enableSpectatorModeForPlayer(Player player)
    {
        String sql = "INSERT INTO " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " ("
                + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.game_mode + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.x + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.y + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.z + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.world  + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.environment + ") \n" +
                    "VALUES('"
                + player.getUniqueId() + "', "
                + "1, "
                + "'" + player.getGameMode().toString() + "', "
                + player.getLocation().getX() + ", "
                + player.getLocation().getY() + ", "
                + player.getLocation().getZ() + ", "
                + "'" + Objects.requireNonNull(player.getLocation().getWorld()).getUID().toString() + "', "
                + "'" + player.getLocation().getWorld().getEnvironment().toString() + "') \n" +
                    "ON CONFLICT (" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + ") DO UPDATE SET "
                + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + " = 1, "
                + DatabaseTables.SpectatorMode.PlayerDataTable.game_mode + " = '" + player.getGameMode().toString() + "', "
                + DatabaseTables.SpectatorMode.PlayerDataTable.x + " = " + player.getLocation().getX() + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.y + " = " + player.getLocation().getY() + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.z + " = " + player.getLocation().getZ() + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.world + " = '" + Objects.requireNonNull(player.getLocation().getWorld()).getUID().toString() + "', "
                + DatabaseTables.SpectatorMode.PlayerDataTable.environment + " = '" + player.getLocation().getWorld().getEnvironment().toString() + "'";

        if( DatabaseManager.database.executeSql(sql) )
        {
            sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                    " WHERE " + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid +
                    " = '" + player.getUniqueId() + "'";

            try (Connection conn = DriverManager.getConnection(
                    DatabaseManager.database.getDatabaseUrl());
                 Statement stmt = conn.createStatement())
            {
                ResultSet rs = stmt.executeQuery(sql);

                if( !DatabaseUtils.isResultSetNullOrEmpty(rs) )
                {
                    return new SpectatorPlayerDataModel().loadDataFromResultSet(rs);
                }
            } catch (SQLException e) {
                ConsoleUtils.logError( SuitePluginManager.SpectatorMode.Name.full,
                        "SQLite query failed for 'enableSpectatorModeForPlayer': " + sql);
                ConsoleUtils.logError( e.getMessage() );
            }
        } else {
            PlayerUtils.sendMessageWithPluginPrefix( player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Failed to go into Spectator Mode! Contact server's admin.");
            ConsoleUtils.logError( SuitePluginManager.SpectatorMode.Name.full,
                    "Failed to add player to the database with the following SQL Statement: " +
                            ChatColor.RED + sql);
        }

        return null;
    }

    public SpectatorPlayerDataModel disableSpectatorModeForPlayer(Player player)
    {
        String sql = "UPDATE " + DatabaseTables.SpectatorMode.player_data_TABLENAME + " SET "
                + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + " = 0 WHERE player_uuid='" + player.getUniqueId().toString() + "'";

        DatabaseManager.database.executeSql(sql);

        sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " WHERE " + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid +
                " = '" + player.getUniqueId() + "'";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetNullOrEmpty(rs) )
            {
                return new SpectatorPlayerDataModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite query failed for 'disableSpectatorModeForPlayer': " + sql);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }

    @Nullable
    public List<SpectatorPlayerDataModel> fetchAllPlayersWithSpectatorModeEnabled()
    {
        String sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " WHERE " + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + " = 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetNullOrEmpty(rs) )
            {
                List<SpectatorPlayerDataModel> playersOnSpectator = new ArrayList<>();

                while(rs.next())
                {
                    playersOnSpectator.add(new SpectatorPlayerDataModel().loadDataFromResultSet(rs));
                }

                return playersOnSpectator;
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite query failed for 'fetchAllPlayersWithSpectatorModeEnabled': " + sql);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }

    public SpectatorPlayerDataModel fetchPlayer(String uuid)
    {
        String sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " WHERE " + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid + " = '" + uuid + "'";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetNullOrEmpty(rs) && rs.next() )
            {
                return new SpectatorPlayerDataModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite query failed for 'fetchPlayer': " + sql);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }
}
