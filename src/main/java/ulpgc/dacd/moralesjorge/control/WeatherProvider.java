package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;

import java.time.Instant;

public interface WeatherProvider {
    String getWeather(Location location, Instant timestamp) throws Exception;
}
