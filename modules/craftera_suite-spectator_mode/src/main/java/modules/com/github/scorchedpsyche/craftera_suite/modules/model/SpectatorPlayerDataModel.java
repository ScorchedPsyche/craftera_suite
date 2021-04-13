package modules.com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.DatabaseUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpectatorPlayerDataModel
{
    private int id;
    private String player_uuid;
    private Boolean enabled;
    private String game_mode;
    private Double x;
    private Double y;
    private Double z;
    private String world;
    private String environment;

    @Nullable
    public SpectatorPlayerDataModel loadDataFromResultSet(@NotNull ResultSet rs)
    {
        try {
            id = rs.getInt(1);
            player_uuid = rs.getString(2);
            enabled = rs.getBoolean(3);
            game_mode = rs.getString(4);
            x = rs.getDouble(5);
            y = rs.getDouble(6);
            z = rs.getDouble(7);
            world = rs.getString(8);
            environment = rs.getString(9);

            return this;
        } catch (SQLException e)
        {
            ConsoleUtils.logError(
                    SuitePluginManager.SpectatorMode.Name.full,
                    "Failed to load player data from ResultSet. TRACE:");
            e.printStackTrace();
        }

        return null;
    }

    public String getPlayerUUID() { return player_uuid; }
    public boolean isSpectatorEnabled() { return enabled; }
    public String getGameMode() { return game_mode; }
    public Double getX() { return x; }
    public Double getY() { return y; }
    public Double getZ() { return z; }
    public String getWorld() { return world; }
    public String getEnvironment() { return environment; }
//    public void setPreference(String preference, boolean
//    public String getPlayerUUID() { return player_uuid; }value)
//    {
//        switch(preference)
//        {
//            case DatabaseTables.SpectatorMode.PlayerDataTable.enabled:
//                enabled = value;
//                break;
//        }
//    }

//    public void togglePreference(String preference)
//    {
//        switch(preference)
//        {
//            case DatabaseTables.Hud.PlayerPreferencesTable.colorize_coordinates:
//                colorize_coordinates = !colorize_coordinates;
//                break;
//        }
//    }
}