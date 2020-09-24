package com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColorizeModel {
    @SerializedName("day_night_cycle")
    @Expose
    private Boolean dayNightCycle;
    @SerializedName("work_hours")
    @Expose
    private Boolean workHours;

    public Boolean getDayNightCycle() {
    return dayNightCycle;
    }

    public void setDayNightCycle(Boolean dayNightCycle) {
    this.dayNightCycle = dayNightCycle;
    }

    public Boolean getWorkHours() {
    return workHours;
    }

    public void setWorkHours(Boolean workHours) {
    this.workHours = workHours;
    }
}