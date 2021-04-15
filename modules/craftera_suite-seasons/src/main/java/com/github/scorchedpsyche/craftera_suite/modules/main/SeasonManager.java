package com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.model.SeasonModel;
import org.jetbrains.annotations.Nullable;

public class SeasonManager {
    public SeasonManager(SeasonsDatabaseApi seasonsDatabaseApi) {
        this.seasonsDatabaseApi = seasonsDatabaseApi;
    }

    @Nullable
    public SeasonModel current;
    private SeasonsDatabaseApi seasonsDatabaseApi;

    public SeasonManager setCurrentSeason(SeasonModel currentSeason) {
        current = currentSeason;
        return this;
    }
}
