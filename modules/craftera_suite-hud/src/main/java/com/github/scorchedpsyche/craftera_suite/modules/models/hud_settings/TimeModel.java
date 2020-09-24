package com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings;

import com.github.scorchedpsyche.craftera_suite.modules.models.hud_settings.ColorizeModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeModel {
    @SerializedName("server")
    @Expose
    private Boolean server;
    @SerializedName("world")
    @Expose
    private Boolean world;
    @SerializedName("colorize")
    @Expose
    private ColorizeModel colorize;

    public Boolean getServer() {
    return server;
    }

    public void setServer(Boolean server) {
    this.server = server;
    }

    public Boolean getWorld() {
    return world;
    }

    public void setWorld(Boolean world) {
    this.world = world;
    }

    public ColorizeModel getColorize() {
    return colorize;
    }

    public void setColorize(ColorizeModel colorize) {
    this.colorize = colorize;
    }
}