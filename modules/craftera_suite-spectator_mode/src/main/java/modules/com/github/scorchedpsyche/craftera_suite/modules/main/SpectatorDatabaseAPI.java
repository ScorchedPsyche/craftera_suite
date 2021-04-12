package modules.com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.DatabaseUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.model.SpectatorPlayerDataModel;
import org.bukkit.entity.Player;

import java.sql.*;

public class SpectatorDatabaseAPI
{
    public SpectatorPlayerDataModel getPlayerData(String playerUUID)
    {
        String sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " WHERE player_uuid='" + playerUUID + "' LIMIT 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetEmpty(rs) )
            {
                return new SpectatorPlayerDataModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }

    public void enableSpectatorModeForPlayer(Player player)
    {
        String sql = "INSERT INTO " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " ("
                + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.game_mode + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.x + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.y + ", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.z + ") \n" +
                    "VALUES('"
                + player.getUniqueId() + "', "
                + "1, "
                + "'" + player.getGameMode().toString() + "', "
                + player.getLocation().getX() + ", "
                + player.getLocation().getY() + ", "
                + player.getLocation().getZ() + ") \n" +
                    "ON CONFLICT(" + DatabaseTables.Hud.PlayerPreferencesTable.player_uuid + ") DO UPDATE SET "
                + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + " = 1, "
                + DatabaseTables.SpectatorMode.PlayerDataTable.game_mode + " = '" + player.getGameMode().toString() +"', "
                + DatabaseTables.SpectatorMode.PlayerDataTable.x + " = " + player.getLocation().getX() +", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.y + " = " + player.getLocation().getY() +", "
                + DatabaseTables.SpectatorMode.PlayerDataTable.z + " = " + player.getLocation().getZ();

        DatabaseManager.database.executeSql(sql);
    }

    public SpectatorPlayerDataModel disableSpectatorModeForPlayer(Player player)
    {
        String sql = "UPDATE " + DatabaseTables.SpectatorMode.player_data_TABLENAME + " SET "
                + DatabaseTables.SpectatorMode.PlayerDataTable.enabled + " = 0";

        DatabaseManager.database.executeSql(sql);

        sql = "SELECT * FROM " + DatabaseTables.SpectatorMode.player_data_TABLENAME +
                " WHERE " + DatabaseTables.SpectatorMode.PlayerDataTable.player_uuid +
                " = '" + player.getUniqueId() + "'";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery(sql);

            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtils.isResultSetEmpty(rs) )
            {
                return new SpectatorPlayerDataModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtils.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtils.logError( e.getMessage() );
        }

        return null;
    }

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
                            + "	" + DatabaseTables.SpectatorMode.PlayerDataTable.z + " REAL DEFAULT 0\n"
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
}
