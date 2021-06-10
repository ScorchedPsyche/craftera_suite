package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.SeasonModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.GameUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import org.jetbrains.annotations.Nullable;

public class SeasonManager {
    public SeasonManager(SeasonsDatabaseApi seasonsDatabaseApi) {
        this.seasonsDatabaseApi = seasonsDatabaseApi;
    }

    @Nullable
    public SeasonModel current;
    private SeasonsDatabaseApi seasonsDatabaseApi;

    public void createSeason(int number, String title, String subtitle, SuitePluginManager.Seasons.Status status,
                             boolean account, long date_start, long date_end, String version_start, String version_end){
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
            String msg = "Created Season " + number;
            if ( !StringUtil.isNullOrEmpty(title) )
            {
                msg += ": " + title;
            }
            if ( !StringUtil.isNullOrEmpty(subtitle) )
            {
                msg += " (" + subtitle + ")";
            }

            ConsoleUtil.logMessage( SuitePluginManager.Seasons.Name.full, msg );
        } else {
            ConsoleUtil.logError( SuitePluginManager.Seasons.Name.full, "Failed to create season.");
        }
    }

    public SeasonManager setCurrentSeason(SeasonModel currentSeason) {
        current = currentSeason;
        return this;
    }

    public boolean isCurrentSeasonValid() {
        return current != null;
    }

    public int getNextAvailableSeasonNumber()
    {
        seasonsDatabaseApi.fetchNextAvailableSeasonNumber();

        return 0;
    }
}
