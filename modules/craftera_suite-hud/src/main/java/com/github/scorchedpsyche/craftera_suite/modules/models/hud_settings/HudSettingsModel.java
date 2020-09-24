package com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HudSettingsModel {
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("display")
    @Expose
    private DisplayModel display;

    public Boolean getEnabled() {
    return enabled;
    }

    public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
    }

    public DisplayModel getDisplay() {
    return display;
    }

    public void setDisplay(DisplayModel display) {
    this.display = display;
    }
}