package modules.com.github.scorchedpsyche.craftera_suite.modules.model;

public class RangeAndLimitForHudModel {
    private double distance;
    private int range;

    public RangeAndLimitForHudModel(double distance, int range) {
        this.distance = distance;
        this.range = range;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
