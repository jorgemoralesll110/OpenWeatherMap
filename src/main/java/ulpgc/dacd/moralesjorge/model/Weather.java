package ulpgc.dacd.moralesjorge.model;

import java.time.Instant;

public class Weather {
    private double temperature;
    private double humidity;
    private double rain;
    private double wind;
    private double clouds;
    private Location location;
    private Instant timestamp;

    public Weather(double temperature, double humidity, double rain, double wind, double clouds, Location location, Instant timestamp) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.rain = rain;
        this.wind = wind;
        this.clouds = clouds;
        this.location = location;
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
