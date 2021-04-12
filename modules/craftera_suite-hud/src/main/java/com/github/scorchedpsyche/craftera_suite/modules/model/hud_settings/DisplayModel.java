package com.github.scorchedpsyche.craftera_suite.modules.model.hud_settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisplayModel {
    @SerializedName("coordinates")
    @Expose
    private Boolean coordinates;
    @SerializedName("facing_direction")
    @Expose
    private Boolean facingDirection;
    @SerializedName("time")
    @Expose
    private TimeModel time;
    
    public Boolean getCoordinates() {
    return coordinates;
    }
    
    public void setCoordinates(Boolean coordinates) {
    this.coordinates = coordinates;
    }
    
    public Boolean getFacingDirection() {
    return facingDirection;
    }
    
    public void setFacingDirection(Boolean facingDirection) {
    this.facingDirection = facingDirection;
    }
    
    public TimeModel getTime() {
    return time;
    }
    
    public void setTime(TimeModel time) {
    this.time = time;
    }
}