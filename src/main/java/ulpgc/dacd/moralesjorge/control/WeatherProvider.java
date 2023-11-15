package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;

import java.time.Instant;

public interface WeatherProvider {
    void getWeather(Location location, Instant timestamp) throws Exception;
}
