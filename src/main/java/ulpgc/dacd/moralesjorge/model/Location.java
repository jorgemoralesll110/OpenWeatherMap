package ulpgc.dacd.moralesjorge.model;

public class Location {
    private String island;
    private double latitude;
    private double longitude;

    public Location(String island, double latitude, double longitude) {
        this.island = island;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
