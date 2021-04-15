package com.github.scorchedpsyche.craftera_suite.modules.model;

public class AchievementModel {
    public AchievementModel(String playerUUid, String achievement, long date) {
        this.playerUUid = playerUUid;
        this.achievement = achievement;
        this.date = date;
    }

    public String playerUUid;
    public String achievement;
    public long date;
}
