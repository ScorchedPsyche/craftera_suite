package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.SeasonModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DatabaseUtil;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeasonManager {
    public SeasonManager(SeasonsDatabaseApi seasonsDatabaseApi) {
        this.seasonsDatabaseApi = seasonsDatabaseApi;
    }

    @Nullable
    public SeasonModel current;
    public SeasonModel selected;
    private SeasonsDatabaseApi seasonsDatabaseApi;

    @Nullable
    public SeasonModel createSeason(int number, String title, String subtitle, int status,
                             boolean account, long date_start, long date_end, String version_start, String version_end)
    {
        String sql = "INSERT INTO " + DatabaseTables.Seasons.seasons_TABLENAME + " ("
                + DatabaseTables.Seasons.Table.number + ", "
                + DatabaseTables.Seasons.Table.title + ", "
                + DatabaseTables.Seasons.Table.subtitle + ", "
                + DatabaseTables.Seasons.Table.status + ", "
                + DatabaseTables.Seasons.Table.account + ", "
                + DatabaseTables.Seasons.Table.date_start + ", "
                + DatabaseTables.Seasons.Table.date_end + ", "
                + DatabaseTables.Seasons.Table.minecraft_version_start + ", "
                + DatabaseTables.Seasons.Table.minecraft_version_end + ") \n" +
                "VALUES("
                + number + ", "
                + "'" + title + "', "
                + "'" + subtitle + "', "
                + "'" + status + "', "
                + account + ", "
                + date_start + ", "
                + date_end + ", "
                + "'" + version_start + "', "
                + "'" + version_end + "')";

        if ( DatabaseManager.database.executeSql(sql) )
        {
            sql = "SELECT * FROM " + DatabaseTables.Seasons.seasons_TABLENAME +
                    " ORDER BY id DESC LIMIT 1";

            try (Connection conn = DriverManager.getConnection(
                    DatabaseManager.database.getDatabaseUrl());
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);

                if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
                {
                    return new SeasonModel().loadDataFromResultSet(rs);
                }
            } catch (SQLException e) {
                ConsoleUtil.logError(
                        "SQLite query failed: " + sql);
                ConsoleUtil.logError( e.getMessage() );
            }
        } else {
            ConsoleUtil.logError( SuitePluginManager.Seasons.Name.full, "Failed to create season.");
        }

        return null;
    }

    public SeasonManager setCurrentSeason(SeasonModel currentSeason)
    {
        current = currentSeason;
        return this;
    }

    @Nullable
    public SeasonModel fetchActiveSeason()
    {
        String sql = "SELECT * FROM " + DatabaseTables.Seasons.seasons_TABLENAME +
                " WHERE status = " + SuitePluginManager.Seasons.Status.Active.ordinal() + " LIMIT 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                return new SeasonModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }

        return null;
    }

    public boolean isActiveSeasonValid() {
        return current != null;
    }
    public boolean isSelectedSeasonValid() {
        return selected != null;
    }

    public int getNextAvailableSeasonNumber()
    {
        return seasonsDatabaseApi.fetchNextAvailableSeasonNumber();
    }

    public boolean updateSeason(SeasonModel season)
    {
        String sql = "UPDATE " + DatabaseTables.Seasons.seasons_TABLENAME + " SET "
                + DatabaseTables.Seasons.Table.number + " = " + season.getNumber() + ", "
                + DatabaseTables.Seasons.Table.title + " = '" + season.getTitle() + "', "
                + DatabaseTables.Seasons.Table.subtitle + " = '" + season.getSubtitle() + "', "
                + DatabaseTables.Seasons.Table.status + " = " + season.getStatus() + ", "
                + DatabaseTables.Seasons.Table.account + " = " + season.getAccount() + ", "
                + DatabaseTables.Seasons.Table.date_start + " = " + season.getDate_start() + ", "
                + DatabaseTables.Seasons.Table.date_end + " = " + season.getDate_end() + ", "
                + DatabaseTables.Seasons.Table.minecraft_version_start + " = '" + season.getMinecraft_version_start() + "', "
                + DatabaseTables.Seasons.Table.minecraft_version_end + " = '" + season.getMinecraft_version_end()
                + "' WHERE id=" + season.getId();

        return DatabaseManager.database.executeSql(sql);
    }

    @Nullable
    public SeasonModel fetchSeason(int season_number)
    {
        String sql = "SELECT * FROM " + DatabaseTables.Seasons.seasons_TABLENAME +
                " WHERE number = " + season_number + " LIMIT 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                return new SeasonModel().loadDataFromResultSet(rs);
            }
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }

        return null;
    }

    @Nullable
    public List<SeasonModel> fetchListingByPage(int page_number)
    {
        String sql = "SELECT * FROM " + DatabaseTables.Seasons.seasons_TABLENAME +
                " LIMIT " + (page_number * 5) + ", " + ((page_number + 1) * 5) ;

        try (Connection conn = DriverManager.getConnection(
                DatabaseManager.database.getDatabaseUrl());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if( !DatabaseUtil.isResultSetNullOrEmpty(rs) )
            {
                List<SeasonModel> seasons = new ArrayList<>();

                while (rs.next()) {
                    seasons.add(new SeasonModel().loadDataFromResultSet(rs));
                }
                return seasons;
            }
        } catch (SQLException e) {
            ConsoleUtil.logError(
                    "SQLite query failed: " + sql);
            ConsoleUtil.logError( e.getMessage() );
        }

        return null;
    }
}
